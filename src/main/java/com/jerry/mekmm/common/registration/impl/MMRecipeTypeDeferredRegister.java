package com.jerry.mekmm.common.registration.impl;

import com.jerry.mekmm.common.recipe.MMRecipeType;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import mekanism.common.registration.MekanismDeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Function;

public class MMRecipeTypeDeferredRegister extends MekanismDeferredRegister<RecipeType<?>> {

    public MMRecipeTypeDeferredRegister(String modid) {
        super(Registries.RECIPE_TYPE, modid, MMRecipeTypeRegistryObject::new);
    }

    public <VANILLA_INPUT extends RecipeInput, RECIPE extends MekanismRecipe<VANILLA_INPUT>, INPUT_CACHE extends IInputRecipeCache>
    MMRecipeTypeRegistryObject<VANILLA_INPUT, RECIPE, INPUT_CACHE> registerMek(String name, Function<ResourceLocation, ? extends MMRecipeType<VANILLA_INPUT, RECIPE, INPUT_CACHE>> func) {
        return (MMRecipeTypeRegistryObject<VANILLA_INPUT, RECIPE, INPUT_CACHE>) super.register(name, func);
    }
}
