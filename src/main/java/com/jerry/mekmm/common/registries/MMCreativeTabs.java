package com.jerry.mekmm.common.registries;

import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.common.MMLang;
import mekanism.common.registration.MekanismDeferredHolder;
import mekanism.common.registration.impl.CreativeTabDeferredRegister;
import net.minecraft.world.item.CreativeModeTab;

public class MMCreativeTabs {

    public static final CreativeTabDeferredRegister MM_CREATIVE_TABS = new CreativeTabDeferredRegister(Mekmm.MOD_ID);

    public static final MekanismDeferredHolder<CreativeModeTab, CreativeModeTab> MEKANISM_MORE_MACHINE = MM_CREATIVE_TABS.registerMain(MMLang.MEKANISM_MORE_MACHINE,
            MMItems.SCRAP, builder ->
                    builder.displayItems((displayParameters, output) -> {
                                CreativeTabDeferredRegister.addToDisplay(MMItems.MM_ITEMS, output);
                                CreativeTabDeferredRegister.addToDisplay(MMBlocks.MM_BLOCKS, output);
                            })
    );
}
