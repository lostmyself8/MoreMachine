package com.jerry.mekmm.mixin;

import com.jerry.mekmm.common.registries.MMBlocks;
import mekanism.api.recipes.ItemStackToChemicalRecipe;
import mekanism.client.recipe_viewer.type.RecipeViewerRecipeType;
import mekanism.client.recipe_viewer.type.SimpleRVRecipeType;
import mekanism.common.MekanismLang;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.util.MekanismUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RecipeViewerRecipeType.class, remap = false)
public abstract class MixinRecipeViewerRecipeType {

    @Final
    @Shadow
    @Mutable
    public static SimpleRVRecipeType<?, ItemStackToChemicalRecipe, ?> CHEMICAL_CONVERSION;

    // 使JEI侧栏显示种植机和复制机等机器（给化学品的物品）
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void modifyEnergyConversion(CallbackInfo ci) {
        CHEMICAL_CONVERSION = new SimpleRVRecipeType<>(MekanismRecipeType.CHEMICAL_CONVERSION, ItemStackToChemicalRecipe.class, MekanismLang.CONVERSION_CHEMICAL, MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "chemicals.png"), -20, -12, 132, 62,
                MekanismBlocks.PURIFICATION_CHAMBER, MekanismBlocks.METALLURGIC_INFUSER, MekanismBlocks.OSMIUM_COMPRESSOR, MekanismBlocks.CHEMICAL_INJECTION_CHAMBER, MekanismBlocks.CHEMICAL_DISSOLUTION_CHAMBER, MekanismBlocks.ANTIPROTONIC_NUCLEOSYNTHESIZER,
                // TODO:这里的顺序会影响显示效果，这很奇怪
                MMBlocks.REPLICATOR, MMBlocks.PLANTING_STATION);

    }
}
