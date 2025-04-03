package com.jerry.meklm.client.render;

import com.jerry.meklm.client.render.tileentity.RenderLargeElectrolyticSeparator;
import com.jerry.meklm.client.render.tileentity.RenderLargeHeatGenerator;
import com.jerry.mekmm.Mekmm;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent;

@EventBusSubscriber(modid = Mekmm.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class LMRenderer {

    @SubscribeEvent
    public static void onStitch(TextureAtlasStitchedEvent event) {
        RenderLargeHeatGenerator.resetCached();
        RenderLargeElectrolyticSeparator.resetCached();
    }
}
