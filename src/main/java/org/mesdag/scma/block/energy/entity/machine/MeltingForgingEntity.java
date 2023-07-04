package org.mesdag.scma.block.energy.entity.machine;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
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
import org.mesdag.scma.screen.MeltingForgingHandler;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MeltingForgingEntity extends AbstractFacingMachineEntity {
    public static final int size = 7;

    public static final Map<Set<Item>, Item> melting_forging_result = Map.of(
            Set.of(Items.COBBLESTONE), Items.STONE
    );

    public MeltingForgingEntity(BlockPos pos, BlockState state) {
        super(50000, 100, size, 400, BlockRegistry.melting_forging_entity, pos, state);
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, MeltingForgingEntity entity) {
        entity.getEnergyFromBattery();
        int amount = 0;
        boolean isNotEmpty = false;
        HashSet<Item> inputItems = new HashSet<>();
        for (int i = 1; i < 6; ++i) {
            ItemStack itemStack = entity.getStack(i);
            if (!itemStack.isEmpty()) {
                ++amount;
                isNotEmpty = true;
                inputItems.add(itemStack.getItem());
            }
        }
        if (entity.hasEnergy() && isNotEmpty) {
            ItemStack outputStack = entity.getStack(6);
            if (melting_forging_result.containsKey(inputItems) && entity.couldContinue() && outputStack.getCount() + amount <= outputStack.getMaxCount()) {
                entity.startGenerating();
                entity.tryExtract(amount * 2L);
            } else if (entity.shouldComplete()) {
                Item resultItem = melting_forging_result.get(inputItems);
                if (outputStack.isEmpty()) {
                    for (int i = 1; i < 6; ++i) entity.getStack(i).increment(-1);
                    entity.setStack(6, new ItemStack(resultItem, amount));
                    entity.generationTime = 0;
                } else if (outputStack.isOf(resultItem)) {
                    for (int i = 1; i < 6; ++i) entity.getStack(i).increment(-1);
                    outputStack.increment(amount);
                    entity.generationTime = 0;
                }
            } else entity.stopGenerating();
        } else entity.stopGenerating();
        entity.updateState(world, pos, state);
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText("block.scma.melting_forging");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new MeltingForgingHandler(syncId, inv, this, this.propertyDelegate);
    }
}
