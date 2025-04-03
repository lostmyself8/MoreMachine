package com.jerry.mekmm.client.recipe_viewer.jei;

import com.jerry.mekmm.client.recipe_viewer.type.IMMRecipeViewerRecipeType;
import com.jerry.mekmm.common.recipe.IMMRecipeTypeProvider;
import mekanism.api.recipes.MekanismRecipe;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;

public class MMRecipeRegistryHelper {

    private MMRecipeRegistryHelper() {

    }

    public static <RECIPE extends MekanismRecipe<?>> void register(IRecipeRegistration registry, IMMRecipeViewerRecipeType<RECIPE> recipeType,
                                                                   IMMRecipeTypeProvider<?, RECIPE, ?> type) {
        registry.addRecipes(MoreMachineJEI.holderRecipeType(recipeType), type.getRecipes(null));
    }

    public static <RECIPE> void register(IRecipeRegistration registry, IMMRecipeViewerRecipeType<RECIPE> recipeType, Map<ResourceLocation, RECIPE> recipes) {
        register(registry, recipeType, List.copyOf(recipes.values()));
    }

    public static <RECIPE> void register(IRecipeRegistration registry, IMMRecipeViewerRecipeType<RECIPE> recipeType, List<RECIPE> recipes) {
        registry.addRecipes(MoreMachineJEI.recipeType(recipeType), recipes);
    }
}
