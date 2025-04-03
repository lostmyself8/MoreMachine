package com.jerry.mekmm.common.content.blocktype;

import com.jerry.mekmm.common.block.attribute.MMAttributeFactoryType;
import com.jerry.mekmm.common.registries.MMBlocks;
import mekanism.api.text.ILangEntry;
import mekanism.common.block.attribute.*;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.lib.math.Pos3D;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;

import java.util.Objects;
import java.util.function.Supplier;

public class MMMachine<TILE extends TileEntityMekanism> extends BlockTypeTile<TILE> {

    public MMMachine(Supplier<TileEntityTypeRegistryObject<TILE>> tileEntityRegistrar, ILangEntry description) {
        super(tileEntityRegistrar, description);
        add(new AttributeParticleFX()
                .add(ParticleTypes.SMOKE, rand -> new Pos3D(rand.nextFloat() * 0.6F - 0.3F, rand.nextFloat() * 6.0F / 16.0F, 0.52))
                .add(DustParticleOptions.REDSTONE, rand -> new Pos3D(rand.nextFloat() * 0.6F - 0.3F, rand.nextFloat() * 6.0F / 16.0F, 0.52)));
        add(Attributes.ACTIVE_LIGHT, new AttributeStateFacing(), Attributes.INVENTORY, Attributes.SECURITY, Attributes.REDSTONE, Attributes.COMPARATOR,
                AttributeUpgradeSupport.DEFAULT_MACHINE_UPGRADES);
    }

    public static class MMFactoryMachine<TILE extends TileEntityMekanism> extends MMMachine<TILE> {

        public MMFactoryMachine(Supplier<TileEntityTypeRegistryObject<TILE>> tileEntitySupplier, ILangEntry description, MMFactoryType factoryType) {
            super(tileEntitySupplier, description);
            add(new MMAttributeFactoryType(factoryType), new AttributeUpgradeable(() -> MMBlocks.getFactory(FactoryTier.BASIC, getMMFactoryType())));
        }

        public MMFactoryType getMMFactoryType() {
            return Objects.requireNonNull(get(MMAttributeFactoryType.class)).getMMFactoryType();
        }
    }

    public static class MMMachineBuilder<MACHINE extends MMMachine<TILE>, TILE extends TileEntityMekanism, T extends MMMachineBuilder<MACHINE, TILE, T>> extends BlockTileBuilder<MACHINE, TILE, T> {

        protected MMMachineBuilder(MACHINE holder) {
            super(holder);
        }

        public static <TILE extends TileEntityMekanism> MMMachineBuilder<MMMachine<TILE>, TILE, ?> createMMMachine(Supplier<TileEntityTypeRegistryObject<TILE>> tileEntityRegistrar,
                                                                                                                     ILangEntry description) {
            return new MMMachineBuilder<>(new MMMachine<>(tileEntityRegistrar, description));
        }

        public static <TILE extends TileEntityMekanism> MMMachineBuilder<MMFactoryMachine<TILE>, TILE, ?> createMMFactoryMachine(Supplier<TileEntityTypeRegistryObject<TILE>> tileEntityRegistrar,
                                                                                                                                           ILangEntry description, MMFactoryType factoryType) {
            return new MMMachineBuilder<>(new MMFactoryMachine<>(tileEntityRegistrar, description, factoryType));
        }
    }
}
