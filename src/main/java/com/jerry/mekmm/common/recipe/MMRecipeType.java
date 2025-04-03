package com.jerry.mekmm.common.recipe;

import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.api.recipes.MMRecipeTypes;
import com.jerry.mekmm.api.recipes.PlantingRecipe;
import com.jerry.mekmm.api.recipes.RecyclerRecipe;
import com.jerry.mekmm.common.recipe.lookup.cache.MMInputRecipeCache;
import com.jerry.mekmm.common.registration.impl.MMRecipeTypeDeferredRegister;
import com.jerry.mekmm.common.registration.impl.MMRecipeTypeRegistryObject;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.vanilla_input.SingleItemChemicalRecipeInput;
import mekanism.client.MekanismClient;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class MMRecipeType<VANILLA_INPUT extends RecipeInput, RECIPE extends MekanismRecipe<VANILLA_INPUT>, INPUT_CACHE extends IInputRecipeCache>
        implements RecipeType<RECIPE>, IMMRecipeTypeProvider<VANILLA_INPUT, RECIPE, INPUT_CACHE> {

    public static final MMRecipeTypeDeferredRegister MM_RECIPE_TYPES = new MMRecipeTypeDeferredRegister(Mekmm.MOD_ID);

    public static final MMRecipeTypeRegistryObject<SingleRecipeInput, RecyclerRecipe, MMInputRecipeCache.SingleItem<RecyclerRecipe>> RECYCLER = register(MMRecipeTypes.NAME_RECYCLER, recipeType -> new MMInputRecipeCache.SingleItem<>(recipeType, RecyclerRecipe::getInput));

    public static final MMRecipeTypeRegistryObject<SingleItemChemicalRecipeInput, PlantingRecipe, MMInputRecipeCache.ItemChemical<PlantingRecipe>> PLANTING_STATION = register(MMRecipeTypes.NAME_PLANTING, recipeType -> new MMInputRecipeCache.ItemChemical<>(recipeType, PlantingRecipe::getItemInput, PlantingRecipe::getChemicalInput));

    public static final MMRecipeTypeRegistryObject<SingleRecipeInput, ItemStackToItemStackRecipe, MMInputRecipeCache.SingleItem<ItemStackToItemStackRecipe>> STAMPING = register(MMRecipeTypes.NAME_STAMPING, recipeType -> new MMInputRecipeCache.SingleItem<>(recipeType, ItemStackToItemStackRecipe::getInput));

    public static final MMRecipeTypeRegistryObject<SingleRecipeInput, ItemStackToItemStackRecipe, MMInputRecipeCache.SingleItem<ItemStackToItemStackRecipe>> LATHE = register(MMRecipeTypes.NAME_LATHE, recipeType -> new MMInputRecipeCache.SingleItem<>(recipeType, ItemStackToItemStackRecipe::getInput));

    public static final MMRecipeTypeRegistryObject<SingleRecipeInput, ItemStackToItemStackRecipe, MMInputRecipeCache.SingleItem<ItemStackToItemStackRecipe>> ROLLING_MILL = register(MMRecipeTypes.NAME_ROLLING_MILL, recipeType -> new MMInputRecipeCache.SingleItem<>(recipeType, ItemStackToItemStackRecipe::getInput));

    private static <VANILLA_INPUT extends RecipeInput, RECIPE extends MekanismRecipe<VANILLA_INPUT>, INPUT_CACHE extends IInputRecipeCache>
    MMRecipeTypeRegistryObject<VANILLA_INPUT, RECIPE, INPUT_CACHE> register(
            ResourceLocation name,
            Function<MMRecipeType<VANILLA_INPUT, RECIPE, INPUT_CACHE>, INPUT_CACHE> inputCacheCreator
    ) {
        if (!Mekmm.MOD_ID.equals(name.getNamespace())) {
            throw new IllegalStateException("Name must be in " + Mekmm.MOD_ID + " namespace");
        }
        return MM_RECIPE_TYPES.registerMek(name.getPath(), registryName -> new MMRecipeType<>(registryName, inputCacheCreator));
    }

    public static void clearCache() {
        for (Holder<RecipeType<?>> entry : MM_RECIPE_TYPES.getEntries()) {
            //Note: We expect all entries to be a MekanismRecipeType, but we validate it just to be sure
            if (entry.value() instanceof MMRecipeType<?, ?, ?> recipeType) {
                recipeType.clearCaches();
            }
        }
    }

    private List<RecipeHolder<RECIPE>> cachedRecipes = Collections.emptyList();
    private final ResourceLocation registryName;
    private final INPUT_CACHE inputCache;

    private MMRecipeType(ResourceLocation name, Function<MMRecipeType<VANILLA_INPUT, RECIPE, INPUT_CACHE>, INPUT_CACHE> inputCacheCreator) {
        this.registryName = name;
        this.inputCache = inputCacheCreator.apply(this);
    }

    @Override
    public MMRecipeType<VANILLA_INPUT, RECIPE, INPUT_CACHE> getMMRecipeType() {
        return this;
    }

    public ResourceLocation getRegistryName() {
        return registryName;
    }

    private void clearCaches() {
        cachedRecipes = Collections.emptyList();
        inputCache.clear();
    }

    public INPUT_CACHE getInputCache() {
        return inputCache;
    }

    @Nullable
    private Level getLevel(@Nullable Level level) {
        if (level == null) {
            //Try to get a fallback world if we are in a context that may not have one
            //If we are on the client get the client's world, if we are on the server get the current server's world
            if (FMLEnvironment.dist.isClient()) {
                return MekanismClient.tryGetClientWorld();
            }
            return ServerLifecycleHooks.getCurrentServer().overworld();
        }
        return level;
    }

    @NotNull
    @Override
    public List<RecipeHolder<RECIPE>> getRecipes(@Nullable Level world) {
        world = getLevel(world);
        if (world == null) {
            //If we failed, then return no recipes
            return Collections.emptyList();
        }
        return getRecipes(world.getRecipeManager(), world);
    }

    @NotNull
    @Override
    public List<RecipeHolder<RECIPE>> getRecipes(RecipeManager recipeManager, @Nullable Level world) {
        if (cachedRecipes.isEmpty()) {
            //Note: This is a fresh immutable list that gets returned
            List<RecipeHolder<RECIPE>> recipes = recipeManager.getAllRecipesFor(this);

//            if (this == SMELTING.get()) {
//                world = getLevel(world);
//                if (world == null) {
//                    //If we failed, then only return the recipes that are for the base type
//                    return recipes.stream()
//                            .filter(recipe -> !recipe.value().isIncomplete())
//                            .toList();
//                }
//                //Ensure the recipes can be modified
//                recipes = new ArrayList<>(recipes);
//                for (RecipeHolder<SmeltingRecipe> smeltingRecipe : recipeManager.getAllRecipesFor(RecipeType.SMELTING)) {
//                    ItemStack recipeOutput = smeltingRecipe.value().getResultItem(world.registryAccess());
//                    if (!smeltingRecipe.value().isSpecial() && !smeltingRecipe.value().isIncomplete() && !recipeOutput.isEmpty()) {
//                        //TODO: Can Smelting recipes even be "special", if so can we add some sort of checker to make getOutput return the correct result
//                        NonNullList<Ingredient> ingredients = smeltingRecipe.value().getIngredients();
//                        if (ingredients.isEmpty()) {
//                            //Something went wrong
//                            continue;
//                        }
//                        ItemStackIngredient input = IngredientCreatorAccess.item().from(CompoundIngredient.of(ingredients.toArray(Ingredient[]::new)));
//                        recipes.add(new RecipeHolder<>(RecipeViewerUtils.synthetic(smeltingRecipe.id(), "mekanism_generated"),
//                                castRecipe(new BasicSmeltingRecipe(input, recipeOutput))));
//                    }
//                }
//            }

            //Make the list of cached recipes immutable and filter out any incomplete recipes
            // as there is no reason to potentially look the partial complete piece up if
            // the other portion of the recipe is incomplete
            cachedRecipes = recipes.stream()
                    .filter(recipe -> !recipe.value().isIncomplete())
                    .toList();
        }
        return cachedRecipes;
    }

    @SuppressWarnings("unchecked")
    private RECIPE castRecipe(MekanismRecipe<?> o) {
        if (o.getType() != this) {
            throw new IllegalArgumentException("Wrong recipe type");
        }
        return (RECIPE) o;
    }

    /**
     * Helper for getting a recipe from a world's recipe manager.
     */
    public static <I extends RecipeInput, RECIPE_TYPE extends Recipe<I>> Optional<RecipeHolder<RECIPE_TYPE>> getRecipeFor(RecipeType<RECIPE_TYPE> recipeType, I input,
                                                                                                                          Level level) {
        //Only allow looking up complete recipes or special recipes as we only use this method for vanilla recipe types
        // and special recipes return that they are not complete
        return level.getRecipeManager().getRecipeFor(recipeType, input, level)
                .filter(recipe -> recipe.value().isSpecial() || !recipe.value().isIncomplete());
    }

    /**
     * Helper for getting a recipe from a world's recipe manager.
     */
    public static Optional<RecipeHolder<?>> byKey(Level level, ResourceLocation id) {
        //Only allow looking up complete recipes or special recipes as we only use this method for vanilla recipe types
        // and special recipes return that they are not complete
        return level.getRecipeManager().byKey(id)
                .filter(recipe -> recipe.value().isSpecial() || !recipe.value().isIncomplete());
    }
}
