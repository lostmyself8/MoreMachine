package com.jerry.meklm.common.config;

import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.common.config.MMConfigHelper;
import mekanism.common.config.IMekanismConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.event.config.ModConfigEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LMConfig {

    private LMConfig() {
    }

    private static final Map<IConfigSpec, IMekanismConfig> KNOWN_CONFIGS = new HashMap<>();
    public static final LMGeneratorsConfig generators = new LMGeneratorsConfig();

    public static void registerConfigs(ModContainer modContainer) {
        MMConfigHelper.registerConfig(KNOWN_CONFIGS, modContainer, generators);
    }

    public static void onConfigLoad(ModConfigEvent configEvent) {
        MMConfigHelper.onConfigLoad(configEvent, Mekmm.MOD_ID, KNOWN_CONFIGS);
    }

    public static Collection<IMekanismConfig> getConfigs() {
        return Collections.unmodifiableCollection(KNOWN_CONFIGS.values());
    }
}
