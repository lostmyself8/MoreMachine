package com.jerry.meklm.client.render.tileentity;

import com.jerry.meklm.client.model.LMModelCache;
import com.jerry.meklm.common.tile.TileEntityLargeHeatGenerator;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.client.render.RenderTickHandler;
import mekanism.client.render.lib.Outlines;
import mekanism.client.render.tileentity.IWireFrameRenderer;
import mekanism.client.render.tileentity.MekanismTileEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@NothingNullByDefault
public class RenderLargeHeatGenerator extends MekanismTileEntityRenderer<TileEntityLargeHeatGenerator> implements IWireFrameRenderer {

    @Nullable
    private static List<Outlines.Line> lines;

    public static void resetCached() {
        lines = null;
    }

    public RenderLargeHeatGenerator(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void renderWireFrame(BlockEntity tile, float partialTick, PoseStack matrix, VertexConsumer buffer) {
        if (tile instanceof TileEntityLargeHeatGenerator generator) {
            if (lines == null) {
                lines = Outlines.extract(LMModelCache.INSTANCE.LARGE_HEAT_GENERATOR.getBakedModel(), null, tile.getLevel().random, ModelData.EMPTY, null);
            }
//            setupRenderer(generator, partialTick, matrix);
            PoseStack.Pose pose = matrix.last();
            RenderTickHandler.renderVertexWireFrame(lines, buffer, pose.pose(), pose.normal());
            matrix.popPose();
        }
    }

    @Override
    protected void render(TileEntityLargeHeatGenerator generator, float partialTick, PoseStack matrix, MultiBufferSource renderer, int light, int overlayLight, ProfilerFiller profiler) {

    }

    @Override
    protected String getProfilerSection() {
        return "largeHeatGenerator";
    }

    @Override
    public boolean isCombined() {
        return true;
    }

    @Override
    public AABB getRenderBoundingBox(TileEntityLargeHeatGenerator tile) {
        BlockPos pos = tile.getBlockPos();
        return AABB.encapsulatingFullBlocks(pos, pos.above());
    }
}
