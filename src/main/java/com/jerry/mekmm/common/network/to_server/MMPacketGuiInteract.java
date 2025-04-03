package com.jerry.mekmm.common.network.to_server;

import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.common.tile.factory.MMTileEntityFactory;
import com.jerry.mekmm.common.tile.machine.TileEntityReplicator;
import io.netty.buffer.ByteBuf;
import mekanism.api.functions.TriConsumer;
import mekanism.common.network.IMekanismPacket;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

/**
 * Used for informing the server that an action happened in a GUI
 */
public class MMPacketGuiInteract implements IMekanismPacket {

    public static final CustomPacketPayload.Type<MMPacketGuiInteract> TYPE = new CustomPacketPayload.Type<>(Mekmm.rl("gui_interact"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MMPacketGuiInteract> STREAM_CODEC = MMInteractionType.STREAM_CODEC.<RegistryFriendlyByteBuf>cast()
            .dispatch(packet -> packet.interactionType, type -> switch (type) {
                case ENTITY -> null;
                case INT -> StreamCodec.composite(
                        MMGuiInteraction.STREAM_CODEC, packet -> packet.interaction,
                        BlockPos.STREAM_CODEC, packet -> packet.tilePosition,
                        //TODO - 1.18?: Eventually we may want to try to make some form of this that can compact negatives better as well
                        ByteBufCodecs.VAR_INT, packet -> packet.extra,
                        MMPacketGuiInteract::new
                );
                case ITEM -> StreamCodec.composite(
                        MMGuiInteractionItem.STREAM_CODEC, packet -> packet.itemInteraction,
                        BlockPos.STREAM_CODEC, packet -> packet.tilePosition,
                        ItemStack.OPTIONAL_STREAM_CODEC, packet -> packet.extraItem,
                        MMPacketGuiInteract::new
                );
            });

    private final MMInteractionType interactionType;

    private MMGuiInteraction interaction;
    private MMGuiInteractionItem itemInteraction;
//    private MMPacketGuiInteract.GuiInteractionEntity entityInteraction;
    private BlockPos tilePosition;
    private ItemStack extraItem;
    private int entityID;
    private int extra;

//    public MMPacketGuiInteract(MMPacketGuiInteract.GuiInteractionEntity interaction, Entity entity) {
//        this(interaction, entity, 0);
//    }
//
//    public MMPacketGuiInteract(MMPacketGuiInteract.GuiInteractionEntity interaction, Entity entity, int extra) {
//        this(interaction, entity.getId(), extra);
//    }
//
//    public MMPacketGuiInteract(MMPacketGuiInteract.GuiInteractionEntity interaction, int entityID, int extra) {
//        this.interactionType = MMPacketGuiInteract.MMInteractionType.ENTITY;
//        this.entityInteraction = interaction;
//        this.entityID = entityID;
//        this.extra = extra;
//    }

    public MMPacketGuiInteract(MMGuiInteraction interaction, BlockEntity tile) {
        this(interaction, tile.getBlockPos());
    }

    public MMPacketGuiInteract(MMGuiInteraction interaction, BlockEntity tile, int extra) {
        this(interaction, tile.getBlockPos(), extra);
    }

    public MMPacketGuiInteract(MMGuiInteraction interaction, BlockPos tilePosition) {
        this(interaction, tilePosition, 0);
    }

    public MMPacketGuiInteract(MMGuiInteraction interaction, BlockPos tilePosition, int extra) {
        this.interactionType = MMPacketGuiInteract.MMInteractionType.INT;
        this.interaction = interaction;
        this.tilePosition = tilePosition;
        this.extra = extra;
    }

    public MMPacketGuiInteract(MMGuiInteractionItem interaction, BlockEntity tile, ItemStack stack) {
        this(interaction, tile.getBlockPos(), stack);
    }

    public MMPacketGuiInteract(MMGuiInteractionItem interaction, BlockPos tilePosition, ItemStack stack) {
        this.interactionType = MMPacketGuiInteract.MMInteractionType.ITEM;
        this.itemInteraction = interaction;
        this.tilePosition = tilePosition;
        this.extraItem = stack;
    }

    @Override
    public void handle(IPayloadContext context) {
        Player player = context.player();
        if (interactionType == MMInteractionType.ENTITY) {
            Entity entity = player.level().getEntity(entityID);
//            if (entity != null) {
//                entityInteraction.consume(entity, player, extra);
//            }
        } else {
            TileEntityMekanism tile = WorldUtils.getTileEntity(TileEntityMekanism.class, player.level(), tilePosition);
            if (tile != null) {
                if (interactionType == MMInteractionType.INT) {
                    interaction.consume(tile, player, extra);
                } else
                if (interactionType == MMInteractionType.ITEM) {
                    itemInteraction.consume(tile, player, extraItem);
                }
            }
        }
    }

    @Override
    public CustomPacketPayload.@NotNull Type<MMPacketGuiInteract> type() {
        return TYPE;
    }

    public enum MMGuiInteractionItem {
        DUPLICATOR_INVERSE_REPLACE_ITEM((tile, player, stack) -> {
            if (tile instanceof TileEntityReplicator duplicator) {
                duplicator.setInverseReplaceTarget(stack.getItem());
            }
        });

        public static final IntFunction<MMGuiInteractionItem> BY_ID = ByIdMap.continuous(MMGuiInteractionItem::ordinal, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
        public static final StreamCodec<ByteBuf, MMGuiInteractionItem> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, MMGuiInteractionItem::ordinal);

        private final TriConsumer<TileEntityMekanism, Player, ItemStack> consumerForTile;

        MMGuiInteractionItem(TriConsumer<TileEntityMekanism, Player, ItemStack> consumerForTile) {
            this.consumerForTile = consumerForTile;
        }

        public void consume(TileEntityMekanism tile, Player player, ItemStack stack) {
            consumerForTile.accept(tile, player, stack);
        }
    }

    public enum MMGuiInteraction {
        AUTO_SORT_BUTTON((tile, player, extra) -> {
            if (tile instanceof MMTileEntityFactory<?> factory) {
                factory.toggleSorting();
            }
        });

        public static final IntFunction<MMGuiInteraction> BY_ID = ByIdMap.continuous(MMGuiInteraction::ordinal, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
        public static final StreamCodec<ByteBuf, MMGuiInteraction> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, MMGuiInteraction::ordinal);

        private final TriConsumer<TileEntityMekanism, Player, Integer> consumerForTile;

        MMGuiInteraction(TriConsumer<TileEntityMekanism, Player, Integer> consumerForTile) {
            this.consumerForTile = consumerForTile;
        }

        public void consume(TileEntityMekanism tile, Player player, int extra) {
            consumerForTile.accept(tile, player, extra);
        }
    }

    private enum MMInteractionType {
        ENTITY,
        ITEM,
        INT;

        public static final IntFunction<MMInteractionType> BY_ID = ByIdMap.continuous(MMInteractionType::ordinal, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
        public static final StreamCodec<ByteBuf, MMInteractionType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, MMInteractionType::ordinal);
    }
}
