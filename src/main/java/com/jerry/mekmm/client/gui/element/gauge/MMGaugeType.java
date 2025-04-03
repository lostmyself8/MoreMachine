package com.jerry.mekmm.client.gui.element.gauge;

import mekanism.client.gui.element.gauge.GaugeInfo;
import mekanism.common.tile.component.config.DataType;

public class MMGaugeType {

    public static final MMGaugeType TINY = get(GaugeInfo.STANDARD, MMGaugeOverlay.TINY);

    private final GaugeInfo gaugeInfo;
    private final MMGaugeOverlay gaugeOverlay;

    private MMGaugeType(GaugeInfo gaugeInfo, MMGaugeOverlay gaugeOverlay) {
        this.gaugeInfo = gaugeInfo;
        this.gaugeOverlay = gaugeOverlay;
    }

    public GaugeInfo getGaugeInfo() {
        return gaugeInfo;
    }

    public MMGaugeOverlay getMMGaugeOverlay() {
        return gaugeOverlay;
    }

    public MMGaugeType with(DataType type) {
        GaugeInfo info = GaugeInfo.get(type);
        return info == gaugeInfo ? this : with(info);
    }

    public MMGaugeType with(GaugeInfo info) {
        return new MMGaugeType(info, gaugeOverlay);
    }

    public static MMGaugeType get(GaugeInfo info, MMGaugeOverlay overlay) {
        return new MMGaugeType(info, overlay);
    }
}