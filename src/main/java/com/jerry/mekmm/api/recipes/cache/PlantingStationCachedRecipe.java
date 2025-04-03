package com.jerry.mekmm.api.recipes.cache;

import com.jerry.mekmm.api.recipes.PlantingRecipe;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.ILongInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

@NothingNullByDefault
public class PlantingStationCachedRecipe extends CachedRecipe<PlantingRecipe> {

    private final Predicate<PlantingRecipe.PlantingStationRecipeOutput> outputEmptyCheck;

    private final IInputHandler<ItemStack> itemInputHandler;
    private final ILongInputHandler<ChemicalStack> chemicalInputHandler;
    private final IOutputHandler<PlantingRecipe.PlantingStationRecipeOutput> outputHandler;

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
    public PlantingStationCachedRecipe(PlantingRecipe recipe, BooleanSupplier recheckAllErrors, IInputHandler<@NotNull ItemStack> itemInputHandler,
                                       ILongInputHandler<@NotNull ChemicalStack> chemicalInputHandler, IOutputHandler<PlantingRecipe.PlantingStationRecipeOutput> outputHandler,
                                       Predicate<PlantingRecipe.PlantingStationRecipeOutput> outputEmptyCheck) {
        super(recipe, recheckAllErrors);
        this.itemInputHandler = Objects.requireNonNull(itemInputHandler, "Item input handler cannot be null.");
        this.chemicalInputHandler = Objects.requireNonNull(chemicalInputHandler, "Chemical input handler cannot be null.");
        this.outputHandler = Objects.requireNonNull(outputHandler, "Output handler cannot be null.");
        this.outputEmptyCheck = Objects.requireNonNull(outputEmptyCheck, "Output empty check cannot be null.");
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
        ItemStack item = itemInputHandler.getInput();
        if (item.isEmpty()) {
            return false;
        }
        ChemicalStack chemical = chemicalInputHandler.getInput();
        return !chemical.isEmpty() && recipe.test(item, chemical);
    }

    @Override
    protected void finishProcessing(int operations) {
        //Validate something didn't go horribly wrong
        if (recipeChemical != null && output != null && !recipeItem.isEmpty() && !recipeChemical.isEmpty() && !outputEmptyCheck.test(output)) {
            itemInputHandler.use(recipeItem, operations);
            chemicalInputHandler.use(recipeChemical, operations);
            outputHandler.handleOutput(output, operations);
        }
    }
}
