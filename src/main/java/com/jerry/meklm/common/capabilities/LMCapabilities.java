package com.jerry.meklm.common.capabilities;

import mekanism.common.tile.TileEntityBoundingBlock;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import static mekanism.common.capabilities.Capabilities.*;

public class LMCapabilities {

    private LMCapabilities() {

    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        TileEntityBoundingBlock.proxyCapability(event, FLUID.block());
        TileEntityBoundingBlock.proxyCapability(event, CHEMICAL.block());
        TileEntityBoundingBlock.proxyCapability(event, HEAT);
    }
}
