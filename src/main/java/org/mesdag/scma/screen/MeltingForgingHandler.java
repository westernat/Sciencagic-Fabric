package org.mesdag.scma.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.slot.Slot;
import org.mesdag.scma.SCMA;
import org.mesdag.scma.block.energy.entity.machine.MeltingForgingEntity;
import org.mesdag.scma.screen.slot.MachineResultSlot;

public class MeltingForgingHandler extends AbstractMachineHandler {
    public MeltingForgingHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(MeltingForgingEntity.size), new ArrayPropertyDelegate(3));
    }

    public MeltingForgingHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(SCMA.melting_forging_handler, syncId, MeltingForgingEntity.size, playerInventory, inventory, propertyDelegate);
        addSlot(new Slot(inventory, 1, 62, 5));
        addSlot(new Slot(inventory, 2, 35, 8));
        addSlot(new Slot(inventory, 3, 32, 35));
        addSlot(new Slot(inventory, 4, 35, 62));
        addSlot(new Slot(inventory, 5, 62, 65));
        addSlot(new MachineResultSlot(playerInventory.player, inventory, 6, 107, 35));
    }

    @Override
    public int getEnergyProgress() {
        return propertyDelegate.get(2) * 36 / 50000;
    }

    @Override
    public int getGenerationProgress() {
        int i = propertyDelegate.get(0);
        int j = propertyDelegate.get(1);
        return i != 0 && j != 0 ? i * 37 / j : 0;
    }
}
