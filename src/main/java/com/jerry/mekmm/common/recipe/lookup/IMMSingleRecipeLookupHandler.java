package com.jerry.mekmm.common.recipe.lookup;

import com.jerry.mekmm.common.recipe.lookup.cache.MMInputRecipeCache;
import com.jerry.mekmm.common.recipe.lookup.cache.MMSingleInputRecipeCache;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.common.recipe.lookup.IRecipeLookupHandler;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

/**
 * Helper expansion of {@link IRecipeLookupHandler} for easily implementing contains and find recipe lookups for recipes that take a single input using the input cache.
 */
public interface IMMSingleRecipeLookupHandler<INPUT, RECIPE extends MekanismRecipe<?> & Predicate<INPUT>, INPUT_CACHE extends MMSingleInputRecipeCache<INPUT, ?, RECIPE, ?>>
      extends IMMRecipeLookupHandler.IMMRecipeTypedLookupHandler<RECIPE, INPUT_CACHE> {

    /**
     * Checks if there is a matching recipe of type {@link #getMMRecipeType()} that has the given input.
     *
     * @param input Recipe input.
     *
     * @return {@code true} if there is a match, {@code false} if there isn't.
     */
    default boolean containsRecipe(INPUT input) {
        return getMMRecipeType().getInputCache().containsInput(getLevel(), input);
    }

    /**
     * Finds the first recipe for the type of recipe we handle ({@link #getMMRecipeType()}) by looking up the given input against the recipe type's input cache.
     *
     * @param input Recipe input.
     *
     * @return Recipe matching the given input, or {@code null} if no recipe matches.
     */
    @Nullable
    default RECIPE findFirstRecipe(INPUT input) {
        return getMMRecipeType().getInputCache().findFirstRecipe(getLevel(), input);
    }

    /**
     * Finds the first recipe for the type of recipe we handle ({@link #getMMRecipeType()}) by looking up the given input against the recipe type's input cache.
     *
     * @param inputHandler Input handler to grab the recipe input from.
     *
     * @return Recipe matching the given input, or {@code null} if no recipe matches.
     */
    @Nullable
    default RECIPE findFirstRecipe(IInputHandler<INPUT> inputHandler) {
        return findFirstRecipe(inputHandler.getInput());
    }

    /**
     * Helper interface to make the generics that we have to pass to {@link IMMSingleRecipeLookupHandler} not as messy.
     */
    interface ItemRecipeLookupHandler<RECIPE extends MekanismRecipe<?> & Predicate<ItemStack>> extends IMMSingleRecipeLookupHandler<ItemStack, RECIPE, MMInputRecipeCache.SingleItem<RECIPE>> {
    }

    /**
     * Helper interface to make the generics that we have to pass to {@link IMMSingleRecipeLookupHandler} not as messy.
     */
    interface FluidRecipeLookupHandler<RECIPE extends MekanismRecipe<?> & Predicate<FluidStack>> extends IMMSingleRecipeLookupHandler<FluidStack, RECIPE, MMInputRecipeCache.SingleFluid<RECIPE>> {
    }

    /**
     * Helper interface to make the generics that we have to pass to {@link IMMSingleRecipeLookupHandler} not as messy.
     */
    interface ChemicalRecipeLookupHandler<RECIPE extends MekanismRecipe<?> & Predicate<ChemicalStack>> extends IMMSingleRecipeLookupHandler<ChemicalStack, RECIPE, MMInputRecipeCache.SingleChemical<RECIPE>> {

        /**
         * Helper wrapper to convert a chemical to a chemical stack and pass it to {@link #containsRecipe(Object)} to make validity predicates easier and cleaner.
         */
        default boolean containsRecipe(Chemical input) {
            return containsRecipe(input.getStack(1));
        }
    }
}