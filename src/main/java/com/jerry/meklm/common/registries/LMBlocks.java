package com.jerry.meklm.common.registries;

import com.jerry.meklm.common.config.LMConfig;
import com.jerry.meklm.common.tile.TileEntityLargeChemicalWasher;
import com.jerry.meklm.common.tile.TileEntityLargeElectrolyticSeparator;
import com.jerry.meklm.common.tile.TileEntityLargeGasGenerator;
import com.jerry.meklm.common.tile.TileEntityLargeHeatGenerator;
import com.jerry.mekmm.Mekmm;
import mekanism.common.attachments.component.AttachedEjector;
import mekanism.common.attachments.component.AttachedSideConfig;
import mekanism.common.attachments.containers.ContainerType;
import mekanism.common.attachments.containers.chemical.ChemicalTanksBuilder;
import mekanism.common.attachments.containers.fluid.FluidTanksBuilder;
import mekanism.common.attachments.containers.heat.HeatCapacitorsBuilder;
import mekanism.common.attachments.containers.item.ItemSlotsBuilder;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.item.block.ItemBlockTooltip;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registries.MekanismDataComponents;
import mekanism.common.resource.BlockResourceInfo;
import mekanism.common.tile.TileEntityChemicalTank;
import mekanism.generators.common.config.MekanismGeneratorsConfig;
import mekanism.generators.common.content.blocktype.Generator;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.material.MapColor;

public class LMBlocks {

    private LMBlocks() {

    }

    public static final BlockDeferredRegister LM_BLOCKS = new BlockDeferredRegister(Mekmm.MOD_ID);

