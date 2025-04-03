package com.jerry.meklm.client.model;

import com.jerry.mekmm.Mekmm;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mekanism.client.model.MekanismJavaModel;
import mekanism.client.model.ModelPartData;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ModelLargeChemicalWasher extends MekanismJavaModel {

    public static final ModelLayerLocation LCW_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Mekmm.MOD_ID, "large_chemical_washer"), "main");
    private static final ResourceLocation LCW_TEXTURE = Mekmm.rl("render/large_chemical_washer.png");

    private static final ModelPartData TANK = new ModelPartData("tank", CubeListBuilder.create()
            .texOffs(0, 111).addBox(2.0F, -43.0F, -23.0F, 21.0F, 38.0F, 23.0F)
            .texOffs(89, 111).addBox(-23.0F, -43.0F, -23.0F, 21.0F, 38.0F, 23.0F)
            .texOffs(0, 52).addBox(-23.0F, -43.0F, 3.0F, 46.0F, 38.0F, 20.0F),
            PartPose.offset(0.0F, 24.0F, 0.0F));

    private static final ModelPartData INPUT_OUTPUT_CONNECT = new ModelPartData("input_output_connect", CubeListBuilder.create()
            .texOffs(185, 28).addBox(-2.0F, -10.0F, -19.0F, 4.0F, 3.0F, 3.0F)
            .texOffs(185, 35).addBox(-2.0F, -16.0F, -19.0F, 4.0F, 3.0F, 3.0F)
            .texOffs(185, 42).addBox(-2.0F, -22.0F, -19.0F, 4.0F, 3.0F, 3.0F)
            .texOffs(64, 186).addBox(-2.0F, -28.0F, -19.0F, 4.0F, 3.0F, 3.0F)
            .texOffs(79, 186).addBox(-2.0F, -34.0F, -19.0F, 4.0F, 3.0F, 3.0F)
            .texOffs(94, 186).addBox(-2.0F, -40.0F, -19.0F, 4.0F, 3.0F, 3.0F)
            .texOffs(169, 190).addBox(-2.0F, -40.0F, -6.0F, 4.0F, 3.0F, 3.0F)
            .texOffs(154, 190).addBox(-2.0F, -34.0F, -6.0F, 4.0F, 3.0F, 3.0F)
            .texOffs(139, 190).addBox(-2.0F, -28.0F, -6.0F, 4.0F, 3.0F, 3.0F)
            .texOffs(124, 190).addBox(-2.0F, -22.0F, -6.0F, 4.0F, 3.0F, 3.0F)
            .texOffs(109, 190).addBox(-2.0F, -16.0F, -6.0F, 4.0F, 3.0F, 3.0F)
            .texOffs(35, 188).addBox(-2.0F, -10.0F, -6.0F, 4.0F, 3.0F, 3.0F),
            PartPose.offset(0.0F, 24.0F, 0.0F));

    private static final ModelPartData WATER_CHEMICAL_CONNECT = new ModelPartData("water_chemical_connect", CubeListBuilder.create()
            .texOffs(133, 52).addBox(-20.0F, -40.0F, 0.0F, 15.0F, 35.0F, 3.0F)
            .texOffs(170, 52).addBox(5.0F, -40.0F, 0.0F, 15.0F, 35.0F, 3.0F),
            PartPose.offset(0.0F, 24.0F, 0.0F));

    private static final ModelPartData FRONT_BUTTON = new ModelPartData("front_button", CubeListBuilder.create()
            .texOffs(52, 197).addBox(-10.0F, -39.0F, -23.5F, 3.0F, 3.0F, 1.0F)
            .texOffs(102, 193).addBox(-10.0F, -34.0F, -23.5F, 2.0F, 2.0F, 1.0F)
            .texOffs(202, 21).addBox(-10.0F, -31.0F, -23.5F, 2.0F, 2.0F, 1.0F)
            .texOffs(52, 202).addBox(-10.0F, -28.0F, -23.5F, 2.0F, 2.0F, 1.0F)
            .texOffs(154, 197).addBox(15.0F, -39.0F, -23.5F, 3.0F, 3.0F, 1.0F)
            .texOffs(59, 202).addBox(15.0F, -31.0F, -23.5F, 2.0F, 2.0F, 1.0F)
            .texOffs(66, 202).addBox(15.0F, -34.0F, -23.5F, 2.0F, 2.0F, 1.0F)
            .texOffs(73, 202).addBox(15.0F, -28.0F, -23.5F, 2.0F, 2.0F, 1.0F),
            PartPose.offset(0.0F, 24.0F, 0.0F));

    private static final ModelPartData TOP_BUTTON_SMALL = new ModelPartData("top_button_small", CubeListBuilder.create()
            .texOffs(26, 191).addBox(18.0F, -44.0F, -19.0F, 2.0F, 1.0F, 2.0F)
            .texOffs(197, 154).addBox(15.0F, -44.0F, -19.0F, 2.0F, 1.0F, 2.0F)
            .texOffs(197, 158).addBox(12.0F, -44.0F, -19.0F, 2.0F, 1.0F, 2.0F)
            .texOffs(197, 162).addBox(-18.0F, -44.0F, -18.0F, 2.0F, 1.0F, 2.0F)
            .texOffs(163, 197).addBox(-18.0F, -44.0F, -14.0F, 2.0F, 1.0F, 2.0F)
            .texOffs(185, 10).addBox(-18.0F, -44.0F, 16.0F, 5.0F, 1.0F, 4.0F)
            .texOffs(115, 197).addBox(-12.0F, -44.0F, 16.0F, 2.0F, 1.0F, 4.0F)
            .texOffs(197, 118).addBox(-9.0F, -44.0F, 16.0F, 2.0F, 1.0F, 4.0F)
            .texOffs(197, 124).addBox(-6.0F, -44.0F, 16.0F, 2.0F, 1.0F, 4.0F)
            .texOffs(128, 197).addBox(-3.0F, -44.0F, 16.0F, 2.0F, 1.0F, 4.0F)
            .texOffs(197, 130).addBox(0.0F, -44.0F, 16.0F, 2.0F, 1.0F, 4.0F),
            PartPose.offset(0.0F, 24.0F, 0.0F));

    private static final ModelPartData TOP_BUTTON_BIG = new ModelPartData("top_button_big", CubeListBuilder.create()
            .texOffs(197, 136).addBox(5.0F, -45.0F, 13.0F, 3.0F, 2.0F, 3.0F)
            .texOffs(141, 197).addBox(11.0F, -45.0F, 13.0F, 3.0F, 2.0F, 3.0F)
            .texOffs(197, 142).addBox(5.0F, -45.0F, -19.0F, 3.0F, 2.0F, 3.0F)
            .texOffs(197, 148).addBox(5.0F, -45.0F, -13.0F, 3.0F, 2.0F, 3.0F),
            PartPose.offset(0.0F, 24.0F, 0.0F));

    private static final ModelPartData VALVE_LEFT = new ModelPartData("valve_left", CubeListBuilder.create()
            .texOffs(18, 200).addBox(-14.0F, -45.0F, -18.0F, 1.0F, 2.0F, 2.0F)
            .texOffs(197, 166).addBox(-14.0F, -45.0F, -19.0F, 3.0F, 2.0F, 1.0F)
            .texOffs(200, 40).addBox(-7.0F, -45.0F, -18.0F, 1.0F, 2.0F, 2.0F)
            .texOffs(172, 197).addBox(-9.0F, -45.0F, -19.0F, 3.0F, 2.0F, 1.0F)
            .texOffs(200, 45).addBox(-7.0F, -45.0F, -14.0F, 1.0F, 2.0F, 2.0F)
            .texOffs(197, 191).addBox(-9.0F, -45.0F, -12.0F, 3.0F, 2.0F, 1.0F)
            .texOffs(181, 200).addBox(-14.0F, -45.0F, -14.0F, 1.0F, 2.0F, 2.0F)
            .texOffs(197, 195).addBox(-14.0F, -45.0F, -12.0F, 3.0F, 2.0F, 1.0F)
            .texOffs(197, 199).addBox(-11.0F, -44.0F, -16.0F, 2.0F, 1.0F, 2.0F)
            .texOffs(185, 16).addBox(-12.0F, -45.0F, -17.0F, 4.0F, 1.0F, 4.0F),
            PartPose.offset(0.0F, 24.0F, 0.0F));

    private static final ModelPartData VALVE_RIGHT = new ModelPartData("valve_right", CubeListBuilder.create()
            .texOffs(188, 200).addBox(-14.0F, -45.0F, -18.0F, 1.0F, 2.0F, 2.0F)
            .texOffs(0, 200).addBox(-14.0F, -45.0F, -19.0F, 3.0F, 2.0F, 1.0F)
            .texOffs(163, 201).addBox(-7.0F, -45.0F, -18.0F, 1.0F, 2.0F, 2.0F)
            .texOffs(9, 200).addBox(-9.0F, -45.0F, -19.0F, 3.0F, 2.0F, 1.0F)
            .texOffs(170, 201).addBox(-7.0F, -45.0F, -14.0F, 1.0F, 2.0F, 2.0F)
            .texOffs(200, 28).addBox(-9.0F, -45.0F, -12.0F, 3.0F, 2.0F, 1.0F)
            .texOffs(202, 16).addBox(-14.0F, -45.0F, -14.0F, 1.0F, 2.0F, 2.0F)
            .texOffs(200, 32).addBox(-14.0F, -45.0F, -12.0F, 3.0F, 2.0F, 1.0F)
            .texOffs(200, 36).addBox(-11.0F, -44.0F, -16.0F, 2.0F, 1.0F, 2.0F)
            .texOffs(185, 22).addBox(-12.0F, -45.0F, -17.0F, 4.0F, 1.0F, 4.0F),
            PartPose.offset(26.0F, 24.0F, 5.0F));

    private static final ModelPartData PIPE_H = new ModelPartData("pipe_h", CubeListBuilder.create()
            .texOffs(50, 188).addBox(5.0F, -48.0F, -7.0F, 3.0F, 5.0F, 3.0F)
            .texOffs(190, 173).addBox(5.0F, -48.0F, 7.0F, 3.0F, 5.0F, 3.0F)
            .texOffs(190, 182).addBox(11.0F, -48.0F, 7.0F, 3.0F, 5.0F, 3.0F)
            .texOffs(0, 191).addBox(17.0F, -48.0F, 7.0F, 3.0F, 5.0F, 3.0F)
            .texOffs(13, 191).addBox(11.0F, -48.0F, -5.0F, 3.0F, 5.0F, 3.0F)
            .texOffs(184, 191).addBox(17.0F, -48.0F, -5.0F, 3.0F, 5.0F, 3.0F)
            .texOffs(63, 193).addBox(-8.0F, -48.0F, -9.0F, 3.0F, 5.0F, 3.0F)
            .texOffs(76, 193).addBox(-8.0F, -48.0F, -9.0F, 3.0F, 5.0F, 3.0F)
            .texOffs(26, 195).addBox(-14.0F, -48.0F, -9.0F, 3.0F, 5.0F, 3.0F)
            .texOffs(89, 193).addBox(-20.0F, -48.0F, -9.0F, 3.0F, 5.0F, 3.0F)
            .texOffs(39, 197).addBox(-20.0F, -48.0F, 8.0F, 3.0F, 5.0F, 3.0F)
            .texOffs(102, 197).addBox(-14.0F, -48.0F, 8.0F, 3.0F, 5.0F, 3.0F)
            .texOffs(197, 109).addBox(-8.0F, -48.0F, 8.0F, 3.0F, 5.0F, 3.0F),
            PartPose.offset(0.0F, 24.0F, 0.0F));

    private static final ModelPartData PIPE_V = new ModelPartData("pipe_v", CubeListBuilder.create()
            .texOffs(133, 91).addBox(-8.0F, -48.0F, -6.0F, 3.0F, 3.0F, 14.0F)
            .texOffs(168, 91).addBox(-14.0F, -48.0F, -6.0F, 3.0F, 3.0F, 14.0F)
            .texOffs(0, 173).addBox(-20.0F, -48.0F, -6.0F, 3.0F, 3.0F, 14.0F)
            .texOffs(35, 173).addBox(5.0F, -48.0F, -4.0F, 3.0F, 3.0F, 11.0F)
            .texOffs(64, 173).addBox(11.0F, -48.0F, -2.0F, 3.0F, 3.0F, 9.0F)
            .texOffs(89, 173).addBox(17.0F, -48.0F, -2.0F, 3.0F, 3.0F, 9.0F),
            PartPose.offset(0.0F, 24.0F, 0.0F));

    private static final ModelPartData CHEMICAL_PORT_O = new ModelPartData("chemical_port_o", CubeListBuilder.create()
            .texOffs(114, 173).addBox(23.0F, -12.0F, -20.0F, 1.0F, 8.0F, 8.0F)
            .texOffs(133, 173).addBox(23.0F, -28.0F, -20.0F, 1.0F, 8.0F, 8.0F),
            PartPose.offset(0.0F, 24.0F, 0.0F));

    private static final ModelPartData CHEMICAL_PORT_I = new ModelPartData("chemical_port_i", CubeListBuilder.create()
            .texOffs(152, 173).addBox(-24.0F, -12.0F, -20.0F, 1.0F, 8.0F, 8.0F)
            .texOffs(171, 173).addBox(-24.0F, -28.0F, -20.0F, 1.0F, 8.0F, 8.0F),
            PartPose.offset(0.0F, 24.0F, 0.0F));

    private static final ModelPartData WATER_PORT = new ModelPartData("water_port", CubeListBuilder.create()
            .texOffs(178, 109).addBox(-24.0F, -12.0F, 12.0F, 1.0F, 8.0F, 8.0F)
            .texOffs(178, 126).addBox(23.0F, -12.0F, 12.0F, 1.0F, 8.0F, 8.0F)
            .texOffs(178, 143).addBox(12.0F, -12.0F, 23.0F, 8.0F, 8.0F, 1.0F)
            .texOffs(178, 153).addBox(-20.0F, -12.0F, 23.0F, 8.0F, 8.0F, 1.0F),
            PartPose.offset(0.0F, 24.0F, 0.0F));

    private static final ModelPartData POWER_PORT = new ModelPartData("power_port", CubeListBuilder.create()
            .texOffs(178, 163).addBox(9.0F, -8.0F, -1.0F, 8.0F, 8.0F, 1.0F)
            .texOffs(185, 0).addBox(9.0F, -24.0F, -1.0F, 8.0F, 8.0F, 1.0F),
            PartPose.offset(-13.0F, 20.0F, 24.0F));

    private static final ModelPartData BB_MAIN = new ModelPartData("bb_main", CubeListBuilder.create()
            .texOffs(0, 0).addBox(-23.0F, -5.0F, -23.0F, 46.0F, 5.0F, 46.0F),
            PartPose.offset(0.0F, 24.0F, 0.0F));

    public static LayerDefinition createLayerDefinition() {
        return createLayerDefinition(256, 256, TANK, INPUT_OUTPUT_CONNECT, WATER_CHEMICAL_CONNECT, FRONT_BUTTON, TOP_BUTTON_SMALL, TOP_BUTTON_BIG, VALVE_LEFT, VALVE_RIGHT,
                PIPE_H, PIPE_V, CHEMICAL_PORT_O, CHEMICAL_PORT_I, WATER_PORT, POWER_PORT, BB_MAIN);
    }

    private final RenderType RENDER_TYPE = renderType(LCW_TEXTURE);
    private final List<ModelPart> parts;

    public ModelLargeChemicalWasher(EntityModelSet entityModelSet) {
        super(RenderType::entitySolid);
        ModelPart root = entityModelSet.bakeLayer(LCW_LAYER);
        parts = getRenderableParts(root, TANK, INPUT_OUTPUT_CONNECT, WATER_CHEMICAL_CONNECT, FRONT_BUTTON, TOP_BUTTON_SMALL, TOP_BUTTON_BIG, VALVE_LEFT, VALVE_RIGHT,
                PIPE_H, PIPE_V, CHEMICAL_PORT_O, CHEMICAL_PORT_I, WATER_PORT, POWER_PORT, BB_MAIN);
    }

    public void render(@NotNull PoseStack matrix, @NotNull MultiBufferSource renderer, int light, int overlayLight, boolean hasEffect) {
        renderToBuffer(matrix, getVertexConsumer(renderer, RENDER_TYPE, hasEffect), light, overlayLight, 0xFFFFFFFF);
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int light, int overlayLight, int color) {
        renderPartsToBuffer(parts, poseStack, vertexConsumer, light, overlayLight, color);
    }

    public void renderWireFrame(PoseStack matrix, VertexConsumer vertexBuilder) {
        renderPartsAsWireFrame(parts, matrix, vertexBuilder);
    }
}
