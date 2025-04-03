package com.jerry.mekmm.client.gui.element.gauge;

import mekanism.common.util.MekanismUtils;
import net.minecraft.resources.ResourceLocation;

public enum MMGaugeOverlay {
    TINY(16, 16, "tiny.png");

    private final int width;
    private final int height;
    private final ResourceLocation barOverlay;

    MMGaugeOverlay(int width, int height, String barOverlay) {
        this.width = width;
        this.height = height;
        this.barOverlay = MekanismUtils.getResource(MekanismUtils.ResourceType.GUI_GAUGE, barOverlay);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ResourceLocation getBarOverlay() {
        return barOverlay;
    }
}
