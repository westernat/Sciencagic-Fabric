package org.mesdag.scma.screen.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.mesdag.scma.item.energy.IChargeable;

public class ChargerableSlot extends Slot {
    public ChargerableSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public int getMaxItemCount() {
        return 1;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.getItem() instanceof IChargeable;
    }
}
