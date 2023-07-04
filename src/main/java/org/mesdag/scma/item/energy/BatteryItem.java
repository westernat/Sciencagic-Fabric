package org.mesdag.scma.item.energy;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static org.mesdag.scma.registry.Groups.ITEMS;

public class BatteryItem extends Item implements IChargeable {
    protected long capacity;
    protected long io;

    public BatteryItem(long capacity, long io) {
        super(new Item.Settings().group(ITEMS).maxCount(1));
        this.capacity = capacity;
        this.io = io;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new TranslatableText("energy.capacity").append(Long.toString(capacity)));
        tooltip.add(new TranslatableText("energy.io").append(Long.toString(io)));
        tooltip.add(new TranslatableText("energy.energy").append(Long.toString(stack.getOrCreateNbt().getLong("energy"))));
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        stack.getOrCreateNbt().putLong("energy", 0);
    }

    @Override
    public long getCapacity() {
        return capacity;
    }

    @Override
    public long getEnergyIO() {
        return io;
    }
}
