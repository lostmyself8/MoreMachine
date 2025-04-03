package com.jerry.mekmm.common.block.attribute;

import com.jerry.mekmm.common.content.blocktype.MMFactoryType;
import mekanism.common.block.attribute.Attribute;
import org.jetbrains.annotations.NotNull;

public class MMAttributeFactoryType implements Attribute {

    private final MMFactoryType type;

    public MMAttributeFactoryType(MMFactoryType type) {
        this.type = type;
    }

    @NotNull
    public MMFactoryType getMMFactoryType() {
        return type;
    }
}
