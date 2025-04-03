package com.jerry.mekmm.api.recipes;

import com.jerry.mekmm.Mekmm;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.vanilla_input.SingleItemChemicalRecipeInput;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * Represents a recipe that can be used in the Planting Station.
 *
 * @author Jerry
 */
@NothingNullByDefault
public abstract class PlantingRecipe extends MekanismRecipe<SingleItemChemicalRecipeInput> implements BiPredicate<@NotNull ItemStack, ChemicalStack> {

    private static final Holder<Item> PLANTING_STATION = DeferredHolder.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Mekmm.MOD_ID, "plating_station"));

    @Override
    public abstract boolean test(@NotNull ItemStack itemStack, ChemicalStack chemicalStack);

    @Override
    public boolean matches(SingleItemChemicalRecipeInput input, Level level) {
        return !isIncomplete() && test(input.item(), input.chemical());
    }

    /**
     * Represents whether this recipe consumes the chemical each tick.
     */
    public abstract boolean perTickUsage();

    /**
     * Gets the input item ingredient.
     * @return ItemStackIngredient
     */
    public abstract ItemStackIngredient getItemInput();

    /**
     * Gets the input chemical ingredient.
     * @return ChemicalStackIngredient
     */
    public abstract ChemicalStackIngredient getChemicalInput();

    /**
     * For JEI, gets the output representations to display.
     *
     * @return Representation of the output, <strong>MUST NOT</strong> be modified.
     */
    public abstract List<PlantingStationRecipeOutput> getOutputDefinition();

    /**
     * For JEI, gets the output representations to display.
     *
     * @return Representation of the output, <strong>MUST NOT</strong> be modified.
     */
    public abstract List<PlantingStationRecipeOutput> getSecondaryOutputDefinition();

    /**
     * Gets a new output based on the given inputs.
     *
     * @param seed  Specific item input.
     * @param chemical    Specific chemical input.
     *
     * @return New output.
     *
     * @apiNote While Mekanism does not currently make use of the inputs, it is important to support it and pass the proper value in case any addons define input based
     * outputs where things like NBT may be different.
     * @implNote The passed in inputs should <strong>NOT</strong> be modified.
     */
    @Contract(value = "_, _ -> new", pure = true)
    public abstract PlantingStationRecipeOutput getOutput(ItemStack seed, ChemicalStack chemical);

    @Override
    public boolean isIncomplete() {
        return getItemInput().hasNoMatchingInstances() || getChemicalInput().hasNoMatchingInstances();
    }

    @Override
    public RecipeType<?> getType() {
        return MMRecipeTypes.TYPE_PLANTING.value();
    }

    @Override
    public String getGroup() {
        return "planting_station";
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(PLANTING_STATION);
    }

    /**
     * @apiNote Main item cannot be null, secondary item can be null.
     */
    public record PlantingStationRecipeOutput(@NotNull ItemStack first, @NotNull ItemStack second) {

        public PlantingStationRecipeOutput {
            Objects.requireNonNull(first, "First output cannot be null.");
            if (first.isEmpty()) {
                throw new IllegalArgumentException("Main output cannot be null");
            }
        }
    }
}
