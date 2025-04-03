package com.jerry.meklm.client;

import com.jerry.meklm.client.gui.machine.GuiLargeChemicalWasher;
import com.jerry.meklm.client.gui.machine.GuiLargeElectrolyticSeparator;
import com.jerry.meklm.client.gui.machine.GuiLargeGasGenerator;
import com.jerry.meklm.client.gui.machine.GuiLargeHeatGenerator;
import com.jerry.meklm.client.model.LMModelCache;
import com.jerry.meklm.client.model.ModelLargeChemicalWasher;
import com.jerry.meklm.client.model.ModelLargeGasBurningGenerator;
import com.jerry.meklm.client.model.bake.LargeChemicalWasherBakeModel;
import com.jerry.meklm.client.model.bake.LargeElectrolyticSeparatorBakeModel;
import com.jerry.meklm.client.render.item.RenderLargeChemicalWasherItem;
import com.jerry.meklm.client.render.item.RenderLargeGasBurningGeneratorItem;
import com.jerry.meklm.client.render.tileentity.RenderLargeChemicalWasher;
import com.jerry.meklm.client.render.tileentity.RenderLargeElectrolyticSeparator;
import com.jerry.meklm.client.render.tileentity.RenderLargeGasBurningGenerator;
import com.jerry.meklm.client.render.tileentity.RenderLargeHeatGenerator;
import com.jerry.meklm.common.registries.LMBlocks;
import com.jerry.meklm.common.registries.LMContainerTypes;
import com.jerry.meklm.common.registries.LMTileEntityTypes;
import com.jerry.mekmm.Mekmm;
import mekanism.client.ClientRegistration;
import mekanism.client.ClientRegistrationUtil;
import mekanism.client.model.baked.ExtensionBakedModel;
import mekanism.client.render.RenderPropertiesProvider;
import mekanism.client.render.lib.QuadTransformation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = Mekmm.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class LMClientRegistration {

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        // large heat gen requires to be translated up 1 block, so handle the model separately
        // 需要平移得在这处理
        ClientRegistration.addCustomModel(LMBlocks.LARGE_HEAT_GENERATOR, (orig, evt) -> new ExtensionBakedModel.TransformedBakedModel<Void>(orig,
                QuadTransformation.translate(0, 1, 0)));
        ClientRegistration.addCustomModel(LMBlocks.LARGE_ELECTROLYTIC_SEPARATOR, (orig, evt) -> new LargeElectrolyticSeparatorBakeModel(orig));
        ClientRegistration.addCustomModel(LMBlocks.LARGE_CHEMICAL_WASHER, (orig, evt) -> new LargeChemicalWasherBakeModel(orig));
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(LMTileEntityTypes.LARGE_HEAT_GENERATOR.get(), RenderLargeHeatGenerator::new);
        event.registerBlockEntityRenderer(LMTileEntityTypes.LARGE_GAS_BURNING_GENERATOR.get(), RenderLargeGasBurningGenerator::new);
        event.registerBlockEntityRenderer(LMTileEntityTypes.LARGE_ELECTROLYTIC_SEPARATOR.get(), RenderLargeElectrolyticSeparator::new);
        event.registerBlockEntityRenderer(LMTileEntityTypes.LARGE_CHEMICAL_WASHER.get(), RenderLargeChemicalWasher::new);
    }

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModelLargeGasBurningGenerator.LGBG_LAYER, ModelLargeGasBurningGenerator::createLayerDefinition);
        event.registerLayerDefinition(ModelLargeChemicalWasher.LCW_LAYER, ModelLargeChemicalWasher::createLayerDefinition);
    }

    @SubscribeEvent
    public static void registerClientReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(RenderLargeGasBurningGeneratorItem.RENDERER);
        event.registerReloadListener(RenderLargeChemicalWasherItem.RENDERER);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        ClientRegistrationUtil.registerScreen(event, LMContainerTypes.LARGE_HEAT_GENERATOR, GuiLargeHeatGenerator::new);
        ClientRegistrationUtil.registerScreen(event, LMContainerTypes.LARGE_GAS_BURNING_GENERATOR, GuiLargeGasGenerator::new);
        ClientRegistrationUtil.registerScreen(event, LMContainerTypes.LARGE_ELECTROLYTIC_SEPARATOR, GuiLargeElectrolyticSeparator::new);
        ClientRegistrationUtil.registerScreen(event, LMContainerTypes.LARGE_CHEMICAL_WASHER, GuiLargeChemicalWasher::new);
    }

    @SubscribeEvent
    public static void onModelBake(ModelEvent.BakingCompleted event) {
        LMModelCache.INSTANCE.onBake(event);
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new RenderPropertiesProvider.MekRenderProperties(RenderLargeGasBurningGeneratorItem.RENDERER), LMBlocks.LARGE_GAS_BURNING_GENERATOR.asItem());
        event.registerItem(new RenderPropertiesProvider.MekRenderProperties(RenderLargeChemicalWasherItem.RENDERER), LMBlocks.LARGE_CHEMICAL_WASHER.asItem());
        ClientRegistrationUtil.registerBlockExtensions(event, LMBlocks.LM_BLOCKS);
    }
}
