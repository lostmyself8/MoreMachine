package com.jerry.mekmm.common.inventory.slot;

import com.jerry.mekmm.common.tile.factory.MMTileEntityFactory;
import mekanism.api.IContentsListener;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.inventory.IInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@NothingNullByDefault
public class MMFactoryInputInventorySlot extends InputInventorySlot {

    public static MMFactoryInputInventorySlot create(MMTileEntityFactory<?> factory, int process, IInventorySlot outputSlot, @Nullable IContentsListener listener,
                                                   int x, int y) {
        return create(factory, process, outputSlot, null, listener, x, y);
    }

    public static MMFactoryInputInventorySlot create(MMTileEntityFactory<?> factory, int process, IInventorySlot outputSlot, @Nullable IInventorySlot secondaryOutputSlot,
                                                   @Nullable IContentsListener listener, int x, int y) {
        Objects.requireNonNull(factory, "Factory cannot be null");
        Objects.requireNonNull(outputSlot, "Primary output slot cannot be null");
        return new MMFactoryInputInventorySlot(factory, process, outputSlot, secondaryOutputSlot, listener, x, y);
    }

    private MMFactoryInputInventorySlot(MMTileEntityFactory<?> factory, int process, IInventorySlot outputSlot, @Nullable IInventorySlot secondaryOutputSlot,
                                      @Nullable IContentsListener listener, int x, int y) {
        super(stack -> factory.isItemValidForSlot(stack) && factory.inputProducesOutput(process, stack, outputSlot, secondaryOutputSlot, false),
                factory::isValidInputItem, listener, x, y);
    }
}
