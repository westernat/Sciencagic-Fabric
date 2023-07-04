package org.mesdag.scma.block.energy.generator;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.mesdag.scma.block.energy.entity.generator.ThermalGeneratorEntity;
import org.mesdag.scma.registry.BlockRegistry;

import java.util.List;

public class ThermalGenerator extends AbstractGeneratorBlock {
    public ThermalGenerator() {
        super();
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ThermalGeneratorEntity(pos, state);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(new TranslatableText("energy.capacity").append("200000"));
        tooltip.add(new TranslatableText("energy.io").append("1000"));
        NbtCompound tag = stack.getOrCreateNbt().getCompound("BlockEntityTag");
        tooltip.add(new TranslatableText("energy.energy").append(Long.toString(tag.getLong("energy") + tag.getLong("energyBuffer"))));
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, BlockRegistry.thermal_generator_entity, ThermalGeneratorEntity::serverTick);
    }
}
