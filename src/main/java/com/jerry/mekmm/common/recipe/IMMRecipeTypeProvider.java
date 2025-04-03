package com.jerry.mekmm.common.recipe;

import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface IMMRecipeTypeProvider<VANILLA_INPUT extends RecipeInput, RECIPE extends MekanismRecipe<VANILLA_INPUT>, INPUT_CACHE extends IInputRecipeCache> {

    /**
     * Gets the registry name of the element represented by this provider.
     *
     * @return Registry name.
     */
    default ResourceLocation getRegistryName() {
        return getMMRecipeType().getRegistryName();
    }

    MMRecipeType<VANILLA_INPUT, RECIPE, INPUT_CACHE> getMMRecipeType();

    default INPUT_CACHE getInputCache() {
        return getMMRecipeType().getInputCache();
    }

    @NotNull
    default List<RecipeHolder<RECIPE>> getRecipes(@Nullable Level world) {
        return getMMRecipeType().getRecipes(world);
    }

    @NotNull
    default List<RecipeHolder<RECIPE>> getRecipes(RecipeManager recipeManager, @Nullable Level world) {
        return getMMRecipeType().getRecipes(recipeManager, world);
    }

    default Stream<RecipeHolder<RECIPE>> stream(@Nullable Level world) {
        return getRecipes(world).stream();
    }

    /**
     * Finds the first recipe that matches the given criteria, or null if no matching recipe is found. Prefer using the find recipe methods in {@link #getInputCache()}.
     */
    @Nullable
    default RECIPE findFirst(@Nullable Level world, Predicate<RECIPE> matchCriteria) {
        for (RecipeHolder<RECIPE> recipeRecipeHolder : getRecipes(world)) {
            RECIPE value = recipeRecipeHolder.value();
            if (matchCriteria.test(value)) {
                return value;
            }
        }
        return null;
    }

    /**
     * Checks if this recipe type contains a recipe that matches the given criteria. Prefer using the contains recipe methods in {@link #getInputCache()}.
     */
    default boolean contains(@Nullable Level world, Predicate<RECIPE> matchCriteria) {
        for (RecipeHolder<RECIPE> holder : getRecipes(world)) {
            if (matchCriteria.test(holder.value())) {
                return true;
            }
        }
        return false;
    }
}
