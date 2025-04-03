package com.jerry.mekmm.common.registries;

import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.common.inventory.container.tile.MMFactoryContainer;
import com.jerry.mekmm.common.tile.factory.MMTileEntityFactory;
import com.jerry.mekmm.common.tile.machine.*;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeDeferredRegister;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import com.jerry.mekmm.common.tile.machine.TileEntityAmbientGasCollector;

public class MMContainerTypes {

    private MMContainerTypes() {

    }

    public static final ContainerTypeDeferredRegister MM_CONTAINER_TYPES = new ContainerTypeDeferredRegister(Mekmm.MOD_ID);

    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityRecycler>> RECYCLER = MM_CONTAINER_TYPES.register(MMBlocks.RECYCLER, TileEntityRecycler.class);
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityPlantingStation>> PLANTING_STATION = MM_CONTAINER_TYPES.register(MMBlocks.PLANTING_STATION, TileEntityPlantingStation.class);
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityStamping>> CNC_STAMPER = MM_CONTAINER_TYPES.register(MMBlocks.CNC_STAMPER, TileEntityStamping.class);
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityLathe>> CNC_LATHE = MM_CONTAINER_TYPES.register(MMBlocks.CNC_LATHE, TileEntityLathe.class);
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityRollingMill>> CNC_ROLLING_MILL = MM_CONTAINER_TYPES.register(MMBlocks.CNC_ROLLING_MILL, TileEntityRollingMill.class);
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityReplicator>> REPLICATOR = MM_CONTAINER_TYPES.register(MMBlocks.REPLICATOR, TileEntityReplicator.class);
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityAmbientGasCollector>> AMBIENT_GAS_COLLECTOR = MM_CONTAINER_TYPES.register(MMBlocks.AMBIENT_GAS_COLLECTOR, TileEntityAmbientGasCollector.class);


    public static final ContainerTypeRegistryObject<MekanismTileContainer<MMTileEntityFactory<?>>> MM_FACTORY = MM_CONTAINER_TYPES.register("factory", factoryClass(), MMFactoryContainer::new);

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static Class<MMTileEntityFactory<?>> factoryClass() {
        return (Class) MMTileEntityFactory.class;
    }

}
