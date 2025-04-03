package com.jerry.mekmm.common.tile.factory;

import com.jerry.mekmm.client.recipe_viewer.type.IMMRecipeViewerRecipeType;
import com.jerry.mekmm.client.recipe_viewer.type.MMRecipeViewerRecipeType;
import com.jerry.mekmm.common.recipe.IMMRecipeTypeProvider;
import com.jerry.mekmm.common.recipe.MMRecipeType;
import com.jerry.mekmm.common.recipe.lookup.IMMSingleRecipeLookupHandler;
import com.jerry.mekmm.common.recipe.lookup.cache.MMInputRecipeCache;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.math.MathUtils;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.cache.OneInputCachedRecipe;
import mekanism.common.upgrade.MachineUpgradeData;
import mekanism.common.util.InventoryUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.TriPredicate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

//Stamping, Lathe, Rolling_Mill
public class MMTileEntityItemStackToItemStackFactory extends MMTileEntityItemToItemFactory<ItemStackToItemStackRecipe> implements
        IMMSingleRecipeLookupHandler.ItemRecipeLookupHandler<ItemStackToItemStackRecipe> {

    private static final TriPredicate<ItemStackToItemStackRecipe, ItemStack, ItemStack> OUTPUT_CHECK =
          (recipe, input, output) -> InventoryUtils.areItemsStackable(recipe.getOutput(input), output);
    private static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
          RecipeError.NOT_ENOUGH_ENERGY,
          RecipeError.NOT_ENOUGH_INPUT,
          RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
          RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT
    );
    private static final Set<RecipeError> GLOBAL_ERROR_TYPES = Set.of(RecipeError.NOT_ENOUGH_ENERGY);

    public MMTileEntityItemStackToItemStackFactory(Holder<Block> blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES, GLOBAL_ERROR_TYPES);
    }

    @Override
    public boolean isItemValidForSlot(@NotNull ItemStack stack) {
        //contains recipe in general already validated by isValidInputItem
        return true;
    }

    @Override
    public boolean isValidInputItem(@NotNull ItemStack stack) {
        return containsRecipe(stack);
    }

    @Override
    protected int getNeededInput(ItemStackToItemStackRecipe recipe, ItemStack inputStack) {
        return MathUtils.clampToInt(recipe.getInput().getNeededAmount(inputStack));
    }

    @Override
    protected boolean isCachedRecipeValid(@Nullable CachedRecipe<ItemStackToItemStackRecipe> cached, @NotNull ItemStack stack) {
        return cached != null && cached.getRecipe().getInput().testType(stack);
    }

    @Override
    protected ItemStackToItemStackRecipe findRecipe(int process, @NotNull ItemStack fallbackInput, @NotNull IInventorySlot outputSlot,
          @Nullable IInventorySlot secondaryOutputSlot) {
        return getMMRecipeType().getInputCache().findTypeBasedRecipe(level, fallbackInput, outputSlot.getStack(), OUTPUT_CHECK);
    }

    @NotNull
    @Override
    public IMMRecipeTypeProvider<SingleRecipeInput, ItemStackToItemStackRecipe, MMInputRecipeCache.SingleItem<ItemStackToItemStackRecipe>> getMMRecipeType() {
        return switch (type) {
            case CNC_STAMPER -> MMRecipeType.STAMPING;
            case CNC_LATHE -> MMRecipeType.LATHE;
            //TODO: Make it so that it throws an error if it is not one of the three types
            default -> MMRecipeType.ROLLING_MILL;
        };
    }

    @Override
    public IMMRecipeViewerRecipeType<ItemStackToItemStackRecipe> recipeViewerType() {
        return switch (type) {
            case CNC_STAMPER -> MMRecipeViewerRecipeType.STAMPING;
            case CNC_LATHE -> MMRecipeViewerRecipeType.LATHE;
            //TODO: Make it so that it throws an error if it is not one of the three types
            default -> MMRecipeViewerRecipeType.ROLLING_MILL;
        };
    }

    @Nullable
    @Override
    public ItemStackToItemStackRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(inputHandlers[cacheIndex]);
    }

    @NotNull
    @Override
    public CachedRecipe<ItemStackToItemStackRecipe> createNewCachedRecipe(@NotNull ItemStackToItemStackRecipe recipe, int cacheIndex) {
        return OneInputCachedRecipe.itemToItem(recipe, recheckAllRecipeErrors[cacheIndex], inputHandlers[cacheIndex], outputHandlers[cacheIndex])
              .setErrorsChanged(errors -> errorTracker.onErrorsChanged(errors, cacheIndex))
              .setCanHolderFunction(this::canFunction)
              .setActive(active -> setActiveState(active, cacheIndex))
              .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
              .setRequiredTicks(this::getTicksRequired)
              .setOnFinish(this::markForSave)
              .setOperatingTicksChanged(operatingTicks -> progress[cacheIndex] = operatingTicks);
    }

    @NotNull
    @Override
    public MachineUpgradeData getUpgradeData(HolderLookup.Provider provider) {
        return new MachineUpgradeData(provider, redstone, getControlType(), getEnergyContainer(), progress, energySlot, inputSlots, outputSlots, isSorting(), getComponents());
    }
}