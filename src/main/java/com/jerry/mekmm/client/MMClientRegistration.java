package com.jerry.mekmm.client;

import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.client.gui.machine.*;
import com.jerry.mekmm.common.registries.MMContainerTypes;
import mekanism.client.ClientRegistrationUtil;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = Mekmm.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class MMClientRegistration {

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        ClientRegistrationUtil.registerScreen(event, MMContainerTypes.RECYCLER, GuiRecycler::new);
        ClientRegistrationUtil.registerScreen(event, MMContainerTypes.PLANTING_STATION, GuiPlantingStation::new);
        MMClientRegistrationUtil.registerElectricScreen(event, MMContainerTypes.CNC_STAMPER);
        MMClientRegistrationUtil.registerElectricScreen(event, MMContainerTypes.CNC_LATHE);
        MMClientRegistrationUtil.registerElectricScreen(event, MMContainerTypes.CNC_ROLLING_MILL);
        ClientRegistrationUtil.registerScreen(event, MMContainerTypes.REPLICATOR, GuiReplicator::new);
        ClientRegistrationUtil.registerScreen(event, MMContainerTypes.AMBIENT_GAS_COLLECTOR, GuiAmbientGasCollector::new);

        ClientRegistrationUtil.registerScreen(event, MMContainerTypes.MM_FACTORY, GuiMMFactory::new);
    }
}
