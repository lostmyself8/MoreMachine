package com.jerry.mekmm.client.recipe_viewer.type;

import com.jerry.mekmm.api.recipes.PlantingRecipe;
import com.jerry.mekmm.api.recipes.RecyclerRecipe;
import com.jerry.mekmm.api.recipes.basic.MMBasicItemStackChemicalToItemStackRecipe;
import com.jerry.mekmm.common.recipe.MMRecipeType;
import com.jerry.mekmm.common.registries.MMBlocks;
import mekanism.api.recipes.ItemStackToItemStackRecipe;

public class MMRecipeViewerRecipeType {

    private MMRecipeViewerRecipeType() {

    }

    public static final MMRVRecipeTypeWrapper<?, RecyclerRecipe, ?> RECYCLER = new MMRVRecipeTypeWrapper<>(MMRecipeType.RECYCLER, RecyclerRecipe.class, -28, -16, 144, 54, MMBlocks.RECYCLER);

    public static final MMRVRecipeTypeWrapper<?, PlantingRecipe, ?> PLANTING_STATION = new MMRVRecipeTypeWrapper<>(MMRecipeType.PLANTING_STATION, PlantingRecipe.class, -28, -16, 144, 54, MMBlocks.PLANTING_STATION);

    public static final MMRVRecipeTypeWrapper<?, ItemStackToItemStackRecipe, ?> STAMPING = new MMRVRecipeTypeWrapper<>(MMRecipeType.STAMPING, ItemStackToItemStackRecipe.class, -28, -16, 144, 54, MMBlocks.CNC_STAMPER);

    public static final MMRVRecipeTypeWrapper<?, ItemStackToItemStackRecipe, ?> LATHE = new MMRVRecipeTypeWrapper<>(MMRecipeType.LATHE, ItemStackToItemStackRecipe.class, -28, -16, 144, 54, MMBlocks.CNC_LATHE);

    public static final MMRVRecipeTypeWrapper<?, ItemStackToItemStackRecipe, ?> ROLLING_MILL = new MMRVRecipeTypeWrapper<>(MMRecipeType.ROLLING_MILL, ItemStackToItemStackRecipe.class, -28, -16, 144, 54, MMBlocks.CNC_ROLLING_MILL);

    public static final MMFakeRVRecipeType<MMBasicItemStackChemicalToItemStackRecipe> REPLICATOR = new MMFakeRVRecipeType<>(MMBlocks.REPLICATOR, MMBasicItemStackChemicalToItemStackRecipe.class, -3, -3, 170, 79);

}
