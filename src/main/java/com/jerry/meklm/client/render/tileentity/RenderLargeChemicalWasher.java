package com.jerry.meklm.client.render.tileentity;

import com.jerry.meklm.client.model.ModelLargeChemicalWasher;
import com.jerry.meklm.common.tile.TileEntityLargeChemicalWasher;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.client.render.MekanismRenderer;
import mekanism.client.render.tileentity.IWireFrameRenderer;
import mekanism.client.render.tileentity.ModelTileEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

@NothingNullByDefault
public class RenderLargeChemicalWasher extends ModelTileEntityRenderer<TileEntityLargeChemicalWasher, ModelLargeChemicalWasher> implements IWireFrameRenderer {

    public RenderLargeChemicalWasher(BlockEntityRendererProvider.Context context) {
        super(context, ModelLargeChemicalWasher::new);
    }

    @Override
    public void renderWireFrame(BlockEntity tile, float partialTick, PoseStack matrix, VertexConsumer buffer) {
        if (tile instanceof TileEntityLargeChemicalWasher generator) {
            setupRenderer(generator, partialTick, matrix);
            model.renderWireFrame(matrix, buffer);
            matrix.popPose();
        }
    }

    @Override
    protected void render(TileEntityLargeChemicalWasher tile, float partialTick, PoseStack matrix, MultiBufferSource renderer, int light, int overlayLight, ProfilerFiller profiler) {
        setupRenderer(tile, partialTick, matrix);
        model.render(matrix, renderer, light, overlayLight, false);
        matrix.popPose();
    }

    @Override
    protected String getProfilerSection() {
        return "large_chemical_washer";
    }

    @Override
    public boolean shouldRenderOffScreen(TileEntityLargeChemicalWasher blockEntity) {
        return true;
    }

    @Override
    public AABB getRenderBoundingBox(TileEntityLargeChemicalWasher tile) {
        BlockPos pos = tile.getBlockPos();
        return AABB.encapsulatingFullBlocks(pos, pos.above());
    }

    //对模型进行平移和翻转
    private void setupRenderer(TileEntityLargeChemicalWasher tile, float partialTick, PoseStack matrix) {
        matrix.pushPose();
        matrix.translate(0.5, 1.5, 0.5);
        MekanismRenderer.rotate(matrix, tile.getDirection(), 0, 180, 90, 270);
        matrix.mulPose(Axis.ZP.rotationDegrees(180));
    }
}
