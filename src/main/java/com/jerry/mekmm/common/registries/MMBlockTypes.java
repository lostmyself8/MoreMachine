package com.jerry.mekmm.common.registries;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.jerry.mekmm.common.MMLang;
import com.jerry.mekmm.common.content.blocktype.MMBlockShapes;
import com.jerry.mekmm.common.content.blocktype.MMFactory;
import com.jerry.mekmm.common.content.blocktype.MMFactoryType;
import com.jerry.mekmm.common.content.blocktype.MMMachine;
import com.jerry.mekmm.common.tile.TileEntityDoll;
import com.jerry.mekmm.common.tile.machine.*;
import com.jerry.mekmm.common.util.MMEnumUtils;
import mekanism.api.Upgrade;
import mekanism.common.MekanismLang;
import mekanism.common.block.attribute.*;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.blocktype.BlockShapes;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.registries.MekanismSounds;
import mekanism.common.tier.FactoryTier;
import mekanism.common.util.EnumUtils;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class MMBlockTypes {

    private MMBlockTypes() {
    }

    private static final Table<FactoryTier, MMFactoryType, MMFactory<?>> MM_FACTORIES = HashBasedTable.create();

    // Recycler
    public static final MMMachine.MMFactoryMachine<TileEntityRecycler> RECYCLER = MMMachine.MMMachineBuilder
            .createMMFactoryMachine(() -> MMTileEntityTypes.RECYCLER, MekanismLang.DESCRIPTION_PRECISION_SAWMILL, MMFactoryType.RECYCLER)
            .withGui(() -> MMContainerTypes.RECYCLER)
            .withSound(MekanismSounds.PRECISION_SAWMILL)
            .withEnergyConfig(MekanismConfig.usage.precisionSawmill, MekanismConfig.storage.precisionSawmill)
            .with(AttributeSideConfig.ELECTRIC_MACHINE)
            .withComputerSupport("recycler")
            .build();

    // Planting Station
    public static final MMMachine.MMFactoryMachine<TileEntityPlantingStation> PLANTING_STATION = MMMachine.MMMachineBuilder
            .createMMFactoryMachine(() -> MMTileEntityTypes.PLANTING_STATION, MekanismLang.DESCRIPTION_PRECISION_SAWMILL, MMFactoryType.PLANTING_STATION)
            .withGui(() -> MMContainerTypes.PLANTING_STATION)
            .withSound(MekanismSounds.PRECISION_SAWMILL)
            .withEnergyConfig(MekanismConfig.usage.precisionSawmill, MekanismConfig.storage.precisionSawmill)
            .with(AttributeUpgradeSupport.DEFAULT_ADVANCED_MACHINE_UPGRADES)
            .with(AttributeSideConfig.ADVANCED_ELECTRIC_MACHINE)
            .withComputerSupport("plantingStation")
            .build();

    // CNC Stamper
    public static final MMMachine.MMFactoryMachine<TileEntityStamping> CNC_STAMPER = MMMachine.MMMachineBuilder
            .createMMFactoryMachine(() -> MMTileEntityTypes.CNC_STAMPER, MekanismLang.DESCRIPTION_PRECISION_SAWMILL, MMFactoryType.CNC_STAMPER)
            .withGui(() -> MMContainerTypes.CNC_STAMPER)
            .withSound(MekanismSounds.PRECISION_SAWMILL)
            .withEnergyConfig(MekanismConfig.usage.precisionSawmill, MekanismConfig.storage.precisionSawmill)
            .with(AttributeSideConfig.ELECTRIC_MACHINE)
            .withComputerSupport("cnc_stamper")
            .build();

    // CNC Lathe
    public static final MMMachine.MMFactoryMachine<TileEntityLathe> CNC_LATHE = MMMachine.MMMachineBuilder
            .createMMFactoryMachine(() -> MMTileEntityTypes.CNC_LATHE, MekanismLang.DESCRIPTION_PRECISION_SAWMILL, MMFactoryType.CNC_LATHE)
            .withGui(() -> MMContainerTypes.CNC_LATHE)
            .withSound(MekanismSounds.PRECISION_SAWMILL)
            .withEnergyConfig(MekanismConfig.usage.precisionSawmill, MekanismConfig.storage.precisionSawmill)
            .with(AttributeSideConfig.ELECTRIC_MACHINE)
            .withComputerSupport("cnc_lathe")
            .build();

    // CNC Rolling Mill
    public static final MMMachine.MMFactoryMachine<TileEntityRollingMill> CNC_ROLLING_MILL = MMMachine.MMMachineBuilder
            .createMMFactoryMachine(() -> MMTileEntityTypes.CNC_ROLLING_MILL, MekanismLang.DESCRIPTION_PRECISION_SAWMILL, MMFactoryType.CNC_ROLLING_MILL)
            .withGui(() -> MMContainerTypes.CNC_ROLLING_MILL)
            .withSound(MekanismSounds.PRECISION_SAWMILL)
            .withEnergyConfig(MekanismConfig.usage.precisionSawmill, MekanismConfig.storage.precisionSawmill)
            .with(AttributeSideConfig.ELECTRIC_MACHINE)
            .withComputerSupport("cnc_rolling_mill")
            .build();

    // Replicator
    public static final Machine<TileEntityReplicator> REPLICATOR = Machine.MachineBuilder
            .createMachine(() -> MMTileEntityTypes.REPLICATOR, MekanismLang.DESCRIPTION_ANTIPROTONIC_NUCLEOSYNTHESIZER)
            .withGui(() -> MMContainerTypes.REPLICATOR)
            .withEnergyConfig(MekanismConfig.usage.antiprotonicNucleosynthesizer, MekanismConfig.storage.antiprotonicNucleosynthesizer)
            .withSound(MekanismSounds.ANTIPROTONIC_NUCLEOSYNTHESIZER)
            .with(AttributeSideConfig.ADVANCED_ELECTRIC_MACHINE)
            .withCustomShape(BlockShapes.ANTIPROTONIC_NUCLEOSYNTHESIZER)
            .withComputerSupport("replicator")
            .build();

    static {
        for (FactoryTier tier : EnumUtils.FACTORY_TIERS) {
            for (MMFactoryType type : MMEnumUtils.MM_FACTORY_TYPES) {
                MM_FACTORIES.put(tier, type, MMFactory.MMFactoryBuilder.createMMFactory(() -> MMTileEntityTypes.getMMFactoryTile(tier, type), type, tier).build());
            }
        }
    }

    // Ambient Gas Collector
    public static final Machine<TileEntityAmbientGasCollector> AMBIENT_GAS_COLLECTOR = Machine.MachineBuilder
            .createMachine(() -> MMTileEntityTypes.AMBIENT_GAS_COLLECTOR, MekanismLang.DESCRIPTION_ELECTRIC_PUMP)
            .withGui(() -> MMContainerTypes.AMBIENT_GAS_COLLECTOR)
            .withEnergyConfig(MekanismConfig.usage.electricPump, MekanismConfig.storage.electricPump)
            .withSupportedUpgrades(Upgrade.SPEED, Upgrade.ENERGY)
            .withCustomShape(BlockShapes.ELECTRIC_PUMP)
            .withComputerSupport("ambientGasCollector")
            .replace(Attributes.ACTIVE)
            .build();

    // Author Doll
    public static final BlockTypeTile<TileEntityDoll> AUTHOR_DOLL = BlockTypeTile.BlockTileBuilder
            .createBlock(() -> MMTileEntityTypes.AUTHOR_DOLL, MMLang.AUTHOR_DOLL)
            .with(new AttributeStateFacing(BlockStateProperties.HORIZONTAL_FACING))
            .withCustomShape(MMBlockShapes.AUTHOR_DOLL)
            .with(AttributeCustomSelectionBox.JSON)
            .build();

    public static MMFactory<?> getMMFactory(FactoryTier tier, MMFactoryType type) {
        return MM_FACTORIES.get(tier, type);
    }
}
