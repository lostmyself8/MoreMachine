package com.jerry.mekmm.client.recipe_viewer.type;

import com.jerry.mekmm.common.recipe.IMMRecipeTypeProvider;
import com.jerry.mekmm.common.recipe.MMRecipeType;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.text.TextComponentUtil;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

@NothingNullByDefault
public record MMRVRecipeTypeWrapper<VANILLA_INPUT extends RecipeInput, RECIPE extends MekanismRecipe<VANILLA_INPUT>, INPUT_CACHE extends IInputRecipeCache>(
        ResourceLocation id, ItemLike item, Class<? extends RECIPE> recipeClass, IMMRecipeTypeProvider<VANILLA_INPUT, RECIPE, INPUT_CACHE> vanillaProvider,
        int xOffset, int yOffset, int width, int height, List<ItemLike> workstations
) implements IMMRecipeViewerRecipeType<RECIPE>, IMMRecipeTypeProvider<VANILLA_INPUT, RECIPE, INPUT_CACHE> {

    public MMRVRecipeTypeWrapper(IMMRecipeTypeProvider<VANILLA_INPUT, RECIPE, INPUT_CACHE> vanillaProvider, Class<? extends RECIPE> recipeClass,
                                 int xOffset, int yOffset, int width, int height, ItemLike icon, ItemLike... altWorkstations) {
        this(vanillaProvider.getRegistryName(), icon, recipeClass, vanillaProvider, xOffset, yOffset, width, height, List.of(altWorkstations));
    }

    public MMRVRecipeTypeWrapper {
        if (workstations.isEmpty()) {
            workstations = List.of(item);
        } else {
            workstations = Stream.concat(Stream.of(item), workstations.stream()).toList();
        }
    }

    @Override
    public Component getTextComponent() {
        return TextComponentUtil.build(item);
    }

    @Override
    public boolean requiresHolder() {
        return true;
    }

    @Override
    public ItemStack iconStack() {
        return new ItemStack(item);
    }

    @Nullable
    @Override
    public ResourceLocation icon() {
        //Handled by the icon stack
        return null;
    }

    @Override
    public MMRecipeType<VANILLA_INPUT, RECIPE, INPUT_CACHE> getMMRecipeType() {
        return vanillaProvider.getMMRecipeType();
    }
}