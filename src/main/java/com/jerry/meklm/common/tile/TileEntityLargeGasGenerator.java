package com.jerry.meklm.common.tile;

import com.jerry.meklm.common.config.LMConfig;
import com.jerry.meklm.common.registries.LMBlocks;
import mekanism.api.*;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.attribute.ChemicalAttributes;
import mekanism.api.datamaps.IMekanismDataMapTypes;
import mekanism.api.datamaps.chemical.attribute.ChemicalFuel;
import mekanism.api.functions.ConstantPredicates;
import mekanism.api.math.MathUtils;
import mekanism.common.attachments.containers.ContainerType;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.capabilities.chemical.VariableCapacityChemicalTank;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper.ComputerChemicalTankWrapper;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper;
import mekanism.common.integration.computer.annotation.ComputerMethod;
import mekanism.common.integration.computer.annotation.WrappingComputerMethod;
import mekanism.common.integration.energy.EnergyCompatUtils;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.container.sync.SyncableDouble;
import mekanism.common.inventory.container.sync.SyncableInt;
import mekanism.common.inventory.container.sync.SyncableLong;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.chemical.ChemicalInventorySlot;
import mekanism.common.tile.interfaces.IBoundingBlock;
import mekanism.common.util.ChemicalUtil;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class TileEntityLargeGasGenerator extends LMTileEntityGenerator implements IBoundingBlock {

    private static final RelativeSide[] ENERGY_SIDES = {RelativeSide.TOP};
    private static final long DEFAULT_VALUE = MathUtils.multiplyClamped(/*MMConfig.general.conversionMultiplier.getAsLong()*/27, ChemicalUtil.hydrogenEnergyPerTick());

    @SuppressWarnings("removal")
    public static final Predicate<ChemicalStack> HAS_FUEL = chemical -> chemical.getData(IMekanismDataMapTypes.INSTANCE.chemicalFuel()) != null
            || chemical.hasLegacy(ChemicalAttributes.Fuel.class);

    /**
     * The tank this block is storing fuel in.
     */
    @WrappingComputerMethod(wrapper = ComputerChemicalTankWrapper.class, methodNames = {"getFuel", "getFuelCapacity", "getFuelNeeded",
                                                                                        "getFuelFilledPercentage"}, docPlaceholder = "fuel tank")
    public FuelTank fuelTank;
    private long burnTicks;
    private int maxBurnTicks;
    private long generationRate = 0;
    private double gasUsedLastTick;
    private int numPowering;

    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getFuelItem", docPlaceholder = "fuel item slot")
    ChemicalInventorySlot fuelSlot;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getEnergyItem", docPlaceholder = "energy item slot")
    EnergyInventorySlot energySlot;

    public TileEntityLargeGasGenerator(BlockPos pos, BlockState state) {
        super(LMBlocks.LARGE_GAS_BURNING_GENERATOR, pos, state, DEFAULT_VALUE);
    }

    @Override
    protected RelativeSide[] getEnergySides() {
        return ENERGY_SIDES;
    }

    @NotNull
    @Override
    public IChemicalTankHolder getInitialChemicalTanks(IContentsListener listener) {
        ChemicalTankHelper builder = ChemicalTankHelper.forSide(facingSupplier);
        builder.addTank(fuelTank = new FuelTank(listener), RelativeSide.LEFT, RelativeSide.RIGHT, RelativeSide.BACK);
        return builder.build();
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSide(facingSupplier);
        builder.addSlot(fuelSlot = ChemicalInventorySlot.fill(fuelTank, listener, 17, 35), RelativeSide.LEFT, RelativeSide.BACK, RelativeSide.TOP,
              RelativeSide.BOTTOM);
        builder.addSlot(energySlot = EnergyInventorySlot.drain(getEnergyContainer(), listener, 143, 35), RelativeSide.RIGHT);
        fuelSlot.setSlotOverlay(SlotOverlay.MINUS);
        return builder.build();
    }

    @Override
    protected boolean onUpdateServer() {
        boolean sendUpdatePacket = super.onUpdateServer();
        energySlot.drainContainer();
        fuelSlot.fillTank();

        if (!fuelTank.isEmpty() && canFunction() && getEnergyContainer().insert(generationRate, Action.SIMULATE, AutomationType.INTERNAL) == 0L) {
            setActive(true);
            if (!fuelTank.isEmpty()) {
                ChemicalFuel fuel = fuelTank.getFuel();
                if (fuel != null) {
                    //Ensure valid data
                    maxBurnTicks = Math.max(1, fuel.burnTicks());
                    generationRate = fuel.energyPerTick();
                }
            }

            //用了多少
            long toUse = getToUse();
            //与发电比例相乘得到生成的电量
            long toUseGeneration = MathUtils.multiplyClamped(generationRate, toUse);
            updateMaxOutputRaw(Math.max(DEFAULT_VALUE, toUseGeneration));

            long total = burnTicks + fuelTank.getStored() * maxBurnTicks;
            total -= toUse;
            getEnergyContainer().insert(toUseGeneration, Action.EXECUTE, AutomationType.INTERNAL);
            if (!fuelTank.isEmpty()) {
                //TODO: Improve this as it is sort of hacky
                fuelTank.setStack(fuelTank.getStack().copyWithAmount(total / maxBurnTicks));
            }
            burnTicks = total % maxBurnTicks;
            gasUsedLastTick = toUse / (double) maxBurnTicks;
        } else {
            if (fuelTank.isEmpty() && burnTicks == 0) {
                reset();
            }
            gasUsedLastTick = 0;
            setActive(false);
        }
        return sendUpdatePacket;
    }

    private void reset() {
        burnTicks = 0;
        maxBurnTicks = 0;
        generationRate = 0L;
        updateMaxOutputRaw(DEFAULT_VALUE);
    }

    private long getToUse() {
        if (generationRate == 0L || fuelTank.isEmpty()) {
            return 0;
        }
        long max = (long) Math.ceil(256 * (fuelTank.getStored() / (double) fuelTank.getCapacity()));
        max = Math.min(maxBurnTicks * fuelTank.getStored() + burnTicks, max);
        max = Math.min(MathUtils.clampToLong(getEnergyContainer().getNeeded() / (double) generationRate), max);
        return max * getUpgradeCount();
    }

    private int getUpgradeCount() {
        int count = 10;
        if (supportsUpgrades()) {
            for (Upgrade upgrade : getSupportedUpgrade()) {
                if (upgrade == Upgrade.SPEED) {
                    count++;
                }
            }
        }
        return count;
    }

    public long getGenerationRate() {
        return generationRate;
    }

    @ComputerMethod(nameOverride = "getBurnRate")
    public double getUsed() {
        return Math.round(gasUsedLastTick * 100) / 100D;
    }

    public int getMaxBurnTicks() {
        return maxBurnTicks;
    }

    @Override
    public int getRedstoneLevel() {
        return MekanismUtils.redstoneLevelFromContents(fuelTank.getStored(), fuelTank.getCapacity());
    }

    @Override
    protected boolean makesComparatorDirty(ContainerType<?, ?, ?> type) {
        return type == ContainerType.CHEMICAL;
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableLong.create(this::getGenerationRate, value -> generationRate = value));
        container.track(syncableMaxOutput());
        container.track(SyncableDouble.create(this::getUsed, value -> gasUsedLastTick = value));
        container.track(SyncableInt.create(this::getMaxBurnTicks, value -> maxBurnTicks = value));
    }

    @Override
    public boolean isPowered() {
        return redstone || numPowering > 0;
    }

    @Override
    public void onBoundingBlockPowerChange(BlockPos boundingPos, int oldLevel, int newLevel) {
        if (oldLevel > 0) {
            if (newLevel == 0) {
                numPowering--;
            }
        } else if (newLevel > 0) {
            numPowering++;
        }
    }

