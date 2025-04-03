package com.jerry.meklm.common.config;

import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.value.CachedLongValue;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.fluids.FluidType;

public class LMGeneratorsConfig extends BaseMekanismConfig {

    private final ModConfigSpec configSpec;

    public final CachedLongValue lgbgTankCapacity;

    public LMGeneratorsConfig() {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        LMGeneratorsConfigTranslations.SERVER_GENERATOR_GAS.applyToBuilder(builder).push("large_gas_generator");
        lgbgTankCapacity = CachedLongValue.wrap(this, LMGeneratorsConfigTranslations.SERVER_GENERATOR_GAS_TANK_CAPACITY.applyToBuilder(builder)
                .defineInRange("tankCapacity", 486L * FluidType.BUCKET_VOLUME, 1, Long.MAX_VALUE));
        builder.pop();

        configSpec = builder.build();
    }

    @Override
    public String getFileName() {
        return "generators";
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
