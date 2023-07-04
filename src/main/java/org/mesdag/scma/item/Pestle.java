package org.mesdag.scma.item;

import net.minecraft.item.Item;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;

import static org.mesdag.scma.registry.Groups.ITEMS;

public class Pestle extends ToolItem {
    private final int level;

    public Pestle(int level, ToolMaterial material) {
        super(material, new Item.Settings().group(ITEMS).maxCount(1));
        this.level = level;
    }

    public int getLevel() {
        return level;
    }


}
