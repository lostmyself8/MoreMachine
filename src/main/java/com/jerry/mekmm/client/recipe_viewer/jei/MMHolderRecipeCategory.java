package com.jerry.mekmm.client.recipe_viewer.jei;

import com.jerry.mekmm.client.recipe_viewer.type.IMMRecipeViewerRecipeType;
import com.mojang.serialization.Codec;
import mezz.jei.api.helpers.ICodecHelper;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IRecipeManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

public abstract class MMHolderRecipeCategory <RECIPE extends Recipe<?>> extends MMBaseRecipeCategory<RecipeHolder<RECIPE>> {

    protected MMHolderRecipeCategory(IGuiHelper helper, IMMRecipeViewerRecipeType<RECIPE> recipeType) {
        super(helper, MoreMachineJEI.holderRecipeType(recipeType), recipeType.getTextComponent(), createIcon(helper, recipeType), recipeType.xOffset(), recipeType.yOffset(), recipeType.width(), recipeType.height());
    }

    @NotNull
    @Override
    public ResourceLocation getRegistryName(RecipeHolder<RECIPE> recipe) {
        return recipe.id();
    }

    @Override
    public @NotNull Codec<RecipeHolder<RECIPE>> getCodec(ICodecHelper codecHelper, @NotNull IRecipeManager recipeManager) {
        return codecHelper.getRecipeHolderCodec();
    }
}
