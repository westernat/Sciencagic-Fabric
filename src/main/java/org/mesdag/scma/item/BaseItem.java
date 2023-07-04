package org.mesdag.scma.item;

import net.minecraft.item.Item;

import static org.mesdag.scma.registry.Groups.ITEMS;


public class BaseItem extends Item {
    public BaseItem() {
        super(new Item.Settings().group(ITEMS));
    }
}
