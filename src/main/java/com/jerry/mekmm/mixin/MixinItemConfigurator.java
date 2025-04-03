package com.jerry.mekmm.mixin;

import mekanism.common.item.ItemConfigurator;
import mekanism.common.item.interfaces.IItemHUDProvider;
import mekanism.common.lib.radial.IRadialModeItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.jerry.mekmm.common.tile.machine.TileEntityReplicator.customRecipeMap;

@Mixin(value = ItemConfigurator.class, remap = false)
public abstract class MixinItemConfigurator extends Item implements IRadialModeItem<ItemConfigurator.ConfiguratorMode>, IItemHUDProvider {
    public MixinItemConfigurator(Properties properties) {
        super(properties);
    }

    @Inject(method = "useOn", at = @At(value = "HEAD"))
    public void useOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        for (String resourceKey : customRecipeMap.keySet()) {
            String[] parts = resourceKey.split(":", 2);
            ResourceLocation namespaceAndPath = ResourceLocation.fromNamespaceAndPath(parts[0], parts[1]);
            if (BuiltInRegistries.ITEM.get(namespaceAndPath) == Items.AIR) continue;
            System.out.println(BuiltInRegistries.ITEM.get(namespaceAndPath));
        }
    }
}
