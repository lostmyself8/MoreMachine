package com.jerry.mekmm.api.recipes.cache;

import com.jerry.mekmm.api.recipes.PlantingRecipe;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.ItemStackConstantChemicalToObjectCachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.ILongInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.LongConsumer;
import java.util.function.Predicate;

@NothingNullByDefault
public class PlantingStationPerTickCachedRecipe extends CachedRecipe<PlantingRecipe> {

    private final Predicate<PlantingRecipe.PlantingStationRecipeOutput> outputEmptyCheck;

    private final IInputHandler<ItemStack> itemInputHandler;
    private final ILongInputHandler<ChemicalStack> chemicalInputHandler;
    private final IOutputHandler<PlantingRecipe.PlantingStationRecipeOutput> outputHandler;

    private final ItemStackConstantChemicalToObjectCachedRecipe.ChemicalUsageMultiplier chemicalUsage;
    private final LongConsumer chemicalUsedSoFarChanged;
    private long chemicalUsageMultiplier;
    private long chemicalUsedSoFar;

    private ItemStack recipeItem = ItemStack.EMPTY;
    private ChemicalStack recipeChemical = ChemicalStack.EMPTY;

    private PlantingRecipe.PlantingStationRecipeOutput output;

    /**
     * @param recipe           Recipe.
     * @param recheckAllErrors Returns {@code true} if processing should be continued even if an error is hit in order to gather all the errors. It is recommended to not
     *                         do this every tick or if there is no one viewing recipes.
     * @param itemInputHandler     Item input handler.
     * @param chemicalInputHandler Chemical input handler.
     * @param outputHandler        Output handler, handles contains two item outputs.
     */
    public PlantingStationPerTickCachedRecipe(PlantingRecipe recipe, BooleanSupplier recheckAllErrors, IInputHandler<@NotNull ItemStack> itemInputHandler,
                                              ILongInputHandler<@NotNull ChemicalStack> chemicalInputHandler, ItemStackConstantChemicalToObjectCachedRecipe.ChemicalUsageMultiplier chemicalUsage,
                                              LongConsumer chemicalUsedSoFarChanged, IOutputHandler<PlantingRecipe.PlantingStationRecipeOutput> outputHandler,
                                              Predicate<PlantingRecipe.PlantingStationRecipeOutput> outputEmptyCheck) {
        super(recipe, recheckAllErrors);
        this.itemInputHandler = Objects.requireNonNull(itemInputHandler, "Item input handler cannot be null.");
        this.chemicalInputHandler = Objects.requireNonNull(chemicalInputHandler, "Chemical input handler cannot be null.");
        this.chemicalUsage = Objects.requireNonNull(chemicalUsage, "Chemical usage cannot be null.");
        this.chemicalUsedSoFarChanged = Objects.requireNonNull(chemicalUsedSoFarChanged, "Chemical used so far changed handler cannot be null.");
        this.outputHandler = Objects.requireNonNull(outputHandler, "Output handler cannot be null.");
        this.outputEmptyCheck = Objects.requireNonNull(outputEmptyCheck, "Output empty check cannot be null.");
    }

    /**
     * Sets the amount of chemical that have been used so far. This is used to allow {@link CachedRecipe} holders to persist and load recipe progress.
     *
     * @param chemicalUsedSoFar Amount of chemical that has been used so far.
     */
    public void loadSavedUsageSoFar(long chemicalUsedSoFar) {
        if (chemicalUsedSoFar > 0) {
            this.chemicalUsedSoFar = chemicalUsedSoFar;
        }
    }

    @Override
    protected void setupVariableValues() {
        chemicalUsageMultiplier = Math.max(chemicalUsage.getToUse(chemicalUsedSoFar, getOperatingTicks()), 0);
    }

    @Override
    protected void calculateOperationsThisTick(OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (tracker.shouldContinueChecking()) {
            recipeItem = itemInputHandler.getRecipeInput(recipe.getItemInput());
            if (recipeItem.isEmpty()) {
                tracker.mismatchedRecipe();
            } else {
                recipeChemical = chemicalInputHandler.getRecipeInput(recipe.getChemicalInput());
                if (recipeChemical.isEmpty()) {
                    tracker.mismatchedRecipe();
                } else {
                    itemInputHandler.calculateOperationsCanSupport(tracker, recipeItem);
                    if (tracker.shouldContinueChecking()) {
                        chemicalInputHandler.calculateOperationsCanSupport(tracker, recipeChemical);
                        if (tracker.shouldContinueChecking()) {
                            output = recipe.getOutput(recipeItem, recipeChemical);
                            outputHandler.calculateOperationsCanSupport(tracker, output);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isInputValid() {
        ItemStack itemInput = itemInputHandler.getInput();
        if (!itemInput.isEmpty()) {
            ChemicalStack chemicalStack = chemicalInputHandler.getInput();
            if (!chemicalStack.isEmpty() && recipe.test(itemInput, chemicalStack)) {
                ChemicalStack recipeChemical = chemicalInputHandler.getRecipeInput(recipe.getChemicalInput());
                return !recipeChemical.isEmpty() && chemicalStack.getAmount() >= recipeChemical.getAmount();
            }
        }
        return false;
    }

    @Override
    protected void useResources(int operations) {
        super.useResources(operations);
        if (chemicalUsageMultiplier <= 0) {
            //We don't need to use the chemical
            return;
        } else if (recipeChemical == null || recipeChemical.isEmpty()) {
            //Something went wrong, this if should never really be true if we are in useResources
            return;
        }
        //Note: We should have enough because of the getOperationsThisTick call to reduce it based on amounts
        long toUse = operations * chemicalUsageMultiplier;
        chemicalInputHandler.use(recipeChemical, toUse);
        chemicalUsedSoFar += toUse;
        chemicalUsedSoFarChanged.accept(chemicalUsedSoFar);
    }

    @Override
    protected void resetCache() {
        super.resetCache();
        chemicalUsedSoFar = 0;
        chemicalUsedSoFarChanged.accept(chemicalUsedSoFar);
    }

    @Override
    protected void finishProcessing(int operations) {
        //Validate something didn't go horribly wrong
        if (recipeChemical != null && output != null && !recipeItem.isEmpty() && !recipeChemical.isEmpty() && !outputEmptyCheck.test(output)) {
            itemInputHandler.use(recipeItem, operations);
            if (chemicalUsageMultiplier > 0) {
                chemicalInputHandler.use(recipeChemical, operations * chemicalUsageMultiplier);
            }
            outputHandler.handleOutput(output, operations);
        }
    }
}
