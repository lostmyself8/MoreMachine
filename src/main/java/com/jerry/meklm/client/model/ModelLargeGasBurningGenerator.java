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

public class ModelLargeGasBurningGenerator extends MekanismJavaModel {

	public static final ModelLayerLocation LGBG_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Mekmm.MOD_ID, "large_gas_burning_generator"), "main");
	private static final ResourceLocation LGBG_TEXTURE = Mekmm.rl("render/large_gas_burning_generator.png");

	private static final ModelPartData BODY = new ModelPartData("body", CubeListBuilder.create()
			.texOffs(0, 54).addBox(-21.0F, -9.0F, -21.0F, 42.0F, 28.0F, 42.0F)
			.texOffs(0, 125).addBox(-16.0F, -18.0F, -16.0F, 32.0F, 9.0F, 32.0F)
			.texOffs(129, 125).addBox(-12.0F, -23.0F, -12.0F, 24.0F, 5.0F, 24.0F),
			PartPose.ZERO);

	private static final ModelPartData FOUR_TANK = new ModelPartData("four_tank", CubeListBuilder.create()
			.texOffs(129, 155).addBox(12.0F, -20.0F, 12.0F, 11.0F, 39.0F, 11.0F)
			.texOffs(45, 167).addBox(12.0F, -20.0F, -23.0F, 11.0F, 39.0F, 11.0F)
			.texOffs(0, 167).addBox(-23.0F, -20.0F, -23.0F, 11.0F, 39.0F, 11.0F)
			.texOffs(169, 54).addBox(-23.0F, -20.0F, 12.0F, 11.0F, 39.0F, 11.0F),
			PartPose.ZERO);

	private static final ModelPartData SIDE = new ModelPartData("side", CubeListBuilder.create()
			.texOffs(231, 48).addBox(21.0F, -21.0F, 16.0F, 1.0F, 1.0F, 3.0F)
			.texOffs(231, 53).addBox(13.0F, -21.0F, 16.0F, 1.0F, 1.0F, 3.0F)
			.texOffs(81, 236).addBox(16.0F, -21.0F, 13.0F, 3.0F, 1.0F, 1.0F)
			.texOffs(90, 236).addBox(16.0F, -21.0F, 21.0F, 3.0F, 1.0F, 1.0F)
			.texOffs(36, 230).addBox(21.0F, -21.0F, -19.0F, 1.0F, 1.0F, 3.0F)
			.texOffs(45, 230).addBox(13.0F, -21.0F, -19.0F, 1.0F, 1.0F, 3.0F)
			.texOffs(207, 235).addBox(16.0F, -21.0F, -22.0F, 3.0F, 1.0F, 1.0F)
			.texOffs(216, 235).addBox(16.0F, -21.0F, -14.0F, 3.0F, 1.0F, 1.0F)
			.texOffs(231, 88).addBox(-14.0F, -21.0F, -19.0F, 1.0F, 1.0F, 3.0F)
			.texOffs(90, 231).addBox(-22.0F, -21.0F, -19.0F, 1.0F, 1.0F, 3.0F)
			.texOffs(225, 236).addBox(-19.0F, -21.0F, -22.0F, 3.0F, 1.0F, 1.0F)
			.texOffs(234, 236).addBox(-19.0F, -21.0F, -14.0F, 3.0F, 1.0F, 1.0F)
			.texOffs(231, 63).addBox(-14.0F, -21.0F, 16.0F, 1.0F, 1.0F, 3.0F)
			.texOffs(231, 68).addBox(-22.0F, -21.0F, 16.0F, 1.0F, 1.0F, 3.0F)
			.texOffs(99, 236).addBox(-19.0F, -21.0F, 13.0F, 3.0F, 1.0F, 1.0F)
			.texOffs(108, 236).addBox(-19.0F, -21.0F, 21.0F, 3.0F, 1.0F, 1.0F),
			PartPose.ZERO);

	private static final ModelPartData CORNER_PIECE = new ModelPartData("corner_piece", CubeListBuilder.create()
			.texOffs(54, 230).addBox(19.0F, -23.0F, 13.0F, 3.0F, 3.0F, 1.0F)
			.texOffs(162, 228).addBox(21.0F, -23.0F, 13.0F, 1.0F, 3.0F, 3.0F)
			.texOffs(63, 230).addBox(13.0F, -23.0F, 13.0F, 3.0F, 3.0F, 1.0F)
			.texOffs(171, 228).addBox(13.0F, -23.0F, 13.0F, 1.0F, 3.0F, 3.0F)
			.texOffs(72, 230).addBox(13.0F, -23.0F, 21.0F, 3.0F, 3.0F, 1.0F)
			.texOffs(180, 228).addBox(13.0F, -23.0F, 19.0F, 1.0F, 3.0F, 3.0F)
			.texOffs(231, 43).addBox(19.0F, -23.0F, 21.0F, 3.0F, 3.0F, 1.0F)
			.texOffs(189, 228).addBox(21.0F, -23.0F, 19.0F, 1.0F, 3.0F, 3.0F)
			.texOffs(27, 230).addBox(19.0F, -23.0F, -22.0F, 3.0F, 3.0F, 1.0F)
			.texOffs(153, 228).addBox(21.0F, -23.0F, -22.0F, 1.0F, 3.0F, 3.0F)
			.texOffs(225, 231).addBox(13.0F, -23.0F, -22.0F, 3.0F, 3.0F, 1.0F)
			.texOffs(229, 36).addBox(13.0F, -23.0F, -22.0F, 1.0F, 3.0F, 3.0F)
			.texOffs(108, 231).addBox(13.0F, -23.0F, -14.0F, 3.0F, 3.0F, 1.0F)
			.texOffs(229, 29).addBox(13.0F, -23.0F, -16.0F, 1.0F, 3.0F, 3.0F)
			.texOffs(99, 231).addBox(19.0F, -23.0F, -14.0F, 3.0F, 3.0F, 1.0F)
			.texOffs(229, 22).addBox(21.0F, -23.0F, -16.0F, 1.0F, 3.0F, 3.0F)
			.texOffs(231, 73).addBox(-16.0F, -23.0F, -22.0F, 3.0F, 3.0F, 1.0F)
			.texOffs(207, 228).addBox(-14.0F, -23.0F, -22.0F, 1.0F, 3.0F, 3.0F)
			.texOffs(231, 78).addBox(-22.0F, -23.0F, -22.0F, 3.0F, 3.0F, 1.0F)
			.texOffs(216, 228).addBox(-22.0F, -23.0F, -22.0F, 1.0F, 3.0F, 3.0F)
			.texOffs(81, 231).addBox(-22.0F, -23.0F, -14.0F, 3.0F, 3.0F, 1.0F)
			.texOffs(229, 8).addBox(-22.0F, -23.0F, -16.0F, 1.0F, 3.0F, 3.0F)
			.texOffs(231, 83).addBox(-16.0F, -23.0F, -14.0F, 3.0F, 3.0F, 1.0F)
			.texOffs(229, 15).addBox(-14.0F, -23.0F, -16.0F, 1.0F, 3.0F, 3.0F)
			.texOffs(231, 58).addBox(-16.0F, -23.0F, 13.0F, 3.0F, 3.0F, 1.0F)
			.texOffs(198, 228).addBox(-14.0F, -23.0F, 13.0F, 1.0F, 3.0F, 3.0F)
			.texOffs(234, 231).addBox(-22.0F, -23.0F, 13.0F, 3.0F, 3.0F, 1.0F)
			.texOffs(18, 230).addBox(-22.0F, -23.0F, 13.0F, 1.0F, 3.0F, 3.0F)
			.texOffs(233, 98).addBox(-22.0F, -23.0F, 21.0F, 3.0F, 3.0F, 1.0F)
			.texOffs(9, 230).addBox(-22.0F, -23.0F, 19.0F, 1.0F, 3.0F, 3.0F)
			.texOffs(233, 93).addBox(-16.0F, -23.0F, 21.0F, 3.0F, 3.0F, 1.0F)
			.texOffs(0, 230).addBox(-14.0F, -23.0F, 19.0F, 1.0F, 3.0F, 3.0F),
			PartPose.ZERO);

	private static final ModelPartData VALVE = new ModelPartData("valve", CubeListBuilder.create()
			.texOffs(227, 152).addBox(16.0F, -47.0F, 16.0F, 3.0F, 3.0F, 3.0F)
			.texOffs(158, 206).addBox(16.0F, -47.0F, -19.0F, 3.0F, 3.0F, 3.0F)
			.texOffs(227, 166).addBox(-19.0F, -47.0F, -19.0F, 3.0F, 3.0F, 3.0F)
			.texOffs(227, 159).addBox(-19.0F, -47.0F, 16.0F, 3.0F, 3.0F, 3.0F)
			.texOffs(63, 235).addBox(15.0F, -47.0F, 15.0F, 2.0F, 1.0F, 2.0F)
			.texOffs(72, 235).addBox(15.0F, -47.0F, 18.0F, 2.0F, 1.0F, 2.0F)
			.texOffs(117, 235).addBox(18.0F, -47.0F, 18.0F, 2.0F, 1.0F, 2.0F)
			.texOffs(126, 235).addBox(18.0F, -47.0F, 15.0F, 2.0F, 1.0F, 2.0F)
			.texOffs(135, 235).addBox(-20.0F, -47.0F, 15.0F, 2.0F, 1.0F, 2.0F)
			.texOffs(144, 235).addBox(-20.0F, -47.0F, 18.0F, 2.0F, 1.0F, 2.0F)
			.texOffs(153, 235).addBox(-17.0F, -47.0F, 18.0F, 2.0F, 1.0F, 2.0F)
			.texOffs(162, 235).addBox(-17.0F, -47.0F, 15.0F, 2.0F, 1.0F, 2.0F)
			.texOffs(171, 235).addBox(-20.0F, -47.0F, -20.0F, 2.0F, 1.0F, 2.0F)
			.texOffs(180, 235).addBox(-20.0F, -47.0F, -17.0F, 2.0F, 1.0F, 2.0F)
			.texOffs(189, 235).addBox(-17.0F, -47.0F, -17.0F, 2.0F, 1.0F, 2.0F)
			.texOffs(198, 235).addBox(-17.0F, -47.0F, -20.0F, 2.0F, 1.0F, 2.0F)
			.texOffs(27, 235).addBox(15.0F, -47.0F, -20.0F, 2.0F, 1.0F, 2.0F)
			.texOffs(36, 235).addBox(15.0F, -47.0F, -17.0F, 2.0F, 1.0F, 2.0F)
			.texOffs(45, 235).addBox(18.0F, -47.0F, -17.0F, 2.0F, 1.0F, 2.0F)
			.texOffs(54, 235).addBox(18.0F, -47.0F, -20.0F, 2.0F, 1.0F, 2.0F),
			PartPose.offset(0.0F, 24.0F, 0.0F));

	private static final ModelPartData CHEMICAL_PORT_RIGHT = new ModelPartData("chemical_port_right", CubeListBuilder.create()
			.texOffs(90, 184).addBox(23.0F, -4.0F, -4.0F, 1.0F, 8.0F, 8.0F)
			.texOffs(158, 215).addBox(21.0F, -3.0F, -3.0F, 2.0F, 6.0F, 6.0F)
			.texOffs(109, 184).addBox(23.0F, 12.0F, -4.0F, 1.0F, 8.0F, 8.0F)
			.texOffs(175, 215).addBox(21.0F, 13.0F, -3.0F, 2.0F, 6.0F, 6.0F),
			PartPose.ZERO);

	private static final ModelPartData CHEMICAL_PORT_LEFT = new ModelPartData("chemical_port_left", CubeListBuilder.create()
			.texOffs(193, 25).addBox(-24.0F, -4.0F, -4.0F, 1.0F, 8.0F, 8.0F)
			.texOffs(209, 215).addBox(-23.0F, -3.0F, -3.0F, 2.0F, 6.0F, 6.0F)
			.texOffs(193, 8).addBox(-24.0F, 12.0F, -4.0F, 1.0F, 8.0F, 8.0F)
			.texOffs(192, 215).addBox(-23.0F, 13.0F, -3.0F, 2.0F, 6.0F, 6.0F),
			PartPose.ZERO);

	private static final ModelPartData CHEMICAL_PORT_BACK = new ModelPartData("chemical_port_back", CubeListBuilder.create()
			.texOffs(214, 94).addBox(-4.0F, -4.0F, 23.0F, 8.0F, 8.0F, 1.0F)
			.texOffs(225, 195).addBox(-3.0F, -3.0F, 21.0F, 6.0F, 6.0F, 2.0F)
			.texOffs(193, 42).addBox(-4.0F, 12.0F, 23.0F, 8.0F, 8.0F, 1.0F)
			.texOffs(222, 116).addBox(-3.0F, 13.0F, 21.0F, 6.0F, 6.0F, 2.0F),
			PartPose.ZERO);

	private static final ModelPartData GAUGE = new ModelPartData("gauge", CubeListBuilder.create()
			.texOffs(225, 204).addBox(-9.0F, -31.0F, -21.5F, 7.0F, 7.0F, 1.0F)
			.texOffs(226, 125).addBox(2.0F, -31.0F, -21.5F, 7.0F, 7.0F, 1.0F)
			.texOffs(226, 134).addBox(2.0F, -22.0F, -21.5F, 7.0F, 7.0F, 1.0F)
			.texOffs(226, 143).addBox(-9.0F, -22.0F, -21.5F, 7.0F, 7.0F, 1.0F)
			.texOffs(226, 213).addBox(-9.0F, -13.0F, -21.5F, 7.0F, 7.0F, 1.0F)
			.texOffs(226, 222).addBox(2.0F, -13.0F, -21.5F, 7.0F, 7.0F, 1.0F)
			.texOffs(0, 237).addBox(6.0F, -40.0F, -16.5F, 2.0F, 2.0F, 1.0F)
			.texOffs(7, 237).addBox(1.0F, -40.0F, -16.5F, 2.0F, 2.0F, 1.0F)
			.texOffs(14, 237).addBox(-2.0F, -40.0F, -16.5F, 2.0F, 2.0F, 1.0F)
			.texOffs(238, 8).addBox(-5.0F, -40.0F, -16.5F, 2.0F, 2.0F, 1.0F)
			.texOffs(238, 12).addBox(-8.0F, -40.0F, -16.5F, 2.0F, 2.0F, 1.0F),
			PartPose.offset(0.0F, 24.0F, 0.0F));

	private static final ModelPartData PASTER = new ModelPartData("paster", CubeListBuilder.create()
			.texOffs(0, 218).addBox(-6.0F, -10.0F, -0.5F, 7.0F, 10.0F, 1.0F)
			.texOffs(17, 218).addBox(-6.0F, -23.0F, -0.5F, 7.0F, 10.0F, 1.0F)
			.texOffs(119, 224).addBox(-6.0F, -35.0F, -0.5F, 7.0F, 9.0F, 1.0F)
			.texOffs(136, 224).addBox(-41.0F, -35.0F, -0.5F, 7.0F, 9.0F, 1.0F)
			.texOffs(51, 218).addBox(-41.0F, -23.0F, -0.5F, 7.0F, 10.0F, 1.0F)
			.texOffs(34, 218).addBox(-41.0F, -10.0F, -0.5F, 7.0F, 10.0F, 1.0F)
			.texOffs(222, 104).addBox(-6.0F, -10.0F, 45.5F, 7.0F, 10.0F, 1.0F)
			.texOffs(102, 219).addBox(-6.0F, -23.0F, 45.5F, 7.0F, 10.0F, 1.0F)
			.texOffs(225, 184).addBox(-6.0F, -35.0F, 45.5F, 7.0F, 9.0F, 1.0F)
			.texOffs(225, 173).addBox(-41.0F, -35.0F, 45.5F, 7.0F, 9.0F, 1.0F)
			.texOffs(85, 219).addBox(-41.0F, -23.0F, 45.5F, 7.0F, 10.0F, 1.0F)
			.texOffs(68, 218).addBox(-41.0F, -10.0F, 45.5F, 7.0F, 10.0F, 1.0F)
			.texOffs(191, 197).addBox(-43.5F, -10.0F, 37.0F, 1.0F, 10.0F, 7.0F)
			.texOffs(174, 197).addBox(-43.5F, -23.0F, 37.0F, 1.0F, 10.0F, 7.0F)
			.texOffs(212, 26).addBox(-43.5F, -35.0F, 37.0F, 1.0F, 9.0F, 7.0F)
			.texOffs(107, 201).addBox(-43.5F, -10.0F, 2.0F, 1.0F, 10.0F, 7.0F)
			.texOffs(90, 201).addBox(-43.5F, -23.0F, 2.0F, 1.0F, 10.0F, 7.0F)
			.texOffs(214, 43).addBox(-43.5F, -35.0F, 2.0F, 1.0F, 9.0F, 7.0F)
			.texOffs(214, 77).addBox(2.5F, -35.0F, 2.0F, 1.0F, 9.0F, 7.0F)
			.texOffs(212, 8).addBox(2.5F, -23.0F, 2.0F, 1.0F, 10.0F, 7.0F)
			.texOffs(208, 197).addBox(2.5F, -10.0F, 2.0F, 1.0F, 10.0F, 7.0F)
			.texOffs(214, 60).addBox(2.5F, -35.0F, 37.0F, 1.0F, 9.0F, 7.0F)
			.texOffs(141, 206).addBox(2.5F, -23.0F, 37.0F, 1.0F, 10.0F, 7.0F)
			.texOffs(124, 206).addBox(2.5F, -10.0F, 37.0F, 1.0F, 10.0F, 7.0F),
			PartPose.offset(20.0F, 17.0F, -23.0F));

	private static final ModelPartData R41 = new ModelPartData("4_r1", CubeListBuilder.create()
			.texOffs(174, 164).addBox(-12.0F, -6.0F, -2.0F, 24.0F, 6.0F, 2.0F),
			PartPose.offsetAndRotation(16.0F, -18.0F, 0.0F, 0.7418F, 1.5708F, 0.0F));

	private static final ModelPartData R42 = new ModelPartData("4_r2", CubeListBuilder.create()
			.texOffs(193, 0).addBox(-12.0F, -6.0F, -1.0F, 24.0F, 6.0F, 1.0F),
			PartPose.offsetAndRotation(21.0F, -9.0F, 0.0F, 1.0036F, 1.5708F, 0.0F));

	private static final ModelPartData R31 = new ModelPartData("3_r1", CubeListBuilder.create()
			.texOffs(174, 155).addBox(-12.0F, -6.0F, -2.0F, 24.0F, 6.0F, 2.0F),
			PartPose.offsetAndRotation(-16.0F, -18.0F, 0.0F, 0.7418F, -1.5708F, 0.0F));

	private static final ModelPartData R32 = new ModelPartData("3_r2", CubeListBuilder.create()
			.texOffs(174, 189).addBox(-12.0F, -6.0F, -1.0F, 24.0F, 6.0F, 1.0F),
			PartPose.offsetAndRotation(-21.0F, -9.0F, 0.0F, 1.0036F, -1.5708F, 0.0F));

	private static final ModelPartData R21 = new ModelPartData("2_r1", CubeListBuilder.create()
			.texOffs(169, 114).addBox(-12.0F, -6.0F, -2.0F, 24.0F, 6.0F, 2.0F),
			PartPose.offsetAndRotation(0.0F, -18.0F, 16.0F, 0.7418F, 0.0F, 0.0F));

	private static final ModelPartData R22 = new ModelPartData("2_r2", CubeListBuilder.create()
			.texOffs(174, 181).addBox(-12.0F, -6.0F, -1.0F, 24.0F, 6.0F, 1.0F),
			PartPose.offsetAndRotation(0.0F, -9.0F, 21.0F, 1.0036F, 0.0F, 0.0F));

	private static final ModelPartData R11 = new ModelPartData("1_r1", CubeListBuilder.create()
			.texOffs(90, 167).addBox(-6.0F, -5.0F, 0.0F, 18.0F, 5.0F, 1.0F),
			PartPose.offsetAndRotation(-3.0F, -10.0F, -20.0F, -1.0036F, 0.0F, 0.0F));

	private static final ModelPartData R12 = new ModelPartData("1_r2", CubeListBuilder.create()
			.texOffs(169, 105).addBox(-12.0F, -6.0F, 0.0F, 24.0F, 6.0F, 2.0F),
			PartPose.offsetAndRotation(0.0F, -18.0F, -16.0F, -0.7418F, 0.0F, 0.0F));

	private static final ModelPartData R13 = new ModelPartData("1_r3", CubeListBuilder.create()
			.texOffs(174, 173).addBox(-12.0F, -6.0F, 0.0F, 24.0F, 6.0F, 1.0F),
			PartPose.offsetAndRotation(0.0F, -9.0F, -21.0F, -1.0036F, 0.0F, 0.0F));

	private static final ModelPartData BB_MAIN = new ModelPartData("bb_main", CubeListBuilder.create()
			.texOffs(0, 0).addBox(-24.0F, -5.0F, -24.0F, 48.0F, 5.0F, 48.0F)
			.texOffs(90, 174).addBox(-4.0F, -48.0F, -4.0F, 8.0F, 1.0F, 8.0F),
			PartPose.offset(0.0F, 24.0F, 0.0F));

	public static LayerDefinition createLayerDefinition() {
		return createLayerDefinition(256, 256, BODY, FOUR_TANK, SIDE, CORNER_PIECE, VALVE, CHEMICAL_PORT_RIGHT, CHEMICAL_PORT_LEFT, CHEMICAL_PORT_BACK,
				GAUGE, PASTER, R41, R42, R31, R32, R21, R22, R11, R12, R13, BB_MAIN);
	}

	private final RenderType RENDER_TYPE = renderType(LGBG_TEXTURE);
	private final List<ModelPart> parts;

	public ModelLargeGasBurningGenerator(EntityModelSet entityModelSet) {
		super(RenderType::entitySolid);
		ModelPart root = entityModelSet.bakeLayer(LGBG_LAYER);
		parts = getRenderableParts(root, BODY, FOUR_TANK, SIDE, CORNER_PIECE, VALVE, CHEMICAL_PORT_RIGHT, CHEMICAL_PORT_LEFT, CHEMICAL_PORT_BACK,
				GAUGE, PASTER, R41, R42, R31, R32, R21, R22, R11, R12, R13, BB_MAIN);
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