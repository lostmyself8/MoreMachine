package com.jerry.mekmm.api.recipes.basic;

import com.jerry.mekmm.api.recipes.MMRecipeSerializers;
import com.jerry.mekmm.api.recipes.PlantingRecipe;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@NothingNullByDefault
public class BasicPlantingRecipe extends PlantingRecipe {

    public final ItemStackIngredient itemInput;
    public final ChemicalStackIngredient chemicalInput;
    public final ItemStack mainOutput;
    public final ItemStack secondaryOutput;

    private final boolean perTickUsage;

    public BasicPlantingRecipe(ItemStackIngredient itemInput, ChemicalStackIngredient chemicalInput, ItemStack mainOutput, ItemStack secondaryOutput, boolean perTickUsage) {
        this.itemInput = Objects.requireNonNull(itemInput, "Input cannot be null.");
        this.chemicalInput = Objects.requireNonNull(chemicalInput, "Chemical input cannot be null.");
        if (mainOutput.isEmpty()) {
            throw new IllegalArgumentException("Main output cannot be null.");
        }
        this.mainOutput = mainOutput.copy();
        this.secondaryOutput = secondaryOutput.copy();

        this.perTickUsage = perTickUsage;
    }

    @Override
    public boolean test(@NotNull ItemStack itemStack, ChemicalStack chemicalStack) {
        return this.itemInput.test(itemStack) && this.chemicalInput.test(chemicalStack);
    }

    @Override
    public boolean perTickUsage() {
        return perTickUsage;
    }

    @Override
    public ItemStackIngredient getItemInput() {
        return itemInput;
    }

    @Override
    public ChemicalStackIngredient getChemicalInput() {
        return chemicalInput;
    }

    @Override
    public List<PlantingStationRecipeOutput> getOutputDefinition() {
        return Collections.singletonList(new PlantingStationRecipeOutput(mainOutput, secondaryOutput));
    }

    @Override
    public List<PlantingStationRecipeOutput> getSecondaryOutputDefinition() {
        return Collections.singletonList(new PlantingStationRecipeOutput(mainOutput, secondaryOutput));
    }

    public ItemStack getMainOutput() {
        return mainOutput;
    }

    public ItemStack getSecondaryOutput() {
        return secondaryOutput.isEmpty() ? ItemStack.EMPTY : secondaryOutput;
    }

    @Override
    @Contract(value = "_, _ -> new", pure = true)
    public PlantingStationRecipeOutput getOutput(ItemStack solid, ChemicalStack chemical) {
        return new PlantingStationRecipeOutput(this.mainOutput.copy(), this.secondaryOutput.copy());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MMRecipeSerializers.PLANTING.get();
    }
}
