package com.jerry.mekmm.client.recipe_viewer.jei.machine;

import com.jerry.mekmm.api.recipes.PlantingRecipe;
import com.jerry.mekmm.client.recipe_viewer.jei.MMHolderRecipeCategory;
import com.jerry.mekmm.client.recipe_viewer.type.IMMRecipeViewerRecipeType;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.functions.ConstantPredicates;
import mekanism.client.gui.element.bar.GuiBar;
import mekanism.client.gui.element.bar.GuiEmptyBar;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.recipe_viewer.RecipeViewerUtils;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.tile.prefab.TileEntityAdvancedElectricMachine;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.List;

@NothingNullByDefault
public class PlantingRecipeCategory extends MMHolderRecipeCategory<PlantingRecipe> {

    private final GuiBar<?> chemicalInput;
    private final GuiSlot input;
    private final GuiSlot extra;
    private final GuiSlot output;

    public PlantingRecipeCategory(IGuiHelper helper, IMMRecipeViewerRecipeType<PlantingRecipe> recipeType) {
        super(helper, recipeType);
        input = addSlot(SlotType.INPUT, 56, 17);
        extra = addSlot(SlotType.EXTRA, 56, 53);
        addSlot(SlotType.POWER, 31, 35).with(SlotOverlay.POWER);
        output = addSlot(SlotType.OUTPUT_WIDE, 112, 31);
        addElement(new GuiVerticalPowerBar(this, RecipeViewerUtils.FULL_BAR, 164, 15));
        chemicalInput = addElement(new GuiEmptyBar(this, 60, 36, 6, 12));
        addSimpleProgress(ProgressType.BAR, 78, 38);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<PlantingRecipe> recipeHolder, IFocusGroup focusGroup) {
        PlantingRecipe recipe = recipeHolder.value();
        initItem(builder, RecipeIngredientRole.INPUT, input, recipe.getItemInput().getRepresentations());
        List<ChemicalStack> scaledChemicals = recipe.getChemicalInput().getRepresentations();
        if (recipe.perTickUsage()) {
            scaledChemicals = scaledChemicals.stream()
                    .map(chemical -> chemical.copyWithAmount(chemical.getAmount() * TileEntityAdvancedElectricMachine.BASE_TICKS_REQUIRED))
                    .toList();
        }
        initChemical(builder, RecipeIngredientRole.INPUT, chemicalInput, scaledChemicals);
        List<ItemStack> firstOutputs = new ArrayList<>();
        List<ItemStack> secondOutputs = new ArrayList<>();
        for (PlantingRecipe.PlantingStationRecipeOutput output : recipe.getOutputDefinition()) {
            firstOutputs.add(output.first());
            secondOutputs.add(output.second());
        }
//        if (!firstOutputs.stream().allMatch(ConstantPredicates.ITEM_EMPTY)) {
//            initItem(builder, RecipeIngredientRole.OUTPUT, output.getX() + 4, output.getY() + 4, firstOutputs);
//        }
        initItem(builder, RecipeIngredientRole.OUTPUT, output.getX() + 4, output.getY() + 4, firstOutputs);
        if (!secondOutputs.stream().allMatch(ConstantPredicates.ITEM_EMPTY)) {
            initItem(builder, RecipeIngredientRole.OUTPUT, output.getX() + 20, output.getY() + 4, secondOutputs);
        }
        initItem(builder, RecipeIngredientRole.CATALYST, extra, RecipeViewerUtils.getStacksFor(recipe.getChemicalInput(), true));
    }
}
