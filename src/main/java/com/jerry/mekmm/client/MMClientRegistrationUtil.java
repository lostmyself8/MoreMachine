package com.jerry.mekmm.client;

import com.jerry.mekmm.client.gui.machine.MMGuiElectricMachine;
import com.jerry.mekmm.common.tile.prefab.MMTileEntityElectricMachine;
import mekanism.client.ClientRegistrationUtil;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class MMClientRegistrationUtil {

    //Helper method to register GuiElectricMachine due to generics not being able to be resolved through registerScreen
    @SuppressWarnings("RedundantTypeArguments")
    public static <TILE extends MMTileEntityElectricMachine, C extends MekanismTileContainer<TILE>> void registerElectricScreen(RegisterMenuScreensEvent event,
                                                                                                                                ContainerTypeRegistryObject<C> type) {
        ClientRegistrationUtil.<C, MMGuiElectricMachine<TILE, C>>registerScreen(event, type, MMGuiElectricMachine::new);
    }
}
