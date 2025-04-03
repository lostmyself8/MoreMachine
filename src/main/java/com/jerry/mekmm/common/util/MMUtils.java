package com.jerry.mekmm.common.util;

import com.jerry.mekmm.common.block.attribute.MMAttributeFactoryType;
import mekanism.common.block.attribute.Attribute;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;

public class MMUtils {

    //从MekanismUtils的isSameTypeFactory单拎出来的
    public static boolean isSameMMTypeFactory(Holder<Block> block, Block factoryBlockType) {
        MMAttributeFactoryType attribute = Attribute.get(block, MMAttributeFactoryType.class);
        if (attribute != null) {
            MMAttributeFactoryType otherType = Attribute.get(factoryBlockType, MMAttributeFactoryType.class);
            return otherType != null && attribute.getMMFactoryType() == otherType.getMMFactoryType();
        }
        return false;
    }
}
