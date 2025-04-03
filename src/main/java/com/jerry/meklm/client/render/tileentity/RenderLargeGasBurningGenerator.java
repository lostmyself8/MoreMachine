package com.jerry.meklm.client.render.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.client.render.MekanismRenderer;
import mekanism.client.render.tileentity.IWireFrameRenderer;
import mekanism.client.render.tileentity.ModelTileEntityRenderer;
import com.jerry.meklm.client.model.ModelLargeGasBurningGenerator;
import com.jerry.meklm.common.tile.TileEntityLargeGasGenerator;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

@NothingNullByDefault
public class RenderLargeGasBurningGenerator extends ModelTileEntityRenderer<TileEntityLargeGasGenerator, ModelLargeGasBurningGenerator> implements IWireFrameRenderer {

    public RenderLargeGasBurningGenerator(BlockEntityRendererProvider.Context context) {
        super(context, ModelLargeGasBurningGenerator::new);
    }

    @Override
    protected void render(TileEntityLargeGasGenerator tile, float partialTick, PoseStack matrix, MultiBufferSource renderer, int light, int overlayLight, ProfilerFiller profiler) {
        setupRenderer(tile, partialTick, matrix);
        model.render(matrix, renderer, light, overlayLight, false);
        matrix.popPose();
    }

    @Override
    protected String getProfilerSection() {
        return "large_gas_burning_generator";
    }

    @Override
    public boolean shouldRenderOffScreen(TileEntityLargeGasGenerator tile) {
        return true;
    }

    @Override
    public AABB getRenderBoundingBox(TileEntityLargeGasGenerator tile) {
        BlockPos pos = tile.getBlockPos();
//        return AABB.encapsulatingFullBlocks(pos.offset(-2, 0, -2), pos.offset(2, 6, 2));
        return AABB.encapsulatingFullBlocks(pos, pos.above());
    }

    @Override
    public void renderWireFrame(BlockEntity tile, float partialTick, PoseStack matrix, VertexConsumer buffer) {
        if (tile instanceof TileEntityLargeGasGenerator generator) {
            setupRenderer(generator, partialTick, matrix);
            model.renderWireFrame(matrix, buffer);
            matrix.popPose();
        }
    }

    //对模型进行平移和翻转
    private void setupRenderer(TileEntityLargeGasGenerator tile, float partialTick, PoseStack matrix) {
        matrix.pushPose();
        matrix.translate(0.5, 1.5, 0.5);
        MekanismRenderer.rotate(matrix, tile.getDirection(), 0, 180, 90, 270);
        matrix.mulPose(Axis.ZP.rotationDegrees(180));
    }

//    @Override
//    public boolean isCombined() {
//        return true;
//    }
}
