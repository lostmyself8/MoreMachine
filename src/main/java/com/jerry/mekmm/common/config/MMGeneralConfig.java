package com.jerry.mekmm.common.config;

import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.common.util.ValidatorUtils;
import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.value.CachedConfigValue;
import mekanism.common.config.value.CachedLongValue;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class MMGeneralConfig extends BaseMekanismConfig {

    private final ModConfigSpec configSpec;

    public final CachedLongValue conversionMultiplier;

    public final CachedConfigValue<List<? extends String>> duplicatorRecipe;

    MMGeneralConfig() {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        MMConfigTranslations.GENERAL_ENERGY_CONVERSION.applyToBuilder(builder).push("energy_conversion");
        conversionMultiplier = CachedLongValue.define(this, builder, MMConfigTranslations.GENERAL_ENERGY_CONVERSION_MULTIPLIER,
                "conversionMultiplier", 27, 1, Long.MAX_VALUE);
        builder.pop();

        MMConfigTranslations.GENERAL_REPLICATOR_RECIPES.applyToBuilder(builder).push("replicator_recipes");
        duplicatorRecipe = CachedConfigValue.wrap(this, MMConfigTranslations.GENERAL_RECIPES.applyToBuilder(builder)
                .defineListAllowEmpty("replicatorRecipe", ArrayList::new, () -> Mekmm.MOD_ID, e -> e instanceof String list && ValidatorUtils.validateList(list)));
        builder.pop();

        configSpec = builder.build();
    }

    @Override
    public String getFileName() {
        return "general";
    }

    @Override
    public String getTranslation() {
        return "General Config";
    }

    @Override
    public ModConfigSpec getConfigSpec() {
        return configSpec;
    }

    @Override
    public ModConfig.Type getConfigType() {
        return ModConfig.Type.SERVER;
    }
}
