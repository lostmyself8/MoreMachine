package com.jerry.mekmm.common.tile.machine;

import com.jerry.mekmm.common.registries.MMBlocks;
import com.jerry.mekmm.common.registries.MMChemicals;
import mekanism.api.*;
import mekanism.api.chemical.BasicChemicalTank;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalHandler;
import mekanism.api.chemical.IChemicalTank;
import mekanism.common.MekanismLang;
import mekanism.common.attachments.containers.ContainerType;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.container.sync.SyncableBoolean;
import mekanism.common.inventory.container.sync.chemical.SyncableChemicalStack;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.chemical.ChemicalInventorySlot;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TileEntityAmbientGasCollector extends TileEntityMekanism implements IConfigurable {
    /**
     * How many ticks it takes to run an operation.
     */
    private static final int BASE_TICKS_REQUIRED = 19;
    public static final int MAX_CHEMICAL = 10 * FluidType.BUCKET_VOLUME;
    private static final int BASE_OUTPUT_RATE = 256;

    //化学品存储槽
    public IChemicalTank chemicalTank;
    /**
     * The type of chemical this collector is collecting
     */
    @NotNull
    private ChemicalStack activeType = ChemicalStack.EMPTY;
    public int ticksRequired = BASE_TICKS_REQUIRED;
    /**
     * How many ticks this machine has been operating for.
     */
    public int operatingTicks;
    private boolean usedEnergy = false;
    private int outputRate = BASE_OUTPUT_RATE;

    private boolean noBlocking = true;
    private List<BlockCapabilityCache<IChemicalHandler, @Nullable Direction>> chemicalHandlerAbove = Collections.emptyList();

    private MachineEnergyContainer<TileEntityAmbientGasCollector> energyContainer;
    ChemicalInventorySlot chemicalSlot;
    EnergyInventorySlot energySlot;

    public TileEntityAmbientGasCollector(BlockPos pos, BlockState state) {
        super(MMBlocks.AMBIENT_GAS_COLLECTOR, pos, state);
    }

    @Override
    public @Nullable IChemicalTankHolder getInitialChemicalTanks(IContentsListener listener) {
        ChemicalTankHelper builder = ChemicalTankHelper.forSide(facingSupplier);
        builder.addTank(chemicalTank = BasicChemicalTank.output(MAX_CHEMICAL, listener), RelativeSide.LEFT, RelativeSide.RIGHT, RelativeSide.FRONT, RelativeSide.BACK);
        return builder.build();
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSide(facingSupplier);
        builder.addContainer(energyContainer = MachineEnergyContainer.input(this, listener), RelativeSide.BOTTOM, RelativeSide.LEFT, RelativeSide.RIGHT, RelativeSide.FRONT, RelativeSide.BACK);
        return builder.build();
    }

    @Override
    protected @Nullable IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSide(facingSupplier);
        builder.addSlot(chemicalSlot = ChemicalInventorySlot.drain(chemicalTank, listener, 28, 35),
                RelativeSide.BOTTOM, RelativeSide.LEFT, RelativeSide.RIGHT, RelativeSide.FRONT, RelativeSide.BACK);
        builder.addSlot(energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 143, 35), RelativeSide.BOTTOM, RelativeSide.LEFT, RelativeSide.RIGHT, RelativeSide.FRONT, RelativeSide.BACK);
        chemicalSlot.setSlotOverlay(SlotOverlay.PLUS);
        return builder.build();
    }

    @Override
    protected boolean onUpdateServer() {
        boolean sendUpdatePacket = super.onUpdateServer();
        energySlot.fillContainerOrConvert();
        chemicalSlot.drainTank();
        long clientEnergyUsed = 0L;
        if (canFunction() && (chemicalTank.isEmpty() || estimateIncrementAmount() <= chemicalTank.getNeeded())) {
            long energyPerTick = energyContainer.getEnergyPerTick();
            if (energyContainer.extract(energyPerTick, Action.SIMULATE, AutomationType.INTERNAL) == energyPerTick) {
                if (!activeType.isEmpty()) {
                    //If we have an active type of fluid, use energy. This can cause there to be ticks where there isn't actually
                    // anything to suck that use energy, but those will balance out with the first set of ticks where it doesn't
                    // use any energy until it actually picks up the first block
                    clientEnergyUsed = energyContainer.extract(energyPerTick, Action.EXECUTE, AutomationType.INTERNAL);
                }
                operatingTicks++;
                if (operatingTicks >= ticksRequired) {
                    operatingTicks = 0;
                    // 判断收集器上方是否是空气
                    if (suck(worldPosition.relative(Direction.UP))) {
                        if (clientEnergyUsed == 0L) {
                            //If it didn't already have an active type (hasn't used energy this tick), then extract energy
                            clientEnergyUsed = energyContainer.extract(energyPerTick, Action.EXECUTE, AutomationType.INTERNAL);
                        }
                    } else {
                        reset();
                    }
                }
            }
        }
        usedEnergy = clientEnergyUsed > 0L;
        if (!chemicalTank.isEmpty()) {
            if (chemicalHandlerAbove.isEmpty()) {
                chemicalHandlerAbove = List.of(Capabilities.CHEMICAL.createCache((ServerLevel) level, worldPosition.north(), Direction.UP));
            }
            ChemicalUtil.emit(chemicalHandlerAbove, chemicalTank, outputRate);
        }
        return sendUpdatePacket;
    }

    public int estimateIncrementAmount() {
        return 1;
    }

    private boolean suck(BlockPos pos) {
        Optional<BlockState> state = WorldUtils.getBlockState(level, pos);
        if (state.isPresent()) {
            BlockState blockState = state.get();
            Block block = blockState.getBlock();
            if (isAir(block)) {
                ChemicalStack chemicalStack = new ChemicalStack(MMChemicals.UNSTABLE_DIMENSIONAL_GAS, 1);
                activeType = chemicalStack.copyWithAmount(1);
                chemicalTank.insert(chemicalStack, Action.EXECUTE, AutomationType.INTERNAL);
                return true;
            }
        }
        return false;
    }

    public boolean isAir(Block block) {
        return noBlocking = block == Blocks.AIR;
    }

    public boolean getNotBlocking() {
        return noBlocking;
    }

    public void reset() {
        activeType = ChemicalStack.EMPTY;
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbtTags, HolderLookup.@NotNull Provider provider) {
        super.saveAdditional(nbtTags, provider);
        if (!activeType.isEmpty()) {
            nbtTags.put(SerializationConstants.CHEMICAL, activeType.save(provider));
        }
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.loadAdditional(nbt, provider);
        operatingTicks = nbt.getInt(SerializationConstants.PROGRESS);
        NBTUtils.setChemicalStackIfPresent(provider, nbt, SerializationConstants.CHEMICAL, chemical -> activeType = chemical);
    }

    @Override
    public InteractionResult onSneakRightClick(Player player) {
        reset();
        player.displayClientMessage(MekanismLang.PUMP_RESET.translate(), true);
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult onRightClick(Player player) {
        return InteractionResult.PASS;
    }

    @Override
    public boolean supportsMode(RedstoneControl mode) {
        return true;
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (upgrade == Upgrade.SPEED) {
            ticksRequired = MekanismUtils.getTicks(this, BASE_TICKS_REQUIRED);
            outputRate = BASE_OUTPUT_RATE * (1 + upgradeComponent.getUpgrades(Upgrade.SPEED));
        }
    }

    @Override
    public int getRedstoneLevel() {
        return MekanismUtils.redstoneLevelFromContents(chemicalTank.getStored(), chemicalTank.getCapacity());
    }

    @Override
    protected boolean makesComparatorDirty(ContainerType<?, ?, ?> type) {
        return type == ContainerType.CHEMICAL;
    }

    @NotNull
    @Override
    public List<Component> getInfo(@NotNull Upgrade upgrade) {
        return UpgradeUtils.getMultScaledInfo(this, upgrade);
    }

    public MachineEnergyContainer<TileEntityAmbientGasCollector> getEnergyContainer() {
        return energyContainer;
    }

    public boolean usedEnergy() {
        return usedEnergy;
    }

    @NotNull
    public ChemicalStack getActiveType() {
        return this.activeType;
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableBoolean.create(this::usedEnergy, value -> usedEnergy = value));
        container.track(SyncableBoolean.create(this::getNotBlocking, value -> noBlocking = value));
        container.track(SyncableChemicalStack.create(this::getActiveType, value -> activeType = value));
    }
}
