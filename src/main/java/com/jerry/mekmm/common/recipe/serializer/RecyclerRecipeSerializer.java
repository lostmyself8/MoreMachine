package com.jerry.mekmm.common.recipe.serializer;

import com.jerry.mekmm.api.recipes.RecyclerRecipe;
import com.jerry.mekmm.api.recipes.basic.BasicRecyclerRecipe;
import com.mojang.datafixers.util.Function3;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mekanism.api.SerializationConstants;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

@NothingNullByDefault
public class RecyclerRecipeSerializer implements RecipeSerializer<BasicRecyclerRecipe> {

    private final StreamCodec<RegistryFriendlyByteBuf, BasicRecyclerRecipe> streamCodec;
    private final MapCodec<BasicRecyclerRecipe> codec;

    public RecyclerRecipeSerializer(Function3<ItemStackIngredient, ItemStack, Double, BasicRecyclerRecipe> factory) {

        Codec<Double> chanceCodec = Codec.DOUBLE.validate(d -> d > 0 && d <= 1 ? DataResult.success(d) : DataResult.error(() -> "Expected chance to be greater than zero, and less than or equal to one. Found " + d));

        this.codec = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ItemStackIngredient.CODEC.fieldOf(SerializationConstants.INPUT).forGetter(RecyclerRecipe::getInput),
                ItemStack.CODEC.fieldOf(SerializationConstants.OUTPUT).forGetter(BasicRecyclerRecipe::getChanceOutputRaw),
                chanceCodec.fieldOf("chance").forGetter(BasicRecyclerRecipe::getOutputChance)
        ).apply(instance, factory));

        this.streamCodec = StreamCodec.composite(
                ItemStackIngredient.STREAM_CODEC, RecyclerRecipe::getInput,
                ItemStack.STREAM_CODEC, BasicRecyclerRecipe::getChanceOutputRaw,
                ByteBufCodecs.DOUBLE, RecyclerRecipe::getOutputChance,
                factory
        );
    }

    @Override
    public MapCodec<BasicRecyclerRecipe> codec() {
        return codec;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, BasicRecyclerRecipe> streamCodec() {
        return streamCodec;
    }
}
