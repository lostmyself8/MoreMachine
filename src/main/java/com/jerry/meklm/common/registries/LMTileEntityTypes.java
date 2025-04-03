package com.jerry.meklm.common.registries;

import com.jerry.meklm.common.tile.TileEntityLargeChemicalWasher;
import com.jerry.meklm.common.tile.TileEntityLargeElectrolyticSeparator;
import com.jerry.meklm.common.tile.TileEntityLargeGasGenerator;
import com.jerry.meklm.common.tile.TileEntityLargeHeatGenerator;
import com.jerry.mekmm.Mekmm;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;

public class LMTileEntityTypes {

    private LMTileEntityTypes() {

    }

    public static final TileEntityTypeDeferredRegister LM_TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(Mekmm.MOD_ID);

    public static final TileEntityTypeRegistryObject<TileEntityLargeHeatGenerator> LARGE_HEAT_GENERATOR = LM_TILE_ENTITY_TYPES
            .mekBuilder(LMBlocks.LARGE_HEAT_GENERATOR, TileEntityLargeHeatGenerator::new)
            .clientTicker(TileEntityMekanism::tickClient)
            .serverTicker(TileEntityMekanism::tickServer)
            .withSimple(Capabilities.CONFIG_CARD)
            .without(Capabilities.FLUID.block(), Capabilities.ITEM.block())
            .build();

    public static final TileEntityTypeRegistryObject<TileEntityLargeGasGenerator> LARGE_GAS_BURNING_GENERATOR = LM_TILE_ENTITY_TYPES
            .mekBuilder(LMBlocks.LARGE_GAS_BURNING_GENERATOR, TileEntityLargeGasGenerator::new)
            .clientTicker(TileEntityMekanism::tickClient)
            .serverTicker(TileEntityMekanism::tickServer)
            .withSimple(Capabilities.CONFIG_CARD)
            .without(Capabilities.CHEMICAL.block(), Capabilities.ITEM.block())
            .build();

    public static final TileEntityTypeRegistryObject<TileEntityLargeElectrolyticSeparator> LARGE_ELECTROLYTIC_SEPARATOR = LM_TILE_ENTITY_TYPES
            .mekBuilder(LMBlocks.LARGE_ELECTROLYTIC_SEPARATOR, TileEntityLargeElectrolyticSeparator::new)
            .clientTicker(TileEntityMekanism::tickClient)
            .serverTicker(TileEntityMekanism::tickServer)
            .withSimple(Capabilities.CONFIG_CARD)
            .without(Capabilities.FLUID.block(), Capabilities.CHEMICAL.block(), Capabilities.ITEM.block(), Capabilities.ENERGY.block())
            .build();

    public static final TileEntityTypeRegistryObject<TileEntityLargeChemicalWasher> LARGE_CHEMICAL_WASHER = LM_TILE_ENTITY_TYPES
            .mekBuilder(LMBlocks.LARGE_CHEMICAL_WASHER, TileEntityLargeChemicalWasher::new)
            .clientTicker(TileEntityMekanism::tickClient)
            .serverTicker(TileEntityMekanism::tickServer)
            .withSimple(Capabilities.CONFIG_CARD)
            .without(Capabilities.FLUID.block(), Capabilities.CHEMICAL.block(), Capabilities.ITEM.block())
            .build();
}
