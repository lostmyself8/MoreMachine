package com.jerry.mekmm.common.registration.impl;

import com.jerry.mekmm.common.recipe.IMMRecipeTypeProvider;
import com.jerry.mekmm.common.recipe.MMRecipeType;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import mekanism.common.registration.MekanismDeferredHolder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;

public class MMRecipeTypeRegistryObject<VANILLA_INPUT extends RecipeInput, RECIPE extends MekanismRecipe<VANILLA_INPUT>, INPUT_CACHE extends IInputRecipeCache> extends
        MekanismDeferredHolder<RecipeType<?>, MMRecipeType<VANILLA_INPUT, RECIPE, INPUT_CACHE>> implements IMMRecipeTypeProvider<VANILLA_INPUT, RECIPE, INPUT_CACHE> {

    public MMRecipeTypeRegistryObject(ResourceKey<RecipeType<?>> key) {
        super(key);
    }

    @Override
    public MMRecipeType<VANILLA_INPUT, RECIPE, INPUT_CACHE> getMMRecipeType() {
        return value();
    }
}
