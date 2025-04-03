package com.jerry.mekmm.common.recipe.lookup.monitor;

import com.jerry.mekmm.common.recipe.lookup.IMMRecipeLookupHandler;
import mekanism.api.recipes.MekanismRecipe;
import org.jetbrains.annotations.NotNull;

public class MMFactoryRecipeCacheLookupMonitor<RECIPE extends MekanismRecipe<?>> extends MMRecipeCacheLookupMonitor<RECIPE> {

    private final Runnable setSortingNeeded;

    public MMFactoryRecipeCacheLookupMonitor(IMMRecipeLookupHandler<RECIPE> handler, int cacheIndex, Runnable setSortingNeeded) {
        super(handler, cacheIndex);
        this.setSortingNeeded = setSortingNeeded;
    }

    @Override
    public void onChange() {
        super.onChange();
        //Mark that sorting is needed
        setSortingNeeded.run();
    }

    public void updateCachedRecipe(@NotNull RECIPE recipe) {
        cachedRecipe = createNewCachedRecipe(recipe, cacheIndex);
        //Note: While this is probably not strictly needed we clear our cache of knowing we have no recipe
        // so that we can properly re-enter the lookup cycle if needed
        hasNoRecipe = false;
    }
}
