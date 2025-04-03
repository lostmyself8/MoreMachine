package com.jerry.mekmm.api.recipes.outputs;

import com.jerry.mekmm.api.recipes.PlantingRecipe;
import com.jerry.mekmm.api.recipes.RecyclerRecipe;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.recipes.SawmillRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

@NothingNullByDefault
public class MMOutputHelper {

    private MMOutputHelper() {
    }

    public static IOutputHandler<RecyclerRecipe.ChanceOutput> getOutputHandler(IInventorySlot chanceSlot, CachedRecipe.OperationTracker.RecipeError chanceSlotNotEnoughSpaceError) {
        Objects.requireNonNull(chanceSlot, "Chance slot cannot be null.");
        Objects.requireNonNull(chanceSlotNotEnoughSpaceError, "Chance slot not enough space error cannot be null.");

        return new IOutputHandler<>() {

            @Override
            public void handleOutput(RecyclerRecipe.ChanceOutput toOutput, int operations) {
                ItemStack chanceOutput = toOutput.getChanceOutput();
                for (int i = 0; i < operations; i++) {
                    MMOutputHelper.handleOutput(chanceSlot, chanceOutput, operations);
                    if (i < operations - 1) {
                        chanceOutput = toOutput.nextChanceOutput();
                    }
                }
            }

            @Override
            public void calculateOperationsCanSupport(CachedRecipe.OperationTracker tracker, RecyclerRecipe.ChanceOutput toOutput) {
                MMOutputHelper.calculateOperationsCanSupport(tracker, chanceSlotNotEnoughSpaceError, chanceSlot, toOutput.getMaxChanceOutput());
            }
        };
    }

    /**
     * Wraps two inventory slots, a "main" slot, and a "secondary" slot into an {@link IOutputHandler} for handling {@link SawmillRecipe.ChanceOutput}s.
     *
     * @param mainSlot                         Main slot to wrap.
     * @param secondarySlot                    Secondary slot to wrap.
     * @param mainSlotNotEnoughSpaceError      The error to apply if the main output causes the recipe to not be able to perform any operations.
     * @param secondarySlotNotEnoughSpaceError The error to apply if the secondary output causes the recipe to not be able to perform any operations.
     */
    public static IOutputHandler<PlantingRecipe.PlantingStationRecipeOutput> getOutputHandler(IInventorySlot mainSlot, CachedRecipe.OperationTracker.RecipeError mainSlotNotEnoughSpaceError,
                                                                                              IInventorySlot secondarySlot, CachedRecipe.OperationTracker.RecipeError secondarySlotNotEnoughSpaceError) {
        Objects.requireNonNull(mainSlot, "Main slot cannot be null.");
        Objects.requireNonNull(secondarySlot, "Secondary/Extra slot cannot be null.");
        Objects.requireNonNull(mainSlotNotEnoughSpaceError, "Main slot not enough space error cannot be null.");
        Objects.requireNonNull(secondarySlotNotEnoughSpaceError, "Secondary/Extra slot not enough space error cannot be null.");
        return new IOutputHandler<>() {
            @Override
            public void handleOutput(PlantingRecipe.PlantingStationRecipeOutput toOutput, int operations) {
                MMOutputHelper.handleOutput(mainSlot, toOutput.first(), operations);
                MMOutputHelper.handleOutput(secondarySlot, toOutput.second(), operations);
            }

            @Override
            public void calculateOperationsCanSupport(CachedRecipe.OperationTracker tracker, PlantingRecipe.PlantingStationRecipeOutput toOutput) {
                MMOutputHelper.calculateOperationsCanSupport(tracker, mainSlotNotEnoughSpaceError, mainSlot, toOutput.first());
                if (tracker.shouldContinueChecking()) {
                    MMOutputHelper.calculateOperationsCanSupport(tracker, secondarySlotNotEnoughSpaceError, secondarySlot, toOutput.second());
                }
            }
        };
    }

    private static void handleOutput(IInventorySlot inventorySlot, ItemStack toOutput, int operations) {
        if (operations == 0 || toOutput.isEmpty()) {
            return;
        }
        ItemStack output = toOutput.copy();
        if (operations > 1) {
            //If we are doing more than one operation we need to make a copy of our stack and change the amount
            // that we are using the fill the tank with
            output.setCount(output.getCount() * operations);
        }
        inventorySlot.insertItem(output, Action.EXECUTE, AutomationType.INTERNAL);
    }

    private static void calculateOperationsCanSupport(CachedRecipe.OperationTracker tracker, CachedRecipe.OperationTracker.RecipeError notEnoughSpace, IInventorySlot slot, ItemStack toOutput) {
        //If our output is empty, we have nothing to add, so we treat it as being able to fit all
        if (!toOutput.isEmpty()) {
            //Make a copy of the stack we are outputting with its maximum size
            ItemStack output = toOutput.copyWithCount(toOutput.getMaxStackSize());
            ItemStack remainder = slot.insertItem(output, Action.SIMULATE, AutomationType.INTERNAL);
            int amountUsed = output.getCount() - remainder.getCount();
            //Divide the amount we can actually use by the amount one output operation is equal to, capping it at the max we were told about
            int operations = amountUsed / toOutput.getCount();
            tracker.updateOperations(operations);
            if (operations == 0) {
                if (amountUsed == 0 && slot.getLimit(slot.getStack()) - slot.getCount() > 0) {
                    tracker.addError(CachedRecipe.OperationTracker.RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);
                } else {
                    tracker.addError(notEnoughSpace);
                }
            }
        }
    }
}
