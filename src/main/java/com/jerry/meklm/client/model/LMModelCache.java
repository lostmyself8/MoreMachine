package com.jerry.meklm.client.model;

import com.jerry.mekmm.Mekmm;
import mekanism.client.model.BaseModelCache;
import net.neoforged.neoforge.client.event.ModelEvent;

import java.util.HashSet;
import java.util.Set;

public class LMModelCache extends BaseModelCache {

    public static final LMModelCache INSTANCE = new LMModelCache();
    private final Set<Runnable> callbacks = new HashSet<>();

    public final JSONModelData LARGE_ELECTROLYTIC_SEPARATOR = registerJSON("block/large_electrolytic_separator");
    public final JSONModelData LARGE_HEAT_GENERATOR = registerJSON("block/large_heat_generator");

    private LMModelCache() {
        super(Mekmm.MOD_ID);
    }

    @Override
    public void onBake(ModelEvent.BakingCompleted evt) {
        super.onBake(evt);
        callbacks.forEach(Runnable::run);
    }

    public void reloadCallback(Runnable callback) {
        callbacks.add(callback);
    }
}
