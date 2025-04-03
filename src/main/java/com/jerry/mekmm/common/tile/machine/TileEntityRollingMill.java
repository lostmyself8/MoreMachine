package com.jerry.mekmm.common.tile.machine;

import com.jerry.mekmm.client.recipe_viewer.type.IMMRecipeViewerRecipeType;
import com.jerry.mekmm.client.recipe_viewer.type.MMRecipeViewerRecipeType;
import com.jerry.mekmm.common.recipe.IMMRecipeTypeProvider;
import com.jerry.mekmm.common.recipe.MMRecipeType;
import com.jerry.mekmm.common.recipe.lookup.cache.MMInputRecipeCache;
import com.jerry.mekmm.common.registries.MMBlocks;
import com.jerry.mekmm.common.tile.prefab.MMTileEntityElectricMachine;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TileEntityRollingMill extends MMTileEntityElectricMachine {

    public TileEntityRollingMill(BlockPos pos, BlockState state) {
        super(MMBlocks.CNC_ROLLING_MILL, pos, state, BASE_TICKS_REQUIRED);
    }

    @Override
    public @NotNull IMMRecipeTypeProvider<?, ItemStackToItemStackRecipe, MMInputRecipeCache.SingleItem<ItemStackToItemStackRecipe>> getMMRecipeType() {
        return MMRecipeType.ROLLING_MILL;
    }

    @Override
    public @Nullable IMMRecipeViewerRecipeType<ItemStackToItemStackRecipe> recipeViewerType() {
        return MMRecipeViewerRecipeType.ROLLING_MILL;
    }
}
