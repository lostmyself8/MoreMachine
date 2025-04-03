package com.jerry.meklm.common.registries;

import com.jerry.meklm.common.content.blocktype.LMBlockShapes;
import com.jerry.meklm.common.tile.TileEntityLargeChemicalWasher;
import com.jerry.meklm.common.tile.TileEntityLargeElectrolyticSeparator;
import com.jerry.meklm.common.tile.TileEntityLargeGasGenerator;
import com.jerry.meklm.common.tile.TileEntityLargeHeatGenerator;
import com.jerry.mekmm.common.config.MMConfig;
import mekanism.api.math.MathUtils;
import mekanism.common.MekanismLang;
import mekanism.common.block.attribute.*;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.lib.math.Pos3D;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.registries.MekanismSounds;
import mekanism.common.util.ChemicalUtil;
import mekanism.generators.common.GeneratorsLang;
import mekanism.generators.common.config.MekanismGeneratorsConfig;
import mekanism.generators.common.content.blocktype.Generator;
import mekanism.generators.common.registries.GeneratorsSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class LMBlockTypes {

    // Heat Generator
    public static final Generator<TileEntityLargeHeatGenerator> LARGE_HEAT_GENERATOR = Generator.GeneratorBuilder
            .createGenerator(() -> LMTileEntityTypes.LARGE_HEAT_GENERATOR, GeneratorsLang.DESCRIPTION_HEAT_GENERATOR)
            .withGui(() -> LMContainerTypes.LARGE_HEAT_GENERATOR)
            .withEnergyConfig(MekanismGeneratorsConfig.storageConfig.heatGenerator)
            .withCustomShape(LMBlockShapes.LARGE_HEAT_GENERATOR)
            .with(AttributeCustomSelectionBox.JSON)
            .withBounding(new AttributeHasBounding.HandleBoundingBlock() {
                @Override
                public <DATA> boolean handle(Level level, BlockPos pos, BlockState state, DATA data, AttributeHasBounding.TriBooleanFunction<Level, BlockPos, DATA> predicate) {
                    BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
                    for (int x = -1; x <= 1; x++) {
                        for (int y = 0; y <= 2; y++) {
                            for (int z = -1; z <= 1; z++) {
                                if (x != 0 || y != 0 || z != 0) {
                                    mutable.setWithOffset(pos, x, y, z);
                                    if (!predicate.accept(level, mutable, data)) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                    return true;
                }
            })
            .withSound(GeneratorsSounds.HEAT_GENERATOR)
            .with(AttributeUpgradeSupport.MUFFLING_ONLY)
            .withComputerSupport("heatGenerator")
            .replace(Attributes.ACTIVE_MELT_LIGHT)
            .with(new AttributeParticleFX()
                    .add(ParticleTypes.SMOKE, rand -> new Pos3D(rand.nextFloat() * 0.6F - 0.3F, rand.nextFloat() * 6.0F / 16.0F, -0.52))
                    .add(ParticleTypes.FLAME, rand -> new Pos3D(rand.nextFloat() * 0.6F - 0.3F, rand.nextFloat() * 6.0F / 16.0F, -0.52)))
            .build();

    // Gas Burning Generator
    public static final Generator<TileEntityLargeGasGenerator> LARGE_GAS_BURNING_GENERATOR = Generator.GeneratorBuilder
            .createGenerator(() -> LMTileEntityTypes.LARGE_GAS_BURNING_GENERATOR, GeneratorsLang.DESCRIPTION_GAS_BURNING_GENERATOR)
            .withGui(() -> LMContainerTypes.LARGE_GAS_BURNING_GENERATOR)
            .withEnergyConfig(() -> MathUtils.multiplyClamped(4_000, ChemicalUtil.hydrogenEnergyDensity()) * MMConfig.general.conversionMultiplier.get())
            .withCustomShape(LMBlockShapes.LARGE_GAS_BURNING_GENERATOR)
            .with(AttributeCustomSelectionBox.JAVA)
            .withBounding(new AttributeHasBounding.HandleBoundingBlock() {
                @Override
                public <DATA> boolean handle(Level level, BlockPos pos, BlockState state, DATA data, AttributeHasBounding.TriBooleanFunction<Level, BlockPos, DATA> predicate) {
                    BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
                    for (int x = -1; x <= 1; x++) {
                        for (int y = 0; y <= 2; y++) {
                            for (int z = -1; z <= 1; z++) {
                                if (x != 0 || y != 0 || z != 0) {
                                    mutable.setWithOffset(pos, x, y, z);
                                    if (!predicate.accept(level, mutable, data)) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                    return true;
                }
            })
            .withSound(GeneratorsSounds.GAS_BURNING_GENERATOR)
            .with(AttributeUpgradeSupport.MUFFLING_ONLY)
//            .withSupportedUpgrades(Upgrade.SPEED, Upgrade.MUFFLING)
            .withComputerSupport("largeGasBurningGenerator")
            .replace(Attributes.ACTIVE_MELT_LIGHT)
            .build();

    // Electrolytic Separator
    public static final Machine<TileEntityLargeElectrolyticSeparator> LARGE_ELECTROLYTIC_SEPARATOR = Machine.MachineBuilder
            .createMachine(() -> LMTileEntityTypes.LARGE_ELECTROLYTIC_SEPARATOR, MekanismLang.DESCRIPTION_ELECTROLYTIC_SEPARATOR)
            .withGui(() -> LMContainerTypes.LARGE_ELECTROLYTIC_SEPARATOR)
            .withSound(MekanismSounds.ELECTROLYTIC_SEPARATOR)
            .withEnergyConfig(() -> MathUtils.multiplyClamped(2, ChemicalUtil.hydrogenEnergyDensity()), MekanismConfig.storage.electrolyticSeparator)
            .withSideConfig(TransmissionType.FLUID, TransmissionType.CHEMICAL, TransmissionType.ITEM, TransmissionType.ENERGY)
            .withCustomShape(LMBlockShapes.LARGE_ELECTROLYTIC_SEPARATOR)
            .with(AttributeCustomSelectionBox.JSON)
            .withBounding(new AttributeHasBounding.HandleBoundingBlock() {
                @Override
                public <DATA> boolean handle(Level level, BlockPos pos, BlockState state, DATA data, AttributeHasBounding.TriBooleanFunction<Level, BlockPos, DATA> predicate) {
                    BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
                    for (int x = -1; x <= 1; x++) {
                        for (int y = 0; y <= 1; y++) {
                            for (int z = -1; z <= 1; z++) {
                                if (x != 0 || y != 0 || z != 0) {
                                    mutable.setWithOffset(pos, x, y, z);
                                    if (!predicate.accept(level, mutable, data)) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                    return true;
                }
            })
            .withComputerSupport("largeElectrolyticSeparator")
            .replace(Attributes.ACTIVE)
            .build();

    // Chemical Washer
    public static final Machine<TileEntityLargeChemicalWasher> LARGE_CHEMICAL_WASHER = Machine.MachineBuilder
            .createMachine(() -> LMTileEntityTypes.LARGE_CHEMICAL_WASHER, MekanismLang.DESCRIPTION_CHEMICAL_WASHER)
            .withGui(() -> LMContainerTypes.LARGE_CHEMICAL_WASHER)
            .withSound(MekanismSounds.CHEMICAL_WASHER)
            .withEnergyConfig(MekanismConfig.usage.chemicalWasher, MekanismConfig.storage.chemicalWasher)
            .withSideConfig(TransmissionType.CHEMICAL, TransmissionType.FLUID, TransmissionType.ITEM, TransmissionType.ENERGY)
            .withCustomShape(LMBlockShapes.LARGE_CHEMICAL_WASHER)
            .with(AttributeCustomSelectionBox.JAVA)
            .withBounding(new AttributeHasBounding.HandleBoundingBlock() {
                @Override
                public <DATA> boolean handle(Level level, BlockPos pos, BlockState state, DATA data, AttributeHasBounding.TriBooleanFunction<Level, BlockPos, DATA> predicate) {
                    BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
                    for (int x = -1; x <= 1; x++) {
                        for (int y = 0; y <= 2; y++) {
                            for (int z = -1; z <= 1; z++) {
                                if (x != 0 || y != 0 || z != 0) {
                                    mutable.setWithOffset(pos, x, y, z);
                                    if (!predicate.accept(level, mutable, data)) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                    return true;
                }
            })
            .withComputerSupport("largeChemicalWasher")
            .build();
}
