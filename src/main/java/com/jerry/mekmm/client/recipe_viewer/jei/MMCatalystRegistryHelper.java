package com.jerry.mekmm.client.recipe_viewer.jei;

import com.jerry.mekmm.client.recipe_viewer.type.IMMRecipeViewerRecipeType;
import com.jerry.mekmm.common.block.attribute.MMAttributeFactoryType;
import com.jerry.mekmm.common.registries.MMBlocks;
import mekanism.client.recipe_viewer.jei.MekanismJEI;
import mekanism.client.recipe_viewer.type.IRecipeViewerRecipeType;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.tier.FactoryTier;
import mekanism.common.util.EnumUtils;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.List;

public class MMCatalystRegistryHelper {

    private MMCatalystRegistryHelper() {

    }

    public static void register(IRecipeCatalystRegistration registry, IRecipeViewerRecipeType<?>... categories) {
        for (IRecipeViewerRecipeType<?> category : categories) {
            register1(registry, MekanismJEI.genericRecipeType(category), category.workstations());
        }
    }

    public static void register1(IRecipeCatalystRegistration registry, RecipeType<?> recipeType, List<ItemLike> workstations) {
        for (ItemLike workstation : workstations) {
            Item item = workstation.asItem();
            if (item instanceof BlockItem blockItem) {
                MMAttributeFactoryType factoryType = Attribute.get(blockItem.getBlock(), MMAttributeFactoryType.class);
                if (factoryType != null) {
                    for (FactoryTier tier : EnumUtils.FACTORY_TIERS) {
                        registry.addRecipeCatalyst(MMBlocks.getFactory(tier, factoryType.getMMFactoryType()), recipeType);
                    }
                }
            }
        }
    }

    public static void register(IRecipeCatalystRegistration registry, IMMRecipeViewerRecipeType<?>... categories) {
        for (IMMRecipeViewerRecipeType<?> category : categories) {
            register(registry, MoreMachineJEI.genericRecipeType(category), category.workstations());
        }
    }

    public static void register(IRecipeCatalystRegistration registry, RecipeType<?> recipeType, List<ItemLike> workstations) {
        for (ItemLike workstation : workstations) {
            Item item = workstation.asItem();
            registry.addRecipeCatalyst(item, recipeType);
            if (item instanceof BlockItem blockItem) {
                MMAttributeFactoryType factoryType = Attribute.get(blockItem.getBlock(), MMAttributeFactoryType.class);
                if (factoryType != null) {
                    for (FactoryTier tier : EnumUtils.FACTORY_TIERS) {
                        registry.addRecipeCatalyst(MMBlocks.getFactory(tier, factoryType.getMMFactoryType()), recipeType);
                    }
                }
            }
        }
    }
}
