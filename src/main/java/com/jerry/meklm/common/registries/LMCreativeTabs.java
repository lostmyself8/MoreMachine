package com.jerry.meklm.common.registries;

import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.common.MMLang;
import mekanism.common.registration.MekanismDeferredHolder;
import mekanism.common.registration.impl.CreativeTabDeferredRegister;
import net.minecraft.world.item.CreativeModeTab;

public class LMCreativeTabs {

    // 因为一个modId只能注册一个创造物品栏，所以这里写一个不一样的modId。
    public static final CreativeTabDeferredRegister LM_CREATIVE_TABS = new CreativeTabDeferredRegister(Mekmm.MOD_ID_LM);

    public static final MekanismDeferredHolder<CreativeModeTab, CreativeModeTab> MEKANISM_LARGE_MACHINE = LM_CREATIVE_TABS.registerMain(MMLang.MEKANISM_LARGE_MACHINE,
            LMBlocks.LARGE_GAS_BURNING_GENERATOR.getItemHolder(), builder ->
                    builder.displayItems((displayParameters, output) -> {
                        CreativeTabDeferredRegister.addToDisplay(LMBlocks.LM_BLOCKS, output);
                    })
    );
}
