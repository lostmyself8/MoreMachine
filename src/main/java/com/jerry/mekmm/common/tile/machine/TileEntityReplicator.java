package com.jerry.mekmm.common.tile.machine;

import com.jerry.mekmm.api.recipes.basic.MMBasicItemStackChemicalToItemStackRecipe;
import com.jerry.mekmm.client.recipe_viewer.type.IMMRecipeViewerRecipeType;
import com.jerry.mekmm.client.recipe_viewer.type.MMRecipeViewerRecipeType;
import com.jerry.mekmm.common.config.MMConfig;
import com.jerry.mekmm.common.recipe.IMMRecipeTypeProvider;
import com.jerry.mekmm.common.recipe.impl.ReplicatorIRecipeSingle;
import com.jerry.mekmm.common.registries.MMBlocks;
import com.jerry.mekmm.common.registries.MMChemicals;
import com.jerry.mekmm.common.tile.prefab.MMTileEntityProgressMachine;
import mekanism.api.IContentsListener;
import mekanism.api.SerializationConstants;
import mekanism.api.chemical.BasicChemicalTank;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalTank;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.TwoInputCachedRecipe;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.ILongInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.integration.computer.annotation.ComputerMethod;
import mekanism.common.integration.computer.computercraft.ComputerConstants;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.container.sync.SyncableLong;
import mekanism.common.inventory.container.sync.SyncableRegistryEntry;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.inventory.slot.chemical.ChemicalInventorySlot;
import mekanism.common.inventory.warning.WarningTracker;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.registries.MekanismDataComponents;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.util.NBTUtils;
import mekanism.common.util.RegistryUtils;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TileEntityReplicator extends MMTileEntityProgressMachine<MMBasicItemStackChemicalToItemStackRecipe> {

    private static final List<CachedRecipe.OperationTracker.RecipeError> TRACKED_ERROR_TYPES = List.of(
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_SECONDARY_INPUT,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            CachedRecipe.OperationTracker.RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT
    );

    private static final int BASE_TICKS_REQUIRED = 10 * SharedConstants.TICKS_PER_SECOND;
    public static final long MAX_GAS = 10 * FluidType.BUCKET_VOLUME;

    public static HashMap<String, Integer> customRecipeMap = getRecipeFromConfig();

    //化学品存储槽
    public IChemicalTank chemicalTank;

    private MachineEnergyContainer<TileEntityReplicator> energyContainer;

    protected final IInputHandler<@NotNull ItemStack> itemInputHandler;
    private final IOutputHandler<ItemStack> outputHandler;
    private final ILongInputHandler<ChemicalStack> chemicalInputHandler;

    InputInventorySlot inputSlot;
    OutputInventorySlot outputSlot;
    //气罐槽
    ChemicalInventorySlot chemicalSlot;
    EnergyInventorySlot energySlot;

    private long clientEnergyUsed = 0L;
    private Item inverseReplaceTarget = Items.AIR;

    public TileEntityReplicator(BlockPos pos, BlockState state) {
        super(MMBlocks.REPLICATOR, pos, state, TRACKED_ERROR_TYPES, BASE_TICKS_REQUIRED);
        configComponent.setupItemIOExtraConfig(inputSlot, outputSlot, chemicalSlot, energySlot);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        configComponent.setupInputConfig(TransmissionType.CHEMICAL, chemicalTank);

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM)
                .setCanTankEject(tank -> tank != chemicalTank);

        chemicalInputHandler = InputHelper.getConstantInputHandler(chemicalTank);
        itemInputHandler = InputHelper.getInputHandler(inputSlot, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT);
        outputHandler = OutputHelper.getOutputHandler(outputSlot, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
    }

    public static HashMap<String, Integer> getRecipeFromConfig() {
        HashMap<String, Integer> map = new HashMap<>();
        List<?> pre = MMConfig.general.duplicatorRecipe.get();
        List<String> recipes = new ArrayList<>();
        for (Object item : pre) {
            if (item instanceof String list) {
                recipes.add(list);
            }
        }
        if (recipes.isEmpty()) return null;
        for (String element : recipes) {
            String[] parts = element.split("#", 2); // 分割成最多两部分
            if (parts.length != 2) continue;

            String key = parts[0];
            int value = Integer.parseInt(parts[1]);
            map.put(key, value);
        }
        return map;
    }

    @NotNull
    @Override
    public IChemicalTankHolder getInitialChemicalTanks(IContentsListener listener, IContentsListener recipeCacheListener, IContentsListener recipeCacheUnpauseListener) {
        ChemicalTankHelper builder = ChemicalTankHelper.forSideWithConfig(this);
        builder.addTank(chemicalTank = BasicChemicalTank.inputModern(MAX_GAS, TileEntityReplicator::isValidChemicalInput, recipeCacheListener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener, IContentsListener recipeCacheListener, IContentsListener recipeCacheUnpauseListener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this);
        builder.addContainer(energyContainer = MachineEnergyContainer.input(this, recipeCacheUnpauseListener));
        return builder.build();
    }

    @Override
    protected @Nullable IInventorySlotHolder getInitialInventory(IContentsListener listener, IContentsListener recipeCacheListener, IContentsListener recipeCacheUnpauseListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this);
        builder.addSlot(inputSlot = InputInventorySlot.at(TileEntityReplicator::isValidItemInput, recipeCacheListener, 29, 32)
        ).tracksWarnings(slot -> slot.warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT)));
        //输出槽位置
        builder.addSlot(outputSlot = OutputInventorySlot.at(listener,131, 32))
                .tracksWarnings(slot -> slot.warning(WarningTracker.WarningType.NO_SPACE_IN_OUTPUT, getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE)));
        //化学品罐槽位置
        builder.addSlot(chemicalSlot = ChemicalInventorySlot.fillOrConvert(chemicalTank, this::getLevel, listener, 8, 65));
        //能量槽位置
        builder.addSlot(energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 152, 65));
        //化学品罐槽减号图标
        chemicalSlot.setSlotOverlay(SlotOverlay.MINUS);
        return builder.build();
    }

    public double getProcessRate() {
        return (double) clientEnergyUsed / energyContainer.getEnergyPerTick();
    }

    @ComputerMethod(nameOverride = "getEnergyUsage", methodDescription = ComputerConstants.DESCRIPTION_GET_ENERGY_USAGE)
    public long getEnergyUsed() {
        return clientEnergyUsed;
    }

    public static boolean isValidChemicalInput(ChemicalStack stack) {
        return stack.is(MMChemicals.UU_MATTER);
    }

    public static boolean isValidItemInput(ItemStack stack) {
//        Item item = stack.getItem();
//        if (customRecipeMap != null) {
//            for (String resourceKey : customRecipeMap.keySet()) {
//                return RegistryUtils.getName(item).toString().equals(resourceKey);
//            }
//        }
        return true;
    }

    @Override
    protected boolean onUpdateServer() {
        boolean sendUpdatePacket = super.onUpdateServer();
        energySlot.fillContainerOrConvert();
        chemicalSlot.fillTankOrConvert();
        clientEnergyUsed = recipeCacheLookupMonitor.updateAndProcess(energyContainer);
        return sendUpdatePacket;
    }

    public Item getInverseReplaceTarget() {
        return inverseReplaceTarget;
    }

    public void setInverseReplaceTarget(Item target) {
        if (target != inverseReplaceTarget) {
            inverseReplaceTarget = target;
            markForSave();
        }
    }

    private boolean inverseReplaceTargetMatches(Item target) {
        return inverseReplaceTarget != Items.AIR && inverseReplaceTarget == target;
    }

    @Override
    public void writeSustainedData(HolderLookup.Provider provider, CompoundTag data) {
        super.writeSustainedData(provider, data);
        if (inverseReplaceTarget != Items.AIR) {
            NBTUtils.writeRegistryEntry(data, SerializationConstants.REPLACE_TARGET, BuiltInRegistries.ITEM, inverseReplaceTarget);
        }
    }

    @Override
    public void readSustainedData(HolderLookup.Provider provider, CompoundTag data) {
        super.readSustainedData(provider, data);
        inverseReplaceTarget = NBTUtils.readRegistryEntry(data, SerializationConstants.REPLACE_TARGET, BuiltInRegistries.ITEM, Items.AIR);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.@NotNull Builder builder) {
        super.collectImplicitComponents(builder);
        builder.set(MekanismDataComponents.REPLACE_STACK, inverseReplaceTarget);
    }

    @Override
    protected void applyImplicitComponents(@NotNull DataComponentInput input) {
        super.applyImplicitComponents(input);
        inverseReplaceTarget = input.getOrDefault(MekanismDataComponents.REPLACE_STACK, inverseReplaceTarget);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbtTags, HolderLookup.@NotNull Provider provider) {
        super.saveAdditional(nbtTags, provider);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.loadAdditional(nbt, provider);
    }

    public @Nullable MachineEnergyContainer<TileEntityReplicator> getEnergyContainer() {
        return energyContainer;
    }

    @Override
    public @NotNull IMMRecipeTypeProvider<?, MMBasicItemStackChemicalToItemStackRecipe, ?> getMMRecipeType() {
        return null;
    }

    @Override
    public @Nullable MMBasicItemStackChemicalToItemStackRecipe getRecipe(int cacheIndex) {
        return getRecipe(itemInputHandler.getInput(), chemicalInputHandler.getInput());
    }

    @Override
    public @NotNull CachedRecipe<MMBasicItemStackChemicalToItemStackRecipe> createNewCachedRecipe(@NotNull MMBasicItemStackChemicalToItemStackRecipe recipe, int cacheIndex) {
        return TwoInputCachedRecipe.itemChemicalToItem(recipe, recheckAllRecipeErrors, itemInputHandler, chemicalInputHandler, outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(this::canFunction)
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setRequiredTicks(this::getTicksRequired)
                .setOnFinish(this::markForSave)
                .setOperatingTicksChanged(this::setOperatingTicks);
    }

    @Override
    public @Nullable IMMRecipeViewerRecipeType<MMBasicItemStackChemicalToItemStackRecipe> recipeViewerType() {
        return MMRecipeViewerRecipeType.REPLICATOR;
    }

    public static MMBasicItemStackChemicalToItemStackRecipe getRecipe(ItemStack itemStack, ChemicalStack chemicalStack) {
        if (chemicalStack.isEmpty() || itemStack.isEmpty()) {
            return null;
        }
        if (customRecipeMap != null) {
            Item item = itemStack.getItem();
            //如果为空则赋值为0
            int amount = customRecipeMap.getOrDefault(RegistryUtils.getName(itemStack.getItemHolder()).toString(), 0);
            //防止null和配置文件中出现0
            if (amount == 0) return null;
            return new ReplicatorIRecipeSingle(
                    IngredientCreatorAccess.item().from(item, 1),
                    IngredientCreatorAccess.chemicalStack().fromHolder(MMChemicals.UU_MATTER, amount),
                    new ItemStack(item, 2)
            );
        }
        return null;
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableLong.create(this::getEnergyUsed, value -> clientEnergyUsed = value));
        container.track(SyncableRegistryEntry.create(BuiltInRegistries.ITEM, this::getInverseReplaceTarget, value -> inverseReplaceTarget = value));
    }
}
