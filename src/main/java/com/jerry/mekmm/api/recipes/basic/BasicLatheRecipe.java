package com.jerry.mekmm.api.recipes.basic;

import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.api.recipes.MMRecipeSerializers;
import com.jerry.mekmm.api.recipes.MMRecipeTypes;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.basic.BasicItemStackToItemStackRecipe;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;

@NothingNullByDefault
public class BasicLatheRecipe extends BasicItemStackToItemStackRecipe {

    private static final Holder<Item> CNC_LATHE = DeferredHolder.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Mekmm.MOD_ID, "cnc_lathe"));

    /**
     * @param input      Input.
     * @param output     Output.
     */
    public BasicLatheRecipe(ItemStackIngredient input, ItemStack output) {
        super(input, output, MMRecipeTypes.TYPE_LATHE.value());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MMRecipeSerializers.LATHE.value();
    }

    @Override
    public String getGroup() {
        return "cnc_lathe";
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(CNC_LATHE);
    }
}
