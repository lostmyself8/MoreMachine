package com.jerry.mekmm.common.content.blocktype;

import com.jerry.mekmm.common.MMLang;
import com.jerry.mekmm.common.registries.MMBlockTypes;
import com.jerry.mekmm.common.registries.MMBlocks;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.text.IHasTranslationKey;
import mekanism.api.text.ILangEntry;
import mekanism.common.registration.impl.BlockRegistryObject;

import java.util.Locale;
import java.util.function.Supplier;

@NothingNullByDefault
public enum MMFactoryType implements IHasTranslationKey.IHasEnumNameTranslationKey {
    RECYCLER("recycling", MMLang.RECYCLING, () -> MMBlockTypes.RECYCLER, () -> MMBlocks.RECYCLER),
    PLANTING_STATION("planting", MMLang.PLANTING, () -> MMBlockTypes.PLANTING_STATION, () -> MMBlocks.PLANTING_STATION),
    CNC_STAMPER("stamping", MMLang.STAMPING, () -> MMBlockTypes.CNC_STAMPER, () -> MMBlocks.CNC_STAMPER),
    CNC_LATHE("lathing", MMLang.LATHING, () -> MMBlockTypes.CNC_LATHE, () -> MMBlocks.CNC_LATHE),
    CNC_ROLLING_MILL("rolling_mill", MMLang.ROLLING_MILL, () -> MMBlockTypes.CNC_ROLLING_MILL, () -> MMBlocks.CNC_ROLLING_MILL);

    private final String registryNameComponent;
    private final ILangEntry langEntry;
    private final Supplier<MMMachine.MMFactoryMachine<?>> baseMachine;
    private final Supplier<BlockRegistryObject<?, ?>> baseBlock;

    MMFactoryType(String registryNameComponent, ILangEntry langEntry, Supplier<MMMachine.MMFactoryMachine<?>> baseMachine, Supplier<BlockRegistryObject<?, ?>> baseBlock) {
        this.registryNameComponent = registryNameComponent;
        this.langEntry = langEntry;
        this.baseMachine = baseMachine;
        this.baseBlock = baseBlock;
    }

    public String getRegistryNameComponent() {
        return registryNameComponent;
    }

    public String getRegistryNameComponentCapitalized() {
        String name = getRegistryNameComponent();
        return name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1);
    }

    public MMMachine.MMFactoryMachine<?> getBaseMachine() {
        return baseMachine.get();
    }

    public BlockRegistryObject<?, ?> getBaseBlock() {
        return baseBlock.get();
    }

    @Override
    public String getTranslationKey() {
        return langEntry.getTranslationKey();
    }
}
