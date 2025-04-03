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
public class BasicStampingRecipe extends BasicItemStackToItemStackRecipe {

    private static final Holder<Item> CNC_STAMPER = DeferredHolder.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Mekmm.MOD_ID, "cnc_stamper"));

    /**
     * @param input      Input.
     * @param output     Output.
     */
    public BasicStampingRecipe(ItemStackIngredient input, ItemStack output) {
        super(input, output, MMRecipeTypes.TYPE_STAMPING.value());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MMRecipeSerializers.STAMPING.value();
    }

    @Override
    public String getGroup() {
        return "cnc_stamper";
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(CNC_STAMPER);
    }
}
