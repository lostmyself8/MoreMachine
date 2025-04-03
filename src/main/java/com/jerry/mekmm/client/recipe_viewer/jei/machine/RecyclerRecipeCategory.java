package com.jerry.mekmm.client.recipe_viewer.jei.machine;

import com.jerry.mekmm.api.recipes.RecyclerRecipe;
import com.jerry.mekmm.client.recipe_viewer.jei.MMHolderRecipeCategory;
import com.jerry.mekmm.client.recipe_viewer.type.IMMRecipeViewerRecipeType;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.client.gui.element.GuiUpArrow;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.recipe_viewer.RecipeViewerUtils;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.util.text.TextUtils;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.crafting.RecipeHolder;

@NothingNullByDefault
public class RecyclerRecipeCategory extends MMHolderRecipeCategory<RecyclerRecipe> {

    private final GuiSlot input;
    private final GuiSlot output;

    public RecyclerRecipeCategory(IGuiHelper helper, IMMRecipeViewerRecipeType<RecyclerRecipe> recipeType) {
        super(helper, recipeType);
        addElement(new GuiUpArrow(this, 68, 38));
        input = addSlot(SlotType.INPUT, 64, 17);
        output = addSlot(SlotType.OUTPUT, 116, 35);
        addSlot(SlotType.POWER, 64, 53).with(SlotOverlay.POWER);
        addElement(new GuiVerticalPowerBar(this, RecipeViewerUtils.FULL_BAR, 164, 16));
        addSimpleProgress(ProgressType.BAR, 86, 38);
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<RecyclerRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, IFocusGroup focuses) {
        super.createRecipeExtras(builder, recipeHolder, recipeSlotsView, focuses);
        double secondaryChance = recipeHolder.value().getOutputChance();
        if (secondaryChance > 0) {
            builder.addText(TextUtils.getPercent(secondaryChance), getGuiLeft() + output.getRelativeX() + 1, getGuiTop() + output.getRelativeBottom() + 1, output.getWidth(), font().lineHeight)
                    //Perform the same translations as super does
                    .alignHorizontalRight()
                    .setColor(titleTextColor());
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<RecyclerRecipe> recipeHolder, IFocusGroup focuses) {
        RecyclerRecipe recipe = recipeHolder.value();
        initItem(builder, RecipeIngredientRole.INPUT, input, recipe.getInput().getRepresentations());
        initItem(builder, RecipeIngredientRole.OUTPUT, output, recipe.getChanceOutputDefinition());
    }
}
