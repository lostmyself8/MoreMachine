package com.jerry.mekmm.api.chemical.chemicals;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenCustomHashSet;
import mekanism.api.chemical.ChemicalStack;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class ChemicalStackLinkedSet {

    public static final Hash.Strategy<? super ChemicalStack> TYPE_AND_COMPONENTS = new Hash.Strategy<ChemicalStack>() {
        public int hashCode(@Nullable ChemicalStack stack) {
            // hashCode会在空时返回0，因此不需要判断非空
            return stack.hashCode();
        }

        public boolean equals(@Nullable ChemicalStack first, @Nullable ChemicalStack second) {
            return first == second || first != null && second != null && first.isEmpty() == second.isEmpty() && ChemicalStack.isSameChemical(first, second);
        }
    };

    public ChemicalStackLinkedSet() {
    }

    public static Set<ChemicalStack> createTypeAndComponentsSet() {
        return new ObjectLinkedOpenCustomHashSet(TYPE_AND_COMPONENTS);
    }
}
