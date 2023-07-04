package org.mesdag.scma.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import org.mesdag.scma.SCMA;
import org.mesdag.scma.block.energy.entity.generator.ThermalGeneratorEntity;
import org.mesdag.scma.screen.slot.BurnableSlot;
import org.mesdag.scma.screen.slot.ChargerableSlot;

public class ThermalGeneratorHandler extends AbstractGeneratorHandler {
    public ThermalGeneratorHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(ThermalGeneratorEntity.size), new ArrayPropertyDelegate(3));
    }

    public ThermalGeneratorHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(SCMA.thermal_generator_handler, syncId, ThermalGeneratorEntity.size, playerInventory, inventory, propertyDelegate);
        addSlot(new ChargerableSlot(inventory, 0, 80, 9)); // 充电槽
        addSlot(new BurnableSlot(inventory, 1, 80, 53)); // 燃料槽
    }

    @Override
    public int getEnergyProgress() {
        return propertyDelegate.get(2) * 36 / 100000;
    }

    @Override
    public int getGenerationProgress() {
        int i = propertyDelegate.get(0);
        int j = propertyDelegate.get(1);
        return i != 0 && j != 0 ? i * 18 / j : 0;
    }
}
