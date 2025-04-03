package com.jerry.mekmm.common.registries;

import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.api.recipes.MMRecipeSerializers;
import com.jerry.mekmm.api.recipes.basic.*;
import com.jerry.mekmm.common.recipe.serializer.PlantingRecipeSerializer;
import com.jerry.mekmm.common.recipe.serializer.RecyclerRecipeSerializer;
import mekanism.common.recipe.serializer.MekanismRecipeSerializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MMRecipeSerializersInternal {

    private MMRecipeSerializersInternal() {

    }

    public static final DeferredRegister<RecipeSerializer<?>> MM_RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Mekmm.MOD_ID);

    static {
        MMRecipeSerializers.RECYCLER = MM_RECIPE_SERIALIZERS.register("recycler", () -> new RecyclerRecipeSerializer(BasicRecyclerRecipe::new));
        MMRecipeSerializers.PLANTING = MM_RECIPE_SERIALIZERS.register("planting", () -> new PlantingRecipeSerializer(BasicPlantingRecipe::new));
        MMRecipeSerializers.STAMPING = MM_RECIPE_SERIALIZERS.register("stamper", () -> MekanismRecipeSerializer.itemToItem(BasicStampingRecipe::new));
        MMRecipeSerializers.LATHE = MM_RECIPE_SERIALIZERS.register("lathe", () -> MekanismRecipeSerializer.itemToItem(BasicLatheRecipe::new));
        MMRecipeSerializers.ROLLING_MILL = MM_RECIPE_SERIALIZERS.register("rolling_mill", () -> MekanismRecipeSerializer.itemToItem(BasicRollingMillRecipe::new));
    }
}
