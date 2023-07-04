package org.mesdag.scma.screen.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

import static org.mesdag.scma.util.Maps.burnableItems;

public class BurnableSlot extends Slot {
    public BurnableSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return burnableItems.containsKey(stack.getItem());
    }
}
