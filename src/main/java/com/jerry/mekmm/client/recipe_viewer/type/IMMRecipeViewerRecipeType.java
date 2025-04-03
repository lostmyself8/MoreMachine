package com.jerry.mekmm.client.recipe_viewer.type;

import mekanism.api.text.IHasTextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IMMRecipeViewerRecipeType<RECIPE> extends IHasTextComponent {

    ResourceLocation id();

    Class<? extends RECIPE> recipeClass();

    boolean requiresHolder();

    ItemStack iconStack();

    @Nullable
    ResourceLocation icon();

    /**
     * Offset to return the recipe viewer screen to a 0, 0 based coordinates compared to the normal mekanism gui. This number is almost always negative
     */
    int xOffset();

    /**
     * Offset to return the recipe viewer screen to a 0, 0 based coordinates compared to the normal mekanism gui. This number is almost always negative
     */
    int yOffset();

    int width();

    int height();

    List<ItemLike> workstations();
}