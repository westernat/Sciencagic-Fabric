package org.mesdag.scma.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.slot.Slot;
import org.mesdag.scma.SCMA;
import org.mesdag.scma.block.energy.entity.machine.RollerPressEntity;
import org.mesdag.scma.screen.slot.MachineResultSlot;
import org.mesdag.scma.screen.slot.RollerPressModuleSlot;

import static org.mesdag.scma.block.energy.entity.machine.RollerPressEntity.roller_press_modules;

public class RollerPressHandler extends AbstractMachineHandler {
    public RollerPressHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(RollerPressEntity.size), new ArrayPropertyDelegate(3));
    }

    public RollerPressHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(SCMA.roller_press_handler, syncId, RollerPressEntity.size, playerInventory, inventory, propertyDelegate);
        addSlot(new Slot(inventory, 1, 53, 39)); // 原料槽
        addSlot(new RollerPressModuleSlot(this, inventory, 2, 80, 18)); // 模块槽
        addSlot(new MachineResultSlot(playerInventory.player, inventory, 3, 107, 39)); // 产物槽
    }

    public boolean isModule(ItemStack stack) {
        return roller_press_modules.contains(stack.getItem());
    }

    @Override
    public int getEnergyProgress() {
        return propertyDelegate.get(2) * 36 / 50000;
    }

    @Override
    public int getGenerationProgress() {
        int i = propertyDelegate.get(0);
        int j = propertyDelegate.get(1);
        return i != 0 && j != 0 ? i * 24 / j : 0;
    }
}
