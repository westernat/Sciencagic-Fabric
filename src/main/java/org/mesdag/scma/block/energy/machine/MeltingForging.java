package org.mesdag.scma.block.energy.machine;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.mesdag.scma.block.energy.entity.machine.MeltingForgingEntity;
import org.mesdag.scma.registry.BlockRegistry;

import java.util.List;

public class MeltingForging extends AbstractFacingMachineBlock {
    public MeltingForging() {
        super();
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(new TranslatableText("energy.capacity").append("50000"));
        tooltip.add(new TranslatableText("energy.io").append("100"));
        tooltip.add(new TranslatableText("energy.energy").append(Long.toString(stack.getOrCreateNbt().getCompound("BlockEntityTag").getLong("energy"))));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MeltingForgingEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, BlockRegistry.melting_forging_entity, MeltingForgingEntity::serverTick);
    }
}
