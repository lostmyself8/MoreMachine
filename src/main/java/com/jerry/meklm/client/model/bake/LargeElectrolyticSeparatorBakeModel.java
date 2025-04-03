package com.jerry.meklm.client.model.bake;

import mekanism.api.annotations.NothingNullByDefault;
import mekanism.client.model.baked.ExtensionBakedModel;
import net.minecraft.client.resources.model.BakedModel;

@NothingNullByDefault
public class LargeElectrolyticSeparatorBakeModel extends ExtensionBakedModel<Void> {

    public LargeElectrolyticSeparatorBakeModel(BakedModel original) {
        super(original);
    }

    @Override
    protected ExtensionBakedModel<Void> wrapModel(BakedModel model) {
        return new LargeElectrolyticSeparatorBakeModel(model);
    }
}
