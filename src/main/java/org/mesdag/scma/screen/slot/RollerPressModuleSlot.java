package org.mesdag.scma.screen.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.mesdag.scma.screen.RollerPressHandler;

public class RollerPressModuleSlot extends Slot {
    private final RollerPressHandler handler;

    public RollerPressModuleSlot(RollerPressHandler handler, Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.handler = handler;
    }

    public boolean canInsert(ItemStack stack) {
        return handler.isModule(stack);
    }

    public int getMaxItemCount(ItemStack stack) {
        return 1;
    }
}
