package com.jerry.mekmm.client.recipe_viewer.type;

import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.text.IHasTranslationKey;
import mekanism.api.text.TextComponentUtil;
import mekanism.common.registration.impl.BlockRegistryObject;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@NothingNullByDefault
public record MMFakeRVRecipeType<RECIPE>(
        ResourceLocation id, @Nullable ResourceLocation icon, @Nullable ItemLike item, IHasTranslationKey name, Class<? extends RECIPE> recipeClass,
        int xOffset, int yOffset, int width, int height, List<ItemLike> workstations
) implements IMMRecipeViewerRecipeType<RECIPE> {

    public MMFakeRVRecipeType(ResourceLocation id, @Nullable ResourceLocation icon, IHasTranslationKey name, Class<? extends RECIPE> recipeClass,
                              int xOffset, int yOffset, int width, int height, ItemLike... altWorkstations) {
        this(id, icon, null, name, recipeClass, xOffset, yOffset, width, height, List.of(altWorkstations));
    }

    public MMFakeRVRecipeType(ResourceLocation id, ItemLike icon, IHasTranslationKey name, Class<? extends RECIPE> recipeClass,
                              int xOffset, int yOffset, int width, int height, ItemLike... altWorkstations) {
        this(id, icon, name, recipeClass, xOffset, yOffset, width, height, true, altWorkstations);
    }

    public MMFakeRVRecipeType(ResourceLocation id, ItemLike icon, IHasTranslationKey name, Class<? extends RECIPE> recipeClass,
                              int xOffset, int yOffset, int width, int height, boolean iconIsWorkstation, ItemLike... altWorkstations) {
        this(id, null, icon, name, recipeClass, xOffset, yOffset, width, height,
              iconIsWorkstation ? Stream.concat(Stream.of(icon), Arrays.stream(altWorkstations)).toList() : List.of(altWorkstations));
    }

    public MMFakeRVRecipeType(BlockRegistryObject<?, ?> item, Class<? extends RECIPE> recipeClass, int xOffset, int yOffset, int width, int height, ItemLike... altWorkstations) {
        this(item.getId(), item, item, recipeClass, xOffset, yOffset, width, height, altWorkstations);
    }

    @Override
    public Component getTextComponent() {
        return TextComponentUtil.build(name);
    }

    @Override
    public boolean requiresHolder() {
        return false;
    }

    @Override
    public ItemStack iconStack() {
        return item == null ? ItemStack.EMPTY : new ItemStack(item);
    }
}