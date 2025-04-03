package com.jerry.mekmm.common.recipe.serializer;

import com.jerry.mekmm.api.recipes.PlantingRecipe;
import com.jerry.mekmm.api.recipes.basic.BasicPlantingRecipe;
import com.mojang.datafixers.util.Function5;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mekanism.api.SerializationConstants;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

@NothingNullByDefault
public class PlantingRecipeSerializer implements RecipeSerializer<BasicPlantingRecipe> {

    private final StreamCodec<RegistryFriendlyByteBuf, BasicPlantingRecipe> streamCodec;
    private final MapCodec<BasicPlantingRecipe> codec;

    public PlantingRecipeSerializer(Function5<ItemStackIngredient, ChemicalStackIngredient, ItemStack, ItemStack, Boolean, BasicPlantingRecipe> factory) {
        this.codec = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ItemStackIngredient.CODEC.fieldOf(SerializationConstants.ITEM_INPUT).forGetter(PlantingRecipe::getItemInput),
                IngredientCreatorAccess.chemicalStack().codec().fieldOf(SerializationConstants.CHEMICAL_INPUT).forGetter(PlantingRecipe::getChemicalInput),
                ItemStack.CODEC.fieldOf(SerializationConstants.MAIN_OUTPUT).forGetter(BasicPlantingRecipe::getMainOutput),
                ItemStack.CODEC.optionalFieldOf(SerializationConstants.SECONDARY_OUTPUT, ItemStack.EMPTY).forGetter(BasicPlantingRecipe::getSecondaryOutput),
                Codec.BOOL.fieldOf(SerializationConstants.PER_TICK_USAGE).forGetter(BasicPlantingRecipe::perTickUsage)
        ).apply(instance, factory));

        this.streamCodec = StreamCodec.composite(
                ItemStackIngredient.STREAM_CODEC, PlantingRecipe::getItemInput,
                IngredientCreatorAccess.chemicalStack().streamCodec(), PlantingRecipe::getChemicalInput,
                ItemStack.STREAM_CODEC, BasicPlantingRecipe::getMainOutput,
                ItemStack.OPTIONAL_STREAM_CODEC, BasicPlantingRecipe::getSecondaryOutput,
                ByteBufCodecs.BOOL, BasicPlantingRecipe::perTickUsage,
                factory
        );
    }

    @Override
    public MapCodec<BasicPlantingRecipe> codec() {
        return codec;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, BasicPlantingRecipe> streamCodec() {
        return streamCodec;
    }
}