    public static final BlockRegistryObject<BlockTile.BlockTileModel<TileEntityLargeHeatGenerator, Generator<TileEntityLargeHeatGenerator>>, ItemBlockTooltip<BlockTile.BlockTileModel<TileEntityLargeHeatGenerator, Generator<TileEntityLargeHeatGenerator>>>> LARGE_HEAT_GENERATOR =
            LM_BLOCKS.registerDetails("large_heat_generator", () -> new BlockTile.BlockTileModel<>(LMBlockTypes.LARGE_HEAT_GENERATOR, properties -> properties.mapColor(MapColor.METAL)))
                    .forItemHolder(holder -> holder
                            .addAttachmentOnlyContainers(ContainerType.FLUID, () -> FluidTanksBuilder.builder()
                                    .addBasic(MekanismGeneratorsConfig.generators.heatTankCapacity, fluid -> fluid.is(FluidTags.LAVA))
                                    .build()
                            ).addAttachmentOnlyContainers(ContainerType.HEAT, () -> HeatCapacitorsBuilder.builder()
                                    .addBasic(TileEntityLargeHeatGenerator.HEAT_CAPACITY, TileEntityLargeHeatGenerator.INVERSE_CONDUCTION_COEFFICIENT, TileEntityLargeHeatGenerator.INVERSE_INSULATION_COEFFICIENT)
                                    .build()
                            ).addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                                    .addFluidFuelSlot(0, s -> s.getBurnTime(null) != 0)
                                    .addEnergy()
                                    .build()
                            )
                    );

    public static final BlockRegistryObject<BlockTile.BlockTileModel<TileEntityLargeGasGenerator, Generator<TileEntityLargeGasGenerator>>, ItemBlockTooltip<BlockTile.BlockTileModel<TileEntityLargeGasGenerator, Generator<TileEntityLargeGasGenerator>>>> LARGE_GAS_BURNING_GENERATOR =
            LM_BLOCKS.registerDetails("large_gas_burning_generator", () -> new BlockTile.BlockTileModel<>(LMBlockTypes.LARGE_GAS_BURNING_GENERATOR, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())))
                    .forItemHolder(holder -> holder
                            .addAttachmentOnlyContainers(ContainerType.CHEMICAL, () -> ChemicalTanksBuilder.builder()
                                    .addBasic(LMConfig.generators.lgbgTankCapacity, TileEntityLargeGasGenerator.HAS_FUEL)
                                    .build()
                            ).addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                                    .addChemicalFillSlot(0)
                                    .addEnergy()
                                    .build()
                            ));


    public static final BlockRegistryObject<BlockTile.BlockTileModel<TileEntityLargeElectrolyticSeparator, Machine<TileEntityLargeElectrolyticSeparator>>, ItemBlockTooltip<BlockTile.BlockTileModel<TileEntityLargeElectrolyticSeparator, Machine<TileEntityLargeElectrolyticSeparator>>>> LARGE_ELECTROLYTIC_SEPARATOR =
            LM_BLOCKS.register("large_electrolytic_separator", () -> new BlockTile.BlockTileModel<>(LMBlockTypes.LARGE_ELECTROLYTIC_SEPARATOR, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())),
                    (block, properties) -> new ItemBlockTooltip<>(block, true, properties
                            .component(MekanismDataComponents.DUMP_MODE, TileEntityChemicalTank.GasMode.IDLE)
                            .component(MekanismDataComponents.SECONDARY_DUMP_MODE, TileEntityChemicalTank.GasMode.IDLE)
                            .component(MekanismDataComponents.EJECTOR, AttachedEjector.DEFAULT)
                            .component(MekanismDataComponents.SIDE_CONFIG, AttachedSideConfig.SEPARATOR)
                    )
            ).forItemHolder(holder -> holder
                    .addAttachmentOnlyContainers(ContainerType.FLUID, () -> FluidTanksBuilder.builder()
                            .addBasic(TileEntityLargeElectrolyticSeparator.MAX_FLUID, MekanismRecipeType.SEPARATING, InputRecipeCache.SingleFluid::containsInput)
                            .build()
                    ).addAttachmentOnlyContainers(ContainerType.CHEMICAL, () -> ChemicalTanksBuilder.builder()
                            .addBasic(TileEntityLargeElectrolyticSeparator.MAX_GAS)
                            .addBasic(TileEntityLargeElectrolyticSeparator.MAX_GAS)
                            .build()
                    ).addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                            .addFluidFillSlot(0)
                            .addChemicalDrainSlot(0)
                            .addChemicalDrainSlot(1)
                            .addEnergy()
                            .build()
                    )
            );

    public static final BlockRegistryObject<BlockTile.BlockTileModel<TileEntityLargeChemicalWasher, Machine<TileEntityLargeChemicalWasher>>, ItemBlockTooltip<BlockTile.BlockTileModel<TileEntityLargeChemicalWasher, Machine<TileEntityLargeChemicalWasher>>>> LARGE_CHEMICAL_WASHER =
            LM_BLOCKS.register("large_chemical_washer", () -> new BlockTile.BlockTileModel<>(LMBlockTypes.LARGE_CHEMICAL_WASHER, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())),
                    (block, properties) -> new ItemBlockTooltip<>(block, true, properties
                            .component(MekanismDataComponents.EJECTOR, AttachedEjector.DEFAULT)
                            .component(MekanismDataComponents.SIDE_CONFIG, AttachedSideConfig.WASHER)
                    )
            ).forItemHolder(holder -> holder
                    .addAttachmentOnlyContainers(ContainerType.FLUID, () -> FluidTanksBuilder.builder()
                            .addBasic(TileEntityLargeChemicalWasher.MAX_FLUID, MekanismRecipeType.WASHING, InputRecipeCache.FluidChemical::containsInputA)
                            .build()
                    ).addAttachmentOnlyContainers(ContainerType.CHEMICAL, () -> ChemicalTanksBuilder.builder()
                            .addBasic(TileEntityLargeChemicalWasher.MAX_SLURRY, MekanismRecipeType.WASHING, InputRecipeCache.FluidChemical::containsInputB)
                            .addBasic(TileEntityLargeChemicalWasher.MAX_SLURRY)
                            .build()
                    ).addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                            .addFluidFillSlot(0)
                            .addOutput()
                            .addChemicalDrainSlot(1)
                            .addEnergy()
                            .build()
                    )
            );
}
