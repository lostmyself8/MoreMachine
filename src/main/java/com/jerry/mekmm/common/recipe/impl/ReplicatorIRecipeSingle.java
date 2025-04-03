package com.jerry.mekmm.common.recipe.impl;

import com.jerry.mekmm.api.recipes.basic.MMBasicItemStackChemicalToItemStackRecipe;
import com.jerry.mekmm.common.registries.MMBlocks;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

@NothingNullByDefault
public class ReplicatorIRecipeSingle extends MMBasicItemStackChemicalToItemStackRecipe {

    /**
     * @param chemicalInput Chemical input.
     * @param output        Output.
     */
    public ReplicatorIRecipeSingle(ItemStackIngredient itemInput, ChemicalStackIngredient chemicalInput, ItemStack output) {
        super(itemInput, chemicalInput, output);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return null;
    }

    @Override
    public String getGroup() {
        return "duplicator";
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(MMBlocks.REPLICATOR);
    }
}
