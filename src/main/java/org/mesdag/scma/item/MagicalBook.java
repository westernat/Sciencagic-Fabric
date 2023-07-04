package org.mesdag.scma.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static org.mesdag.scma.registry.Groups.ITEMS;

public class MagicalBook extends Item {
    public MagicalBook() {
        super(new Item.Settings().group(ITEMS).maxCount(1).rarity(Rarity.RARE));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbt = stack.getOrCreateNbt();

        if (nbt.getBoolean("final")) {
            tooltip.add(new TranslatableText("item.scma.programmable_book.final").formatted(Formatting.GOLD));
        } else {
            tooltip.add(new TranslatableText("item.scma.programmable_book.void").formatted(Formatting.AQUA));
        }
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return stack.getOrCreateNbt().getBoolean("final");
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        NbtCompound nbt = new NbtCompound();
        nbt.put("code", new NbtCompound());
        nbt.putBoolean("final", false);
        stack.setNbt(nbt);
    }
}
