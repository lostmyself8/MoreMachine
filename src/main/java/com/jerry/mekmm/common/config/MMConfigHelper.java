package com.jerry.mekmm.common.config;

import com.jerry.mekmm.Mekmm;
import mekanism.common.config.IMekanismConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;
import java.util.Map;

public class MMConfigHelper {

    private MMConfigHelper() {
    }

    public static final Path CONFIG_DIR = FMLPaths.getOrCreateGameRelativePath(FMLPaths.CONFIGDIR.get().resolve(Mekmm.MOD_NAME));

    /**
     * Creates and register a mod config, and track it so that we can properly clear cached values.
     */
    public static void registerConfig(Map<IConfigSpec, IMekanismConfig> knownConfigs, ModContainer modContainer, IMekanismConfig config) {
        modContainer.registerConfig(config.getConfigType(), config.getConfigSpec(), Mekmm.MOD_NAME + "/" + config.getFileName() + ".toml");
        knownConfigs.put(config.getConfigSpec(), config);
    }

    public static void onConfigLoad(ModConfigEvent event, String modid, Map<IConfigSpec, IMekanismConfig> knownConfigs) {
        //Note: We listen to both the initial load and the reload, to make sure that we fix any accidentally
        // cached values from calls before the initial loading
        ModConfig config = event.getConfig();
        //Make sure it is for the same modid as us
        if (config.getModId().equals(modid)) {
            IMekanismConfig mekanismConfig = knownConfigs.get(config.getSpec());
            if (mekanismConfig != null) {
                mekanismConfig.clearCache(event instanceof ModConfigEvent.Unloading);
            }
        }
    }
}
