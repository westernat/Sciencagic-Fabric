package org.mesdag.scma.block.energy.entity.generator;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.mesdag.scma.registry.BlockRegistry;
import org.mesdag.scma.screen.ThermalGeneratorHandler;

import static org.mesdag.scma.util.Maps.burnableItems;

public class ThermalGeneratorEntity extends AbstractGeneratorEntity {
    public static final int size = 2;

    public ThermalGeneratorEntity(BlockPos pos, BlockState state) {
        super(100000, 1000, size, 100, BlockRegistry.thermal_generator_entity, pos, state);
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, ThermalGeneratorEntity entity) {
        // 缓存能量
        ItemStack fuelStack = entity.getStack(1);
        Item fuel = fuelStack.getItem();
        if (!fuelStack.isEmpty() && burnableItems.containsKey(fuel)) {
            long shouldInsert = (long) (burnableItems.get(fuel) * 2.5);
            if (entity.energyBuffer + shouldInsert <= entity.capacity) {
                entity.energyBuffer += shouldInsert;
                if (fuel instanceof BucketItem) {
                    entity.setStack(1, new ItemStack(Items.BUCKET));
                } else {
                    fuelStack.increment(-1);
                }
            }
        }
        // 更新方块状态
        entity.updateState(world, pos, state);
        // 先给电池充能
        entity.chargingBattery();
        // 再给自己充电
        entity.chargingSelf();
        // 更新进度条
        entity.updateGenerationTime();
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText("block.scma.thermal_generator");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new ThermalGeneratorHandler(syncId, inv, this, this.propertyDelegate);
    }
}
