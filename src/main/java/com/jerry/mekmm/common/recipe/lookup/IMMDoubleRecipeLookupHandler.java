package com.jerry.mekmm.common.recipe.lookup;

import com.jerry.mekmm.common.recipe.lookup.cache.MMDoubleInputRecipeCache;
import com.jerry.mekmm.common.recipe.lookup.cache.MMInputRecipeCache;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.common.recipe.lookup.IRecipeLookupHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiPredicate;

/**
 * Helper expansion of {@link IRecipeLookupHandler} for easily implementing contains and find recipe lookups for recipes that takes two inputs.
 */
public interface IMMDoubleRecipeLookupHandler<INPUT_A, INPUT_B, RECIPE extends MekanismRecipe<?> & BiPredicate<INPUT_A, INPUT_B>,
      INPUT_CACHE extends MMDoubleInputRecipeCache<INPUT_A, ?, INPUT_B, ?, RECIPE, ?, ?>> extends IMMRecipeLookupHandler.IMMRecipeTypedLookupHandler<RECIPE, INPUT_CACHE> {

    /**
     * Checks if there is a matching recipe of type {@link #getMMRecipeType()} that has the given inputs.
     *
     * @param inputA Recipe input a.
     * @param inputB Recipe input b.
     *
     * @return {@code true} if there is a match, {@code false} if there isn't.
     *
     * @apiNote See {@link MMDoubleInputRecipeCache#containsInputAB(Level, Object, Object)} and {@link MMDoubleInputRecipeCache#containsInputBA(Level, Object, Object)} for
     * more details about when this method should be called versus when {@link #containsRecipeBA(Object, Object)} should be called.
     */
    default boolean containsRecipeAB(INPUT_A inputA, INPUT_B inputB) {
        return getMMRecipeType().getInputCache().containsInputAB(getLevel(), inputA, inputB);
    }

    /**
     * Checks if there is a matching recipe of type {@link #getMMRecipeType()} that has the given inputs.
     *
     * @param inputA Recipe input a.
     * @param inputB Recipe input b.
     *
     * @return {@code true} if there is a match, {@code false} if there isn't.
     *
     * @apiNote See {@link MMDoubleInputRecipeCache#containsInputAB(Level, Object, Object)} and {@link MMDoubleInputRecipeCache#containsInputBA(Level, Object, Object)} for
     * more details about when this method should be called versus when {@link #containsRecipeAB(Object, Object)} should be called.
     */
    default boolean containsRecipeBA(INPUT_A inputA, INPUT_B inputB) {
        return getMMRecipeType().getInputCache().containsInputBA(getLevel(), inputA, inputB);
    }

    /**
     * Checks if there is a matching recipe of type {@link #getMMRecipeType()} that has the given input.
     *
     * @param input Recipe input.
     *
     * @return {@code true} if there is a match, {@code false} if there isn't.
     */
    default boolean containsRecipeA(INPUT_A input) {
        return getMMRecipeType().getInputCache().containsInputA(getLevel(), input);
    }

    /**
     * Checks if there is a matching recipe of type {@link #getMMRecipeType()} that has the given input.
     *
     * @param input Recipe input.
     *
     * @return {@code true} if there is a match, {@code false} if there isn't.
     */
    default boolean containsRecipeB(INPUT_B input) {
        return getMMRecipeType().getInputCache().containsInputB(getLevel(), input);
    }

    /**
     * Finds the first recipe for the type of recipe we handle ({@link #getMMRecipeType()}) by looking up the given inputs against the recipe type's input cache.
     *
     * @param inputA Recipe input a.
     * @param inputB Recipe input b.
     *
     * @return Recipe matching the given inputs, or {@code null} if no recipe matches.
     */
    @Nullable
    default RECIPE findFirstRecipe(INPUT_A inputA, INPUT_B inputB) {
        return getMMRecipeType().getInputCache().findFirstRecipe(getLevel(), inputA, inputB);
    }

    /**
     * Finds the first recipe for the type of recipe we handle ({@link #getMMRecipeType()}) by looking up the given inputs against the recipe type's input cache.
     *
     * @param inputAHandler Input handler to grab the first recipe input from.
     * @param inputBHandler Input handler to grab the second recipe input from.
     *
     * @return Recipe matching the given inputs, or {@code null} if no recipe matches.
     */
    @Nullable
    default RECIPE findFirstRecipe(IInputHandler<INPUT_A> inputAHandler, IInputHandler<INPUT_B> inputBHandler) {
        return findFirstRecipe(inputAHandler.getInput(), inputBHandler.getInput());
    }

    /**
     * Helper interface to make the generics that we have to pass to {@link IMMDoubleRecipeLookupHandler} not as messy.
     */
    interface DoubleItemRecipeLookupHandler<RECIPE extends MekanismRecipe<?> & BiPredicate<ItemStack, ItemStack>> extends
            IMMDoubleRecipeLookupHandler<ItemStack, ItemStack, RECIPE, MMInputRecipeCache.DoubleItem<RECIPE>> {
    }

    /**
     * Helper interface to make the generics that we have to pass to {@link IMMDoubleRecipeLookupHandler} not as messy, and reduce the duplicate code in the other chemical
     * based helper interfaces.
     */
    interface ObjectChemicalRecipeLookupHandler<INPUT, RECIPE extends MekanismRecipe<?> & BiPredicate<INPUT, ChemicalStack>,
          INPUT_CACHE extends MMDoubleInputRecipeCache<INPUT, ?, ChemicalStack, ?, RECIPE, ?, ?>> extends IMMDoubleRecipeLookupHandler<INPUT, ChemicalStack, RECIPE, INPUT_CACHE> {

        /**
         * Helper wrapper to convert a chemical to a chemical stack and pass it to {@link #containsRecipeBA(Object, Object)} to make validity predicates easier and
         * cleaner.
         */
        default boolean containsRecipeBA(INPUT inputA, Chemical inputB) {
            return containsRecipeBA(inputA, inputB.getStack(1));
        }

        /**
         * Helper wrapper to convert a chemical to a chemical stack and pass it to {@link #containsRecipeB(Object)} to make validity predicates easier and cleaner.
         */
        default boolean containsRecipeB(Chemical input) {
            return containsRecipeB(input.getStack(1));
        }
    }

    /**
     * Helper interface to make the generics that we have to pass to {@link IMMDoubleRecipeLookupHandler} not as messy.
     */
    interface ItemChemicalRecipeLookupHandler<RECIPE extends MekanismRecipe<?> & BiPredicate<ItemStack, ChemicalStack>> extends
          ObjectChemicalRecipeLookupHandler<ItemStack, RECIPE, MMInputRecipeCache.ItemChemical<RECIPE>> {
    }

    /**
     * Helper interface to make the generics that we have to pass to {@link IMMDoubleRecipeLookupHandler} not as messy.
     */
    interface FluidChemicalRecipeLookupHandler<RECIPE extends MekanismRecipe<?> & BiPredicate<FluidStack, ChemicalStack>> extends
          ObjectChemicalRecipeLookupHandler<FluidStack, RECIPE, MMInputRecipeCache.FluidChemical<RECIPE>> {
    }
}