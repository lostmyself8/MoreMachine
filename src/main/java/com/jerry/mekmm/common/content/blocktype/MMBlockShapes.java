package com.jerry.mekmm.common.content.blocktype;

import mekanism.common.tier.FactoryTier;
import mekanism.common.util.EnumUtils;
import mekanism.common.util.VoxelShapeUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MMBlockShapes {

    private MMBlockShapes() {
    }

    private static VoxelShape box(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return Block.box(minX, minY, minZ, maxX, maxY, maxZ);
    }

    //Factories
    public static final VoxelShape[] RECYCLER_FACTORY = new VoxelShape[EnumUtils.HORIZONTAL_DIRECTIONS.length];

    //Doll
    public static final VoxelShape[] AUTHOR_DOLL = new VoxelShape[EnumUtils.HORIZONTAL_DIRECTIONS.length];

    static {
        VoxelShapeUtils.setShape(VoxelShapeUtils.combine(
                box(0, 0, 0, 16, 16, 4), // front_panel
                box(4, 4, 14, 12, 12, 16), // port
                box(0, 5, 5, 7, 16, 16), // saw2
                box(9, 5, 5, 16, 16, 16), // saw1
                box(0, 0, 4, 16, 4, 16), // base
                box(1, 4, 4, 15, 15, 15) // core
        ), RECYCLER_FACTORY);

        VoxelShapeUtils.setShape(VoxelShapeUtils.combine(
                box(4.35, 6.2, 7.35, 11.65, 13.5, 14.65), // head
                box(5, 4.2, 7.08248, 6.5, 6.2, 13.08248), // left_arm
                box(9.6, 4.54246, 6.979, 11.1, 6.54246, 12.979), // right_arm
                box(8, 0.1, 4, 10, 2.1, 10), //left_leg
                box(6, 0.1, 4, 8, 2.1, 10), //right_leg
                box(5.6, 0.2, 9.6, 10.4, 6.2, 12.4) //body
        ), AUTHOR_DOLL);
    }

    public static VoxelShape[] getShape(FactoryTier tier, MMFactoryType type) {
        return switch (type) {
            case RECYCLER, PLANTING_STATION, CNC_STAMPER, CNC_LATHE, CNC_ROLLING_MILL -> RECYCLER_FACTORY;
        };
    }
}
