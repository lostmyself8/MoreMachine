package com.jerry.mekmm.client.recipe_viewer.jei;

import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.client.recipe_viewer.MMRecipeViewerUtils;
import com.jerry.mekmm.client.recipe_viewer.jei.machine.MMItemStackToItemStackRecipeCategory;
import com.jerry.mekmm.client.recipe_viewer.jei.machine.PlantingRecipeCategory;
import com.jerry.mekmm.client.recipe_viewer.jei.machine.RecyclerRecipeCategory;
import com.jerry.mekmm.client.recipe_viewer.jei.machine.ReplicatorRecipeCategory;
import com.jerry.mekmm.client.recipe_viewer.type.IMMRecipeViewerRecipeType;
import com.jerry.mekmm.client.recipe_viewer.type.MMRecipeViewerRecipeType;
import com.jerry.mekmm.common.recipe.MMRecipeType;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.client.gui.GuiMekanism;
import mekanism.client.recipe_viewer.jei.MekanismJEI;
import mekanism.client.recipe_viewer.type.RecipeViewerRecipeType;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@JeiPlugin
@NothingNullByDefault
public class MoreMachineJEI implements IModPlugin {

    private static final Map<IMMRecipeViewerRecipeType<?>, RecipeType<?>> recipeTypeInstanceCache = new HashMap<>();

    public static RecipeType<?> genericRecipeType(IMMRecipeViewerRecipeType<?> recipeType) {
        return recipeTypeInstanceCache.computeIfAbsent(recipeType, r -> {
            if (r.requiresHolder()) {
                return RecipeType.createRecipeHolderType(r.id());
            }
            return new RecipeType<>(r.id(), r.recipeClass());
        });
    }

    @SuppressWarnings("unchecked")
    public static <TYPE> RecipeType<TYPE> recipeType(IMMRecipeViewerRecipeType<TYPE> recipeType) {
        if (recipeType.requiresHolder()) {
            throw new IllegalStateException("Basic recipe type requested for a recipe that uses holders");
        }
        return (RecipeType<TYPE>) genericRecipeType(recipeType);
    }

    @SuppressWarnings("unchecked")
    public static <TYPE extends Recipe<?>> RecipeType<RecipeHolder<TYPE>> holderRecipeType(IMMRecipeViewerRecipeType<TYPE> recipeType) {
        if (!recipeType.requiresHolder()) {
            throw new IllegalStateException("Holder recipe type requested for a recipe that doesn't use holders");
        }
        return (RecipeType<RecipeHolder<TYPE>>) genericRecipeType(recipeType);
    }

    public static RecipeType<?>[] recipeType(IMMRecipeViewerRecipeType<?>... recipeTypes) {
        return Arrays.stream(recipeTypes).map(MoreMachineJEI::genericRecipeType).toArray(RecipeType[]::new);
    }

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        // 不能使用Mekmm.rl()，原因见MekanismJEI.class
        return ResourceLocation.fromNamespaceAndPath(Mekmm.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registry) {
        if (!MekanismJEI.shouldLoad()) {
            return;
        }
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(new RecyclerRecipeCategory(guiHelper, MMRecipeViewerRecipeType.RECYCLER));
        registry.addRecipeCategories(new PlantingRecipeCategory(guiHelper, MMRecipeViewerRecipeType.PLANTING_STATION));

        registry.addRecipeCategories(new ReplicatorRecipeCategory(guiHelper, MMRecipeViewerRecipeType.REPLICATOR));

        registry.addRecipeCategories(new MMItemStackToItemStackRecipeCategory(guiHelper, MMRecipeViewerRecipeType.STAMPING));
        registry.addRecipeCategories(new MMItemStackToItemStackRecipeCategory(guiHelper, MMRecipeViewerRecipeType.LATHE));
        registry.addRecipeCategories(new MMItemStackToItemStackRecipeCategory(guiHelper, MMRecipeViewerRecipeType.ROLLING_MILL));
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registry) {
        if (!MekanismJEI.shouldLoad()) {
            return;
        }
        registry.addGenericGuiContainerHandler(GuiMekanism.class, new MMJeiGuiElementHandler(registry.getJeiHelpers().getIngredientManager()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        if (!MekanismJEI.shouldLoad()) {
            return;
        }
        MMRecipeRegistryHelper.register(registry, MMRecipeViewerRecipeType.RECYCLER, MMRecipeType.RECYCLER);
        MMRecipeRegistryHelper.register(registry, MMRecipeViewerRecipeType.PLANTING_STATION, MMRecipeType.PLANTING_STATION);

        MMRecipeRegistryHelper.register(registry, MMRecipeViewerRecipeType.REPLICATOR, MMRecipeViewerUtils.getReplicatorRecipes());

        MMRecipeRegistryHelper.register(registry, MMRecipeViewerRecipeType.STAMPING, MMRecipeType.STAMPING);
        MMRecipeRegistryHelper.register(registry, MMRecipeViewerRecipeType.LATHE, MMRecipeType.LATHE);
        MMRecipeRegistryHelper.register(registry, MMRecipeViewerRecipeType.ROLLING_MILL, MMRecipeType.ROLLING_MILL);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        if (!MekanismJEI.shouldLoad()) {
            return;
        }
        MMCatalystRegistryHelper.register(registry, MMRecipeViewerRecipeType.RECYCLER, MMRecipeViewerRecipeType.PLANTING_STATION, MMRecipeViewerRecipeType.REPLICATOR,
                MMRecipeViewerRecipeType.STAMPING, MMRecipeViewerRecipeType.LATHE, MMRecipeViewerRecipeType.ROLLING_MILL);

        MMCatalystRegistryHelper.register(registry, RecipeViewerRecipeType.CHEMICAL_CONVERSION);
    }
}
