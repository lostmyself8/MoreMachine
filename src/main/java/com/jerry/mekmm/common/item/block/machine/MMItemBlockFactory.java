package com.jerry.mekmm.common.item.block.machine;

import com.jerry.mekmm.common.block.attribute.MMAttributeFactoryType;
import com.jerry.mekmm.common.block.prefab.MMBlockFactoryMachine;
import mekanism.api.text.EnumColor;
import mekanism.common.MekanismLang;
import mekanism.common.attachments.component.AttachedEjector;
import mekanism.common.attachments.component.AttachedSideConfig;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.item.block.ItemBlockTooltip;
import mekanism.common.registries.MekanismDataComponents;
import mekanism.common.tier.FactoryTier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MMItemBlockFactory extends ItemBlockTooltip<BlockTile<?, ?>> {

    private static AttachedSideConfig getSideConfig(MMBlockFactoryMachine.MMBlockFactory<?> block) {
        return switch (Attribute.getOrThrow(block.builtInRegistryHolder(), MMAttributeFactoryType.class).getMMFactoryType()) {
//            case COMPRESSING, INFUSING -> AttachedSideConfig.ADVANCED_MACHINE;
//            case COMBINING -> AttachedSideConfig.EXTRA_MACHINE;
//            case PURIFYING, INJECTING -> AttachedSideConfig.ADVANCED_MACHINE_INPUT_ONLY;
            case RECYCLER, CNC_STAMPER, CNC_LATHE, CNC_ROLLING_MILL -> AttachedSideConfig.ELECTRIC_MACHINE;
            case PLANTING_STATION -> AttachedSideConfig.ADVANCED_MACHINE_INPUT_ONLY;
        };
    }

    public MMItemBlockFactory(MMBlockFactoryMachine.MMBlockFactory<?> block, Properties properties) {
        super(block, true, properties
                .component(MekanismDataComponents.SORTING, false)
                .component(MekanismDataComponents.EJECTOR, AttachedEjector.DEFAULT)
                .component(MekanismDataComponents.SIDE_CONFIG, getSideConfig(block))
        );
    }

    @Override
    public FactoryTier getTier() {
        return Attribute.getTier(getBlock(), FactoryTier.class);
    }

    @Override
    protected void addTypeDetails(@NotNull ItemStack stack, @NotNull Item.TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        //Should always be present but validate it just in case
        MMAttributeFactoryType factoryType = Attribute.get(getBlock(), MMAttributeFactoryType.class);
        if (factoryType != null) {
            tooltip.add(MekanismLang.FACTORY_TYPE.translateColored(EnumColor.INDIGO, EnumColor.GRAY, factoryType.getMMFactoryType()));
        }
        super.addTypeDetails(stack, context, tooltip, flag);
    }
}
