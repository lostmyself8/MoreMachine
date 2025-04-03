package com.jerry.mekmm.common.block.prefab;

import com.jerry.mekmm.common.content.blocktype.MMFactory;
import com.jerry.mekmm.common.content.blocktype.MMMachine;
import com.jerry.mekmm.common.tile.factory.MMTileEntityFactory;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.block.states.IStateFluidLoggable;
import mekanism.common.resource.BlockResourceInfo;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.UnaryOperator;

public class MMBlockFactoryMachine<TILE extends TileEntityMekanism, MACHINE extends MMMachine.MMFactoryMachine<TILE>> extends BlockTile<TILE, MACHINE> {

    public MMBlockFactoryMachine(MACHINE machine, UnaryOperator<Properties> propertiesModifier) {
        super(machine, propertiesModifier);
    }

    public static class MMBlockFactoryMachineModel<TILE extends TileEntityMekanism, MACHINE extends MMMachine.MMFactoryMachine<TILE>> extends MMBlockFactoryMachine<TILE, MACHINE> implements IStateFluidLoggable {

        public MMBlockFactoryMachineModel(MACHINE machineType, UnaryOperator<BlockBehaviour.Properties> propertiesModifier) {
            super(machineType, propertiesModifier);
        }
    }

    public static class MMBlockFactory<TILE extends MMTileEntityFactory<?>> extends MMBlockFactoryMachineModel<TILE, MMFactory<TILE>> {

        public MMBlockFactory(MMFactory<TILE> factoryType) {
            super(factoryType, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor()));
        }
    }
}
