package com.jerry.mekmm.common.config;

import mekanism.common.Mekanism;
import mekanism.common.config.IMekanismConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.event.config.ModConfigEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MMConfig {

    private MMConfig() {
    }

    private static final Map<IConfigSpec, IMekanismConfig> KNOWN_CONFIGS = new HashMap<>();
    public static final MMGeneralConfig general = new MMGeneralConfig();
    public static void registerConfigs(ModContainer modContainer) {
        MMConfigHelper.registerConfig(KNOWN_CONFIGS, modContainer, general);
    }

    public static void onConfigLoad(ModConfigEvent configEvent) {
        MMConfigHelper.onConfigLoad(configEvent, Mekanism.MODID, KNOWN_CONFIGS);
    }

    public static Collection<IMekanismConfig> getConfigs() {
        return Collections.unmodifiableCollection(KNOWN_CONFIGS.values());
    }
}
