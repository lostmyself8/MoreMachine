package com.jerry.meklm.client.recipe_viewer.jei;

import com.jerry.meklm.common.registries.LMBlocks;
import com.jerry.mekmm.Mekmm;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.client.recipe_viewer.jei.MekanismJEI;
import mekanism.client.recipe_viewer.type.RecipeViewerRecipeType;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
@NothingNullByDefault
public class LargeMachineJEI implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        // 不能使用Mekmm.rl()，原因见MekanismJEI.class
        return ResourceLocation.fromNamespaceAndPath(Mekmm.MOD_ID_LM, "jei_plugin");
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        if (!MekanismJEI.shouldLoad()) {
            return;
        }

        // 只是添加JEI的侧面栏的显示
        registry.addRecipeCatalyst(LMBlocks.LARGE_ELECTROLYTIC_SEPARATOR, MekanismJEI.genericRecipeType(RecipeViewerRecipeType.SEPARATING));
        registry.addRecipeCatalyst(LMBlocks.LARGE_CHEMICAL_WASHER, MekanismJEI.genericRecipeType(RecipeViewerRecipeType.WASHING));
    }
}
