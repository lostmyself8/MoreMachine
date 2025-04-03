package com.jerry.mekmm.common.registries;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.common.attachments.containers.chemical.MMChemicalTanksBuilder;
import com.jerry.mekmm.common.attachments.containers.item.MMItemSlotsBuilder;
import com.jerry.mekmm.common.block.BlockDoll;
import com.jerry.mekmm.common.block.prefab.MMBlockFactoryMachine;
import com.jerry.mekmm.common.content.blocktype.MMFactory;
import com.jerry.mekmm.common.content.blocktype.MMFactoryType;
import com.jerry.mekmm.common.content.blocktype.MMMachine;
import com.jerry.mekmm.common.item.block.ItemBlockDoll;
import com.jerry.mekmm.common.item.block.machine.MMItemBlockFactory;
import com.jerry.mekmm.common.recipe.MMRecipeType;
import com.jerry.mekmm.common.recipe.lookup.cache.MMInputRecipeCache;
import com.jerry.mekmm.common.recipe.lookup.cache.MMSingleInputRecipeCache;
import com.jerry.mekmm.common.tile.factory.MMTileEntityFactory;
import com.jerry.mekmm.common.tile.machine.*;
import com.jerry.mekmm.common.tile.prefab.MMTileEntityAdvancedElectricMachine;
import com.jerry.mekmm.common.util.MMEnumUtils;
import mekanism.api.tier.ITier;
import mekanism.common.attachments.component.AttachedEjector;
import mekanism.common.attachments.component.AttachedSideConfig;
import mekanism.common.attachments.containers.ContainerType;
import mekanism.common.attachments.containers.chemical.ChemicalTanksBuilder;
import mekanism.common.attachments.containers.item.ItemSlotsBuilder;
import mekanism.common.block.attribute.AttributeTier;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.item.block.ItemBlockTooltip;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registries.MekanismDataComponents;
import mekanism.common.resource.BlockResourceInfo;
import mekanism.common.tier.FactoryTier;
import com.jerry.mekmm.common.tile.machine.TileEntityAmbientGasCollector;
import mekanism.common.util.EnumUtils;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class MMBlocks {

    private MMBlocks() {
    }

    public static final BlockDeferredRegister MM_BLOCKS = new BlockDeferredRegister(Mekmm.MOD_ID);

    private static final Table<FactoryTier, MMFactoryType, BlockRegistryObject<MMBlockFactoryMachine.MMBlockFactory<?>, MMItemBlockFactory>> MM_FACTORIES = HashBasedTable.create();

    static {
        // factories
        for (FactoryTier tier : EnumUtils.FACTORY_TIERS) {
            for (MMFactoryType type : MMEnumUtils.MM_FACTORY_TYPES) {
                MM_FACTORIES.put(tier, type, registerMMFactory(MMBlockTypes.getMMFactory(tier, type)));
            }
        }
    }

    public static final BlockRegistryObject<MMBlockFactoryMachine<TileEntityRecycler, MMMachine.MMFactoryMachine<TileEntityRecycler>>, ItemBlockTooltip<MMBlockFactoryMachine<TileEntityRecycler, MMMachine.MMFactoryMachine<TileEntityRecycler>>>> RECYCLER =
            MM_BLOCKS.register("recycler", () -> new MMBlockFactoryMachine<>(MMBlockTypes.RECYCLER, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())),
                    (block, properties) -> new ItemBlockTooltip<>(block, true, properties
                            .component(MekanismDataComponents.EJECTOR, AttachedEjector.DEFAULT)
                            .component(MekanismDataComponents.SIDE_CONFIG, AttachedSideConfig.ELECTRIC_MACHINE)
                    )
            ).forItemHolder(holder -> holder.addAttachmentOnlyContainers(ContainerType.ITEM, () -> MMItemSlotsBuilder.builder()
                    .addInput(MMRecipeType.RECYCLER, MMSingleInputRecipeCache::containsInput)
                    .addOutput()
                    .addEnergy()
                    .build()
            ));

    public static final BlockRegistryObject<MMBlockFactoryMachine<TileEntityPlantingStation, MMMachine.MMFactoryMachine<TileEntityPlantingStation>>, ItemBlockTooltip<MMBlockFactoryMachine<TileEntityPlantingStation, MMMachine.MMFactoryMachine<TileEntityPlantingStation>>>> PLANTING_STATION =
            MM_BLOCKS.register("planting_station", () -> new MMBlockFactoryMachine<>(MMBlockTypes.PLANTING_STATION, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())),
                    (block, properties) -> new ItemBlockTooltip<>(block, true, properties
                            .component(MekanismDataComponents.EJECTOR, AttachedEjector.DEFAULT)
                            .component(MekanismDataComponents.SIDE_CONFIG, AttachedSideConfig.ADVANCED_MACHINE_INPUT_ONLY)
                    )
            ).forItemHolder(holder -> holder.
                    addAttachmentOnlyContainers(ContainerType.CHEMICAL, () -> MMChemicalTanksBuilder.builder()
                            .addBasic(TileEntityPlantingStation.MAX_GAS, MMRecipeType.PLANTING_STATION, MMInputRecipeCache.ItemChemical::containsInputB)
                            .build()
                    ).addAttachmentOnlyContainers(ContainerType.ITEM, () -> MMItemSlotsBuilder.builder()
                            .addInput(MMRecipeType.PLANTING_STATION, MMInputRecipeCache.ItemChemical::containsInputA)
                            .addChemicalFillOrConvertSlot(0)
                            .addOutput()
                            .addEnergy()
                            .build()
                    )
        );

    public static final BlockRegistryObject<MMBlockFactoryMachine<TileEntityStamping, MMMachine.MMFactoryMachine<TileEntityStamping>>, ItemBlockTooltip<MMBlockFactoryMachine<TileEntityStamping, MMMachine.MMFactoryMachine<TileEntityStamping>>>> CNC_STAMPER =
            MM_BLOCKS.register("cnc_stamper", () -> new MMBlockFactoryMachine<>(MMBlockTypes.CNC_STAMPER, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())),
                    (block, properties) -> new ItemBlockTooltip<>(block, true, properties
                            .component(MekanismDataComponents.EJECTOR, AttachedEjector.DEFAULT)
                            .component(MekanismDataComponents.SIDE_CONFIG, AttachedSideConfig.ELECTRIC_MACHINE)
                    )
            ).forItemHolder(holder -> holder.addAttachmentOnlyContainers(ContainerType.ITEM, () -> MMItemSlotsBuilder.builder()
                    .addInput(MMRecipeType.RECYCLER, MMSingleInputRecipeCache::containsInput)
                    .addOutput()
                    .addEnergy()
                    .build()
            ));

    public static final BlockRegistryObject<MMBlockFactoryMachine<TileEntityLathe, MMMachine.MMFactoryMachine<TileEntityLathe>>, ItemBlockTooltip<MMBlockFactoryMachine<TileEntityLathe, MMMachine.MMFactoryMachine<TileEntityLathe>>>> CNC_LATHE =
            MM_BLOCKS.register("cnc_lathe", () -> new MMBlockFactoryMachine<>(MMBlockTypes.CNC_LATHE, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())),
                    (block, properties) -> new ItemBlockTooltip<>(block, true, properties
                            .component(MekanismDataComponents.EJECTOR, AttachedEjector.DEFAULT)
                            .component(MekanismDataComponents.SIDE_CONFIG, AttachedSideConfig.ELECTRIC_MACHINE)
                    )
            ).forItemHolder(holder -> holder.addAttachmentOnlyContainers(ContainerType.ITEM, () -> MMItemSlotsBuilder.builder()
                    .addInput(MMRecipeType.RECYCLER, MMSingleInputRecipeCache::containsInput)
                    .addOutput()
                    .addEnergy()
                    .build()
            ));

    public static final BlockRegistryObject<MMBlockFactoryMachine<TileEntityRollingMill, MMMachine.MMFactoryMachine<TileEntityRollingMill>>, ItemBlockTooltip<MMBlockFactoryMachine<TileEntityRollingMill, MMMachine.MMFactoryMachine<TileEntityRollingMill>>>> CNC_ROLLING_MILL =
            MM_BLOCKS.register("cnc_rolling_mill", () -> new MMBlockFactoryMachine<>(MMBlockTypes.CNC_ROLLING_MILL, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())),
                    (block, properties) -> new ItemBlockTooltip<>(block, true, properties
                            .component(MekanismDataComponents.EJECTOR, AttachedEjector.DEFAULT)
                            .component(MekanismDataComponents.SIDE_CONFIG, AttachedSideConfig.ELECTRIC_MACHINE)
                    )
            ).forItemHolder(holder -> holder.addAttachmentOnlyContainers(ContainerType.ITEM, () -> MMItemSlotsBuilder.builder()
                    .addInput(MMRecipeType.RECYCLER, MMSingleInputRecipeCache::containsInput)
                    .addOutput()
                    .addEnergy()
                    .build()
            ));

    public static final BlockRegistryObject<BlockTile.BlockTileModel<TileEntityReplicator, Machine<TileEntityReplicator>>, ItemBlockTooltip<BlockTile.BlockTileModel<TileEntityReplicator, Machine<TileEntityReplicator>>>> REPLICATOR =
            MM_BLOCKS.register("replicator", () -> new BlockTile.BlockTileModel<>(MMBlockTypes.REPLICATOR, properties -> properties.mapColor(MapColor.METAL)),
                    (block, properties) -> new ItemBlockTooltip<>(block, true, properties
                            .component(MekanismDataComponents.EJECTOR, AttachedEjector.DEFAULT)
                            .component(MekanismDataComponents.SIDE_CONFIG, AttachedSideConfig.ADVANCED_MACHINE_INPUT_ONLY)
                    )
            ).forItemHolder(holder -> holder
                    .addAttachmentOnlyContainers(ContainerType.CHEMICAL, () -> MMChemicalTanksBuilder.builder()
                            .addBasic(() -> TileEntityReplicator.MAX_GAS, TileEntityReplicator::isValidChemicalInput)
                            .build()
                    ).addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                            .addChemicalFillOrConvertSlot(0)
                            .addInput(TileEntityReplicator::isValidItemInput)
                            .addOutput()
                            .addEnergy()
                            .build()
                    )
            );

    private static <TILE extends MMTileEntityFactory<?>> BlockRegistryObject<MMBlockFactoryMachine.MMBlockFactory<?>, MMItemBlockFactory> registerMMFactory(MMFactory<TILE> type) {
        FactoryTier tier = (FactoryTier) Objects.requireNonNull(type.get(AttributeTier.class)).tier();
        BlockRegistryObject<MMBlockFactoryMachine.MMBlockFactory<?>, MMItemBlockFactory> factory = registerTieredBlock(tier, "_" + type.getMMFactoryType().getRegistryNameComponent() + "_factory", () -> new MMBlockFactoryMachine.MMBlockFactory<>(type), MMItemBlockFactory::new);
        factory.forItemHolder(holder -> {
            int processes = tier.processes;
            Predicate<ItemStack> recipeInputPredicate = switch (type.getMMFactoryType()) {
                case RECYCLER -> s -> MMRecipeType.RECYCLER.getInputCache().containsInput(null, s);
                case PLANTING_STATION -> s -> MMRecipeType.PLANTING_STATION.getInputCache().containsInputA(null, s);
                case CNC_STAMPER -> s -> MMRecipeType.STAMPING.getInputCache().containsInput(null, s);
                case CNC_LATHE -> s -> MMRecipeType.LATHE.getInputCache().containsInput(null, s);
                case CNC_ROLLING_MILL -> s -> MMRecipeType.ROLLING_MILL.getInputCache().containsInput(null, s);
            };
            switch (type.getMMFactoryType()) {
                case CNC_STAMPER, CNC_LATHE, CNC_ROLLING_MILL -> holder.addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                        .addBasicFactorySlots(processes, recipeInputPredicate)
                        .addEnergy()
                        .build()
                );
                case RECYCLER -> holder.addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                        .addBasicFactorySlots(processes, recipeInputPredicate, true)
                        .addEnergy()
                        .build()
                );
                case PLANTING_STATION -> holder
                        .addAttachmentOnlyContainers(ContainerType.CHEMICAL, () -> MMChemicalTanksBuilder.builder()
                                .addBasic(MMTileEntityAdvancedElectricMachine.MAX_GAS * processes, switch (type.getMMFactoryType()) {
                                    case PLANTING_STATION -> MMRecipeType.PLANTING_STATION;
                                    default -> throw new IllegalStateException("Factory type doesn't have a known gas recipe.");
                                }, MMInputRecipeCache.ItemChemical::containsInputB)
                                .build()
                        ).addAttachmentOnlyContainers(ContainerType.ITEM, () -> MMItemSlotsBuilder.builder()
                                .addBasicFactorySlots(processes, recipeInputPredicate, true)
                                .addChemicalFillOrConvertSlot(0)
                                .addEnergy()
                                .build()
                        );
            }
        });
        return factory;
    }

    public static final BlockRegistryObject<BlockTile.BlockTileModel<TileEntityAmbientGasCollector, Machine<TileEntityAmbientGasCollector>>, ItemBlockTooltip<BlockTile.BlockTileModel<TileEntityAmbientGasCollector, Machine<TileEntityAmbientGasCollector>>>> AMBIENT_GAS_COLLECTOR =
            MM_BLOCKS.registerDetails("ambient_gas_collector", () -> new BlockTile.BlockTileModel<>(MMBlockTypes.AMBIENT_GAS_COLLECTOR, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())))
                    .forItemHolder(holder -> holder
                            .addAttachmentOnlyContainers(ContainerType.CHEMICAL, () -> ChemicalTanksBuilder.builder()
                                    .addBasic(TileEntityAmbientGasCollector.MAX_CHEMICAL)
                                    .build()
                            ).addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                                    .addChemicalDrainSlot(0)
                                    .addEnergy()
                                    .build()
                            )
                    );

    public static final BlockRegistryObject<BlockDoll, ItemBlockDoll> AUTHOR_DOLL = MM_BLOCKS.register("author_doll",
            () -> new BlockDoll(MMBlockTypes.AUTHOR_DOLL, properties -> properties.sound(SoundType.WOOL).destroyTime(0).strength(0)), ItemBlockDoll::new);

    private static <BLOCK extends Block, ITEM extends BlockItem> BlockRegistryObject<BLOCK, ITEM> registerTieredBlock(ITier tier, String suffix,
                                                                                                                      Supplier<? extends BLOCK> blockSupplier, BiFunction<BLOCK, Item.Properties, ITEM> itemCreator) {
        return MM_BLOCKS.register(tier.getBaseTier().getLowerName() + suffix, blockSupplier, itemCreator);
    }

    /**
     * Retrieves a Factory with a defined tier and recipe type.
     *
     * @param tier - tier to add to the Factory
     * @param type - recipe type to add to the Factory
     *
     * @return factory with defined tier and recipe type
     */
    public static BlockRegistryObject<MMBlockFactoryMachine.MMBlockFactory<?>, MMItemBlockFactory> getFactory(@NotNull FactoryTier tier, @NotNull MMFactoryType type) {
        return MM_FACTORIES.get(tier, type);
    }

    @SuppressWarnings("unchecked")
    public static BlockRegistryObject<MMBlockFactoryMachine.MMBlockFactory<?>, MMItemBlockFactory>[] getFactoryBlocks() {
        return MM_FACTORIES.values().toArray(new BlockRegistryObject[0]);
    }
}
