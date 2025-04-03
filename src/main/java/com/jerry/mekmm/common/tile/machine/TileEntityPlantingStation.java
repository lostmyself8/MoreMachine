package com.jerry.mekmm.common.tile.machine;

import com.jerry.mekmm.api.recipes.PlantingRecipe;
import com.jerry.mekmm.api.recipes.cache.PlantingStationCachedRecipe;
import com.jerry.mekmm.api.recipes.cache.PlantingStationPerTickCachedRecipe;
import com.jerry.mekmm.api.recipes.outputs.MMOutputHelper;
import com.jerry.mekmm.client.recipe_viewer.type.IMMRecipeViewerRecipeType;
import com.jerry.mekmm.client.recipe_viewer.type.MMRecipeViewerRecipeType;
import com.jerry.mekmm.common.recipe.IMMRecipeTypeProvider;
import com.jerry.mekmm.common.recipe.MMRecipeType;
import com.jerry.mekmm.common.recipe.lookup.IMMDoubleRecipeLookupHandler;
import com.jerry.mekmm.common.recipe.lookup.cache.MMInputRecipeCache;
import com.jerry.mekmm.common.registries.MMBlocks;
import com.jerry.mekmm.common.tile.prefab.MMTileEntityProgressMachine;
import mekanism.api.IContentsListener;
import mekanism.api.SerializationConstants;
import mekanism.api.Upgrade;
import mekanism.api.chemical.BasicChemicalTank;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalTank;
import mekanism.api.functions.ConstantPredicates;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.ItemStackConstantChemicalToObjectCachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.ILongInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.inventory.slot.chemical.ChemicalInventorySlot;
import mekanism.common.inventory.warning.WarningTracker;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.lookup.IRecipeLookupHandler;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.upgrade.IUpgradeData;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.StatUtils;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class TileEntityPlantingStation extends MMTileEntityProgressMachine<PlantingRecipe> implements IRecipeLookupHandler.ConstantUsageRecipeLookupHandler,
        IMMDoubleRecipeLookupHandler.ItemChemicalRecipeLookupHandler<PlantingRecipe> {

    public static final CachedRecipe.OperationTracker.RecipeError NOT_ENOUGH_SPACE_SECONDARY_OUTPUT_ERROR = CachedRecipe.OperationTracker.RecipeError.create();
    private static final List<CachedRecipe.OperationTracker.RecipeError> TRACKED_ERROR_TYPES = List.of(
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY_REDUCED_RATE,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_SECONDARY_INPUT,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            NOT_ENOUGH_SPACE_SECONDARY_OUTPUT_ERROR,
            CachedRecipe.OperationTracker.RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT
    );

    public static final int BASE_TICKS_REQUIRED = 10 * SharedConstants.TICKS_PER_SECOND;

    public static final long MAX_GAS = 210;

    private long baseTotalUsage;

    //化学品存储槽
    public IChemicalTank chemicalTank;

    private final ItemStackConstantChemicalToObjectCachedRecipe.ChemicalUsageMultiplier injectUsageMultiplier;
    private double injectUsage = 1;
    private long usedSoFar;

    private final IOutputHandler<PlantingRecipe.PlantingStationRecipeOutput> outputHandler;
    private final IInputHandler<ItemStack> itemInputHandler;
    private final ILongInputHandler<ChemicalStack> chemicalInputHandler;

    private MachineEnergyContainer<TileEntityPlantingStation> energyContainer;

    InputInventorySlot inputSlot;
    OutputInventorySlot mainOutputSlot;
    OutputInventorySlot secondaryOutputSlot;
    //气罐槽
    ChemicalInventorySlot chemicalSlot;
    EnergyInventorySlot energySlot;

    public TileEntityPlantingStation(BlockPos pos, BlockState state) {
        super(MMBlocks.PLANTING_STATION, pos, state, TRACKED_ERROR_TYPES, BASE_TICKS_REQUIRED);
        configComponent.setupItemIOExtraConfig(inputSlot, mainOutputSlot, chemicalSlot, energySlot);
        configComponent.setupItemIOConfig(Collections.singletonList(inputSlot), List.of(mainOutputSlot, secondaryOutputSlot), energySlot, false);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        configComponent.setupInputConfig(TransmissionType.CHEMICAL, chemicalTank);

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM)
                .setCanTankEject(tank -> tank != chemicalTank);

        itemInputHandler = InputHelper.getInputHandler(inputSlot, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT);
        chemicalInputHandler = InputHelper.getConstantInputHandler(chemicalTank);
        outputHandler = MMOutputHelper.getOutputHandler(mainOutputSlot, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE, secondaryOutputSlot, NOT_ENOUGH_SPACE_SECONDARY_OUTPUT_ERROR);

        baseTotalUsage = baseTicksRequired;
        injectUsageMultiplier = (usedSoFar, operatingTicks) -> StatUtils.inversePoisson(injectUsage);
    }

    @NotNull
    @Override
    public IChemicalTankHolder getInitialChemicalTanks(IContentsListener listener, IContentsListener recipeCacheListener, IContentsListener recipeCacheUnpauseListener) {
        ChemicalTankHelper builder = ChemicalTankHelper.forSideWithConfig(this);
        builder.addTank(chemicalTank = BasicChemicalTank.createModern(MAX_GAS, allowExtractingChemical() ? ConstantPredicates.alwaysTrueBi() : ConstantPredicates.notExternal(),
                (gas, automationType) -> containsRecipeBA(inputSlot.getStack(), gas), this::containsRecipeB, recipeCacheListener));
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
        builder.addSlot(inputSlot = InputInventorySlot.at(item -> containsRecipeAB(item, chemicalTank.getStack()), this::containsRecipeA, recipeCacheListener, 56, 17))
                .tracksWarnings(slot -> slot.warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT)));
        builder.addSlot(chemicalSlot = ChemicalInventorySlot.fillOrConvert(chemicalTank, this::getLevel, listener, 56, 53));
        builder.addSlot(mainOutputSlot = OutputInventorySlot.at(recipeCacheUnpauseListener, 116, 35));
        builder.addSlot(secondaryOutputSlot = OutputInventorySlot.at(recipeCacheUnpauseListener, 132, 35));
        builder.addSlot(energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 31, 35));
        return builder.build();
    }

    protected boolean allowExtractingChemical() {
        return !useStatisticalMechanics();
    }

    protected boolean useStatisticalMechanics() {
        return false;
    }

    @Override
    protected boolean onUpdateServer() {
        boolean sendUpdatePacket = super.onUpdateServer();
        energySlot.fillContainerOrConvert();
        chemicalSlot.fillTankOrConvert();
        recipeCacheLookupMonitor.updateAndProcess();
        return sendUpdatePacket;
    }

    @Override
    public @NotNull IMMRecipeTypeProvider<?, PlantingRecipe, MMInputRecipeCache.ItemChemical<PlantingRecipe>> getMMRecipeType() {
        return MMRecipeType.PLANTING_STATION;
    }

    @Override
    public @Nullable IMMRecipeViewerRecipeType<PlantingRecipe> recipeViewerType() {
        return MMRecipeViewerRecipeType.PLANTING_STATION;
    }

    @Override
    public @Nullable PlantingRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(itemInputHandler, chemicalInputHandler);
    }

    @Override
    public @NotNull CachedRecipe<PlantingRecipe> createNewCachedRecipe(@NotNull PlantingRecipe recipe, int cacheIndex) {
        CachedRecipe<PlantingRecipe> cachedRecipe;
        if (recipe.perTickUsage()) {
            cachedRecipe = new PlantingStationPerTickCachedRecipe(recipe, recheckAllRecipeErrors, itemInputHandler, chemicalInputHandler,
                    injectUsageMultiplier, used -> usedSoFar = used, outputHandler, ConstantPredicates.alwaysFalse());
        } else {
            cachedRecipe = new PlantingStationCachedRecipe(recipe, recheckAllRecipeErrors, itemInputHandler, chemicalInputHandler, outputHandler, ConstantPredicates.alwaysFalse());
        }
        return cachedRecipe
                //设置错误更改
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(this::canFunction)
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setRequiredTicks(this::getTicksRequired)
                .setOnFinish(this::markForSave)
                .setOperatingTicksChanged(this::setOperatingTicks);
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (upgrade == Upgrade.CHEMICAL || upgrade == Upgrade.SPEED) {
            injectUsage = MekanismUtils.getGasPerTickMeanMultiplier(this);
        } else {
            baseTotalUsage = MekanismUtils.getBaseUsage(this, baseTicksRequired);
        }
    }

    @Override
    public @Nullable IUpgradeData getUpgradeData(HolderLookup.Provider provider) {
        return super.getUpgradeData(provider);
    }

    public MachineEnergyContainer<TileEntityPlantingStation> getEnergyContainer() {
        return energyContainer;
    }

    @Override
    public long getSavedUsedSoFar(int cacheIndex) {
        return usedSoFar;
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag nbt, @NotNull HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);
        usedSoFar = nbt.getLong(SerializationConstants.USED_SO_FAR);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbtTags, @NotNull HolderLookup.Provider provider) {
        super.saveAdditional(nbtTags, provider);
        nbtTags.putLong(SerializationConstants.USED_SO_FAR, usedSoFar);
    }

    @Override
    public boolean isConfigurationDataCompatible(Block blockType) {
        return super.isConfigurationDataCompatible(blockType) || MekanismUtils.isSameTypeFactory(getBlockHolder(), blockType);
    }
}
