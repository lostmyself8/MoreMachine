package com.jerry.meklm.common.config;

import com.jerry.mekmm.Mekmm;
import mekanism.common.config.IConfigTranslation;
import mekanism.common.config.TranslationPreset;
import net.minecraft.Util;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum LMGeneratorsConfigTranslations implements IConfigTranslation {
    SERVER_GENERATOR_GAS("server.generator.large_gas", "Large Gas-Burning Generator", "Settings for configuring Large Gas-Burning Generators", true),
    SERVER_GENERATOR_GAS_TANK_CAPACITY("server.generator.large_gas.tank_capacity", "Tank Capacity", "The capacity in mB of the chemical tank in the Large Gas-Burning Generator.");

    private final String key;
    private final String title;
    private final String tooltip;
    @Nullable
    private final String button;

    LMGeneratorsConfigTranslations(TranslationPreset preset, String type) {
        this(preset.path(type), preset.title(type), preset.tooltip(type));
    }

    LMGeneratorsConfigTranslations(String path, String title, String tooltip) {
        this(path, title, tooltip, false);
    }

    LMGeneratorsConfigTranslations(String path, String title, String tooltip, boolean isSection) {
        this(path, title, tooltip, IConfigTranslation.getSectionTitle(title, isSection));
    }

    LMGeneratorsConfigTranslations(String path, String title, String tooltip, @Nullable String button) {
        this.key = Util.makeDescriptionId("configuration", Mekmm.rl(path));
        this.title = title;
        this.tooltip = tooltip;
        this.button = button;
    }



    @Override
    public String title() {
        return title;
    }

    @Override
    public String tooltip() {
        return tooltip;
    }

    @Override
    public @NotNull String getTranslationKey() {
        return key;
    }

    @Override
    public @Nullable String button() {
        return button;
    }
}
