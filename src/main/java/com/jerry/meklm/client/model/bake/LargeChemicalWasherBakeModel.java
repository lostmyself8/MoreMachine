package com.jerry.meklm.client.model.bake;

import mekanism.api.annotations.NothingNullByDefault;
import mekanism.client.model.baked.ExtensionBakedModel;
import net.minecraft.client.resources.model.BakedModel;

@NothingNullByDefault
public class LargeChemicalWasherBakeModel extends ExtensionBakedModel<Void> {
    public LargeChemicalWasherBakeModel(BakedModel original) {
        super(original);
    }

    @Override
    protected ExtensionBakedModel<Void> wrapModel(BakedModel model) {
        return new LargeChemicalWasherBakeModel(model);
    }
}
