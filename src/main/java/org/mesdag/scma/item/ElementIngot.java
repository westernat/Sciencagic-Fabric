package org.mesdag.scma.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;

import static org.mesdag.scma.registry.Groups.ITEMS;

public class ElementIngot extends Item {
    public ElementIngot() {
        super(new Item.Settings().group(ITEMS).maxCount(32).fireproof().rarity(Rarity.RARE));
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}