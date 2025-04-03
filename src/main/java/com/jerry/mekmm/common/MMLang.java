package com.jerry.mekmm.common;

import com.jerry.mekmm.Mekmm;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.text.ILangEntry;
import net.minecraft.Util;

@NothingNullByDefault
public enum MMLang implements ILangEntry {
    //Gui lang strings
    MEKANISM_MORE_MACHINE("constants", "more_machine"),
    MEKANISM_LARGE_MACHINE("constants", "large_machine"),
    //Factory Type
    RECYCLING("factory", "recycling"),
    PLANTING("factory", "planting"),
    STAMPING("factory", "stamping"),
    LATHING("factory", "lathing"),
    ROLLING_MILL("factory", "rolling_mill"),

    //Doll
    AUTHOR_DOLL("description", "author_doll"),

    //Tooltip stuff
    IS_BLOCKING("tooltip", "is_blocking"),
    NO_BLOCKING("tooltip", "no_blocking");

    private final String key;

    MMLang(String type, String path) {
        this(Util.makeDescriptionId(type, Mekmm.rl(path)));
    }

    MMLang(String key) {
        this.key = key;
    }

    @Override
    public String getTranslationKey() {
        return key;
    }
}
