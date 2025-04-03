package com.jerry.mekmm.client.recipe_viewer;

import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.api.recipes.basic.MMBasicItemStackChemicalToItemStackRecipe;
import com.jerry.mekmm.common.registries.MMChemicals;
import com.jerry.mekmm.common.tile.machine.TileEntityReplicator;
import mekanism.client.recipe_viewer.RecipeViewerUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public class MMRecipeViewerUtils {

    private MMRecipeViewerUtils() {

    }

    public static Map<ResourceLocation, MMBasicItemStackChemicalToItemStackRecipe> getReplicatorRecipes() {
        Map<ResourceLocation, MMBasicItemStackChemicalToItemStackRecipe> replicator = new HashMap<>();
        //TODO: Do we want to loop creative tabs or something instead?
        // In theory recipe loaders should init the creative tabs before we are called so we wouldn't need to call
        // CreativeModeTab#buildContents, and in theory we only need to care about things in search so could use:
        // CreativeModeTabs.searchTab().getDisplayItems(). The bigger issue is how to come up with unique synthetic
        // names for the recipes as EMI requires they be unique. (Maybe index them?)
        for (Map.Entry<ResourceKey<Item>, Item> entry : BuiltInRegistries.ITEM.entrySet()) {
            MMBasicItemStackChemicalToItemStackRecipe recipe = TileEntityReplicator.getRecipe(entry.getValue().getDefaultInstance(), MMChemicals.UU_MATTER.asStack(1));
            if (recipe != null) {
                replicator.put(RecipeViewerUtils.synthetic(entry.getKey().location(), "replicator", Mekmm.MOD_ID), recipe);
            }
        }
        return replicator;
    }
}
