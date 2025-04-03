package com.jerry.mekmm.common.inventory.container.tile;

import com.jerry.mekmm.common.registries.MMContainerTypes;
import com.jerry.mekmm.common.tile.factory.MMTileEntityFactory;
import com.jerry.mekmm.common.tile.factory.TileEntityPlantingFactory;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.tier.FactoryTier;
import net.minecraft.world.entity.player.Inventory;

public class MMFactoryContainer extends MekanismTileContainer<MMTileEntityFactory<?>> {

    public MMFactoryContainer(int id, Inventory inv, MMTileEntityFactory<?> tile) {
        super(MMContainerTypes.MM_FACTORY, id, inv, tile);
    }

    @Override
    protected int getInventoryYOffset() {
        if (tile.hasSecondaryResourceBar()) {
            return tile instanceof TileEntityPlantingFactory ? 115 : 95;
        }
        return 85;
    }

    @Override
    protected int getInventoryXOffset() {
        return tile.tier == FactoryTier.ULTIMATE ? 26 : 8;
    }
}
