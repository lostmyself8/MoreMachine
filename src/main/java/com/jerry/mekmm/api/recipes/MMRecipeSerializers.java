package com.jerry.mekmm.api.recipes;

import com.jerry.mekmm.api.recipes.basic.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;

public class MMRecipeSerializers {

    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BasicRecyclerRecipe>> RECYCLER;
    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BasicPlantingRecipe>> PLANTING;
    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BasicStampingRecipe>> STAMPING;
    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BasicLatheRecipe>> LATHE;
    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BasicRollingMillRecipe>> ROLLING_MILL;
}
