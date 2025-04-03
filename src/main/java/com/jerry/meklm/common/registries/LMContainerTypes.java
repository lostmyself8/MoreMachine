package com.jerry.meklm.common.registries;

import com.jerry.meklm.common.tile.TileEntityLargeChemicalWasher;
import com.jerry.meklm.common.tile.TileEntityLargeElectrolyticSeparator;
import com.jerry.meklm.common.tile.TileEntityLargeGasGenerator;
import com.jerry.meklm.common.tile.TileEntityLargeHeatGenerator;
import com.jerry.mekmm.Mekmm;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeDeferredRegister;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import mekanism.common.registries.MekanismBlocks;

public class LMContainerTypes {

    private LMContainerTypes() {
    }

    public static final ContainerTypeDeferredRegister LM_CONTAINER_TYPES = new ContainerTypeDeferredRegister(Mekmm.MOD_ID);

    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityLargeHeatGenerator>> LARGE_HEAT_GENERATOR = LM_CONTAINER_TYPES.custom(LMBlocks.LARGE_HEAT_GENERATOR, TileEntityLargeHeatGenerator.class).armorSideBar(-20, 11, 0).build();
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityLargeGasGenerator>> LARGE_GAS_BURNING_GENERATOR = LM_CONTAINER_TYPES.custom(LMBlocks.LARGE_GAS_BURNING_GENERATOR, TileEntityLargeGasGenerator.class).armorSideBar(-20, 11, 0).build();
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityLargeElectrolyticSeparator>> LARGE_ELECTROLYTIC_SEPARATOR = LM_CONTAINER_TYPES.register(LMBlocks.LARGE_ELECTROLYTIC_SEPARATOR, TileEntityLargeElectrolyticSeparator.class);
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityLargeChemicalWasher>> LARGE_CHEMICAL_WASHER = LM_CONTAINER_TYPES.register(MekanismBlocks.CHEMICAL_WASHER, TileEntityLargeChemicalWasher.class);

}
