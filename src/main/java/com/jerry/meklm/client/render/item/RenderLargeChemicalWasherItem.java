package com.jerry.meklm.client.render.item;

import com.jerry.meklm.client.model.ModelLargeChemicalWasher;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mekanism.client.render.item.MekanismISTER;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RenderLargeChemicalWasherItem extends MekanismISTER {

    public static final RenderLargeChemicalWasherItem RENDERER = new RenderLargeChemicalWasherItem();
    private ModelLargeChemicalWasher largeChemicalWasher;

    @Override
    public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
        largeChemicalWasher = new ModelLargeChemicalWasher(getEntityModels());
    }

    @Override
    public void renderByItem(@NotNull ItemStack stack, @NotNull ItemDisplayContext displayContext, @NotNull PoseStack matrix, @NotNull MultiBufferSource renderer, int light, int overlayLight) {
        matrix.pushPose();
        matrix.translate(0.5, 0.5, 0.5);
        matrix.mulPose(Axis.ZP.rotationDegrees(180));
        largeChemicalWasher.render(matrix, renderer, light, overlayLight, stack.hasFoil());
        matrix.popPose();
    }
}
