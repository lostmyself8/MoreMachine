package com.jerry.mekmm.api.recipes;

import com.jerry.mekmm.Mekmm;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class MMRecipeTypes {

    public static final ResourceLocation NAME_RECYCLER = ResourceLocation.fromNamespaceAndPath(Mekmm.MOD_ID, "recycler");
    public static final ResourceLocation NAME_PLANTING = ResourceLocation.fromNamespaceAndPath(Mekmm.MOD_ID, "planting");
    public static final ResourceLocation NAME_STAMPING = ResourceLocation.fromNamespaceAndPath(Mekmm.MOD_ID, "stamping");
    public static final ResourceLocation NAME_LATHE = ResourceLocation.fromNamespaceAndPath(Mekmm.MOD_ID, "lathe");
    public static final ResourceLocation NAME_ROLLING_MILL = ResourceLocation.fromNamespaceAndPath(Mekmm.MOD_ID, "rolling_mill");

    public static final DeferredHolder<RecipeType<?>, RecipeType<RecyclerRecipe>> TYPE_RECYCLER = DeferredHolder.create(Registries.RECIPE_TYPE, NAME_RECYCLER);
    public static final DeferredHolder<RecipeType<?>, RecipeType<PlantingRecipe>> TYPE_PLANTING = DeferredHolder.create(Registries.RECIPE_TYPE, NAME_PLANTING);
    public static final DeferredHolder<RecipeType<?>, RecipeType<ItemStackToItemStackRecipe>> TYPE_STAMPING = DeferredHolder.create(Registries.RECIPE_TYPE, NAME_STAMPING);
    public static final DeferredHolder<RecipeType<?>, RecipeType<ItemStackToItemStackRecipe>> TYPE_LATHE = DeferredHolder.create(Registries.RECIPE_TYPE, NAME_LATHE);
    public static final DeferredHolder<RecipeType<?>, RecipeType<ItemStackToItemStackRecipe>> TYPE_ROLLING_MILL = DeferredHolder.create(Registries.RECIPE_TYPE, NAME_ROLLING_MILL);
}