//    @Override
//    public int getBoundingComparatorSignal(Vec3i offset) {
//        return IBoundingBlock.super.getBoundingComparatorSignal(offset);
//    }

    @Override
    public <T> @Nullable T getOffsetCapability(@NotNull BlockCapability<T, @Nullable Direction> capability, @Nullable Direction side, @NotNull Vec3i offset) {
//        Field chemicalField;
//        try {
//            chemicalField = TileEntityMekanism.class.getDeclaredField("chemicalHandlerManager");
//            chemicalField.setAccessible(true);
//            if (capability == Capabilities.CHEMICAL.block()) {
//                return Objects.requireNonNull((ChemicalHandlerManager) chemicalField.get(this), "Expected to have chemical handler").resolve(capability, side);
//            }
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
        return WorldUtils.getCapability(level, capability, worldPosition, null, this, side);
    }

    @Override
    public boolean isOffsetCapabilityDisabled(@NotNull BlockCapability<?, @Nullable Direction> capability, Direction side, @NotNull Vec3i offset) {
        if (capability == Capabilities.CHEMICAL.block()) {
            return notChemicalPort(side, offset);
        } else if (EnergyCompatUtils.isEnergyCapability(capability)) {
            return notEnergyPort(side, offset);
        }
        //If we are not an item handler or energy capability, and it is a capability that we can support,
        // but it is one that normally should be disabled for offset capabilities, then expose it but only do so
        // via our ports for things like computer integration capabilities, then we treat the capability as
        // disabled if it is not against one of our ports
        return notChemicalPort(side, offset) && notEnergyPort(side, offset);
    }

    private boolean notChemicalPort(Direction side, Vec3i offset) {
        Direction left = getLeftSide();
        if (offset.equals(new Vec3i(left.getStepX(), 0, left.getStepZ())) || offset.equals(new Vec3i(left.getStepX(), 1, left.getStepZ()))) {
            //Disable if left power port but wrong side of the port
            return side != left;
        }
        Direction right = left.getOpposite();
        if (offset.equals(new Vec3i(right.getStepX(), 0, right.getStepZ())) || offset.equals(new Vec3i(right.getStepX(), 1, right.getStepZ()))) {
            //Disable if right power port but wrong side of the port
            return side != right;
        }
        return true;
    }

    private boolean notEnergyPort(Direction side, Vec3i offset) {
        if (offset.equals(new Vec3i(0, 2, 0))) {
            //If output then disable if wrong face of output
            return side != Direction.UP;
        }
        return true;
    }

    //Methods relating to IComputerTile
    @Override
    long getProductionRate() {
        return MathUtils.clampToLong(getGenerationRate() * getUsed() * getMaxBurnTicks());
    }
    //End methods IComputerTile

    //Implementation of gas tank that on no longer being empty updates the output rate of this generator
    public class FuelTank extends VariableCapacityChemicalTank {

        protected FuelTank(@Nullable IContentsListener listener) {
            super(LMConfig.generators.lgbgTankCapacity, ConstantPredicates.notExternal(), ConstantPredicates.alwaysTrueBi(), HAS_FUEL, null, listener);
        }

        @Override
        public void setStack(@NotNull ChemicalStack stack) {
            boolean wasEmpty = isEmpty();
            super.setStack(stack);
            recheckOutput(stack, wasEmpty);
        }

        @Override
        public void setStackUnchecked(@NotNull ChemicalStack stack) {
            boolean wasEmpty = isEmpty();
            super.setStackUnchecked(stack);
            recheckOutput(stack, wasEmpty);
        }

        private void recheckOutput(@NotNull ChemicalStack stack, boolean wasEmpty) {
            if (wasEmpty && !stack.isEmpty()) {
                ChemicalFuel fuel = getFuel();
                if (fuel != null) {
                    updateMaxOutputRaw(fuel.energyPerTick());
                }
            }
        }

        @Nullable
        @SuppressWarnings("removal")
        public ChemicalFuel getFuel() {
            if (isEmpty()) {
                return null;
            }
            ChemicalStack stack = getStack();
            ChemicalFuel fuel = stack.getData(IMekanismDataMapTypes.INSTANCE.chemicalFuel());
            if (fuel == null) {//TODO - 1.22: Remove this handling of legacy data
                //If there is no fuel in the data map, see if one was set manually on the stack
                ChemicalAttributes.Fuel legacyFuel = stack.getLegacy(ChemicalAttributes.Fuel.class);
                if (legacyFuel != null) {
                    //If it was, convert it to the non legacy type
                    return legacyFuel.asModern();
                }
            }
            return fuel;
        }
    }
}