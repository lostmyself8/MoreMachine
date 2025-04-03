package com.jerry.mekmm.common.tile.factory;

import com.jerry.mekmm.common.inventory.slot.MMFactoryInputInventorySlot;
import com.jerry.mekmm.common.recipe.lookup.monitor.MMFactoryRecipeCacheLookupMonitor;
import mekanism.api.IContentsListener;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mekanism.common.tier.FactoryTier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public abstract class MMTileEntityItemToItemFactory<RECIPE extends MekanismRecipe<?>> extends MMTileEntityFactory<RECIPE> {

    protected IInputHandler<@NotNull ItemStack>[] inputHandlers;
    protected IOutputHandler<@NotNull ItemStack>[] outputHandlers;

    protected MMTileEntityItemToItemFactory(Holder<Block> blockProvider, BlockPos pos, BlockState state, List<RecipeError> errorTypes, Set<RecipeError> globalErrorTypes) {
        super(blockProvider, pos, state, errorTypes, globalErrorTypes);
    }

    @Override
    protected void addSlots(InventorySlotHelper builder, IContentsListener listener, IContentsListener updateSortingListener) {
        inputHandlers = new IInputHandler[tier.processes];
        outputHandlers = new IOutputHandler[tier.processes];
        processInfoSlots = new ProcessInfo[tier.processes];
        int baseX = tier == FactoryTier.BASIC ? 55 : tier == FactoryTier.ADVANCED ? 35 : tier == FactoryTier.ELITE ? 29 : 27;
        int baseXMult = tier == FactoryTier.BASIC ? 38 : tier == FactoryTier.ADVANCED ? 26 : 19;
        for (int i = 0; i < tier.processes; i++) {
            int xPos = baseX + (i * baseXMult);
            MMFactoryRecipeCacheLookupMonitor<RECIPE> lookupMonitor = recipeCacheLookupMonitors[i];
            IContentsListener updateSortingAndUnpause = () -> {
                updateSortingListener.onContentsChanged();
                lookupMonitor.unpause();
            };
            OutputInventorySlot outputSlot = OutputInventorySlot.at(updateSortingAndUnpause, xPos, 57);
            //Note: As we are an item factory that has comparator's based on items we can just use the monitor as a listener directly
            MMFactoryInputInventorySlot inputSlot = MMFactoryInputInventorySlot.create(this, i, outputSlot, recipeCacheLookupMonitors[i], xPos, 13);
            int index = i;
            builder.addSlot(inputSlot).tracksWarnings(slot -> slot.warning(WarningType.NO_MATCHING_RECIPE, getWarningCheck(RecipeError.NOT_ENOUGH_INPUT, index)));
            builder.addSlot(outputSlot).tracksWarnings(slot -> slot.warning(WarningType.NO_SPACE_IN_OUTPUT, getWarningCheck(RecipeError.NOT_ENOUGH_OUTPUT_SPACE, index)));
            inputHandlers[i] = InputHelper.getInputHandler(inputSlot, RecipeError.NOT_ENOUGH_INPUT);
            outputHandlers[i] = OutputHelper.getOutputHandler(outputSlot, RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
            processInfoSlots[i] = new ProcessInfo(i, inputSlot, outputSlot, null);
        }
    }
}