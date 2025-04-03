package com.jerry.mekmm;

import com.jerry.meklm.common.capabilities.LMCapabilities;
import com.jerry.meklm.common.config.LMConfig;
import com.jerry.meklm.common.registries.LMBlocks;
import com.jerry.meklm.common.registries.LMContainerTypes;
import com.jerry.meklm.common.registries.LMCreativeTabs;
import com.jerry.meklm.common.registries.LMTileEntityTypes;
import com.jerry.mekmm.common.config.MMConfig;
import com.jerry.mekmm.common.network.MMPacketHandler;
import com.jerry.mekmm.common.recipe.MMRecipeType;
import com.jerry.mekmm.common.registries.*;
import com.mojang.logging.LogUtils;
import mekanism.common.lib.Version;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(Mekmm.MOD_ID)
public class Mekmm {
    public static final String MOD_ID = "mekmm";
    public static final String MOD_ID_LM = "meklm";
    public static final String MOD_NAME = "MekanismMoreMachine";
    private static final Logger LOGGER = LogUtils.getLogger();

    private final MMPacketHandler packetHandler;

    public static Mekmm instance;

    public final Version versionNumber;

    public Mekmm(IEventBus modEventBus, ModContainer modContainer) {
        instance = this;
        //Set our version number to match the neoforge.mods.toml file, which matches the one in our build.gradle
        versionNumber = new Version(modContainer);

        MMConfig.registerConfigs(modContainer);
        LMConfig.registerConfigs(modContainer);
        MMItems.MM_ITEMS.register(modEventBus);
        MMBlocks.MM_BLOCKS.register(modEventBus);
        MMTileEntityTypes.MM_TILE_ENTITY_TYPES.register(modEventBus);
        MMContainerTypes.MM_CONTAINER_TYPES.register(modEventBus);
        MMRecipeType.MM_RECIPE_TYPES.register(modEventBus);
        MMRecipeSerializersInternal.MM_RECIPE_SERIALIZERS.register(modEventBus);
        MMChemicals.MM_CHEMICALS.register(modEventBus);
        MMCreativeTabs.MM_CREATIVE_TABS.register(modEventBus);
        conditionalRegistry(modEventBus);

        packetHandler = new MMPacketHandler(modEventBus, versionNumber);
    }

    public static MMPacketHandler packetHandler() {
        return instance.packetHandler;
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    private static void conditionalRegistry(IEventBus modEventBus){
        LMBlocks.LM_BLOCKS.register(modEventBus);
        LMTileEntityTypes.LM_TILE_ENTITY_TYPES.register(modEventBus);
        LMContainerTypes.LM_CONTAINER_TYPES.register(modEventBus);
        LMCreativeTabs.LM_CREATIVE_TABS.register(modEventBus);
        modEventBus.addListener(LMCapabilities::registerCapabilities);
    }
}
