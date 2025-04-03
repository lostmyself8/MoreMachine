package com.jerry.mekmm.common.block;

import com.jerry.mekmm.common.tile.TileEntityDoll;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;

import java.util.function.UnaryOperator;

public class BlockDoll extends BlockTile<TileEntityDoll, BlockTypeTile<TileEntityDoll>> {

    public BlockDoll(BlockTypeTile<TileEntityDoll> tileEntityDollBlockTypeTile, UnaryOperator<Properties> propertiesModifier) {
        super(tileEntityDollBlockTypeTile, propertiesModifier);
    }
}
