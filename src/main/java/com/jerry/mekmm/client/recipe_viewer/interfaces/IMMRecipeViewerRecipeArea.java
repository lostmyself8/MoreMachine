package com.jerry.mekmm.client.recipe_viewer.interfaces;

import com.jerry.mekmm.client.recipe_viewer.type.IMMRecipeViewerRecipeType;
import com.jerry.mekmm.common.recipe.lookup.IMMRecipeLookupHandler;
import mekanism.client.gui.element.GuiElement;
import net.minecraft.client.gui.components.events.GuiEventListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IMMRecipeViewerRecipeArea<ELEMENT extends GuiElement> extends GuiEventListener {

    /**
     * @return null if not an active recipe area, otherwise the category
     */
    @Nullable
    IMMRecipeViewerRecipeType<?>[] getRecipeCategories();

    default boolean isRecipeViewerAreaActive() {
        return true;
    }

    ELEMENT recipeViewerCategories(@NotNull IMMRecipeViewerRecipeType<?>... recipeCategories);

    default ELEMENT recipeViewerCategory(IMMRecipeLookupHandler<?> recipeLookup) {
        IMMRecipeViewerRecipeType<?> recipeType = recipeLookup.recipeViewerType();
        if (recipeType != null) {
            return recipeViewerCategories(recipeType);
        }
        return (ELEMENT) this;
    }

    default boolean isMouseOverRecipeViewerArea(double mouseX, double mouseY) {
        return isMouseOver(mouseX, mouseY);
    }
}