package org.mesdag.scma.block.energy.generator;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.mesdag.scma.block.energy.IConnectable;
import org.mesdag.scma.block.energy.entity.generator.SolarPanelEntity;
import org.mesdag.scma.block.energy.entity.machine.AbstractMachineEntity;
import org.mesdag.scma.registry.BlockRegistry;

import java.util.ArrayList;
import java.util.List;

import static org.mesdag.scma.util.DirLists.downList;

public class SolarPanel extends BlockWithEntity implements IConnectable {
    public SolarPanel() {
        super(FabricBlockSettings.of(Material.METAL).strength(1.0f).sounds(BlockSoundGroup.METAL));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(new TranslatableText("energy.capacity").append("20000"));
        tooltip.add(new TranslatableText("energy.io").append("100"));
        tooltip.add(new TranslatableText("energy.energy").append(Long.toString(stack.getOrCreateNbt().getCompound("BlockEntityTag").getLong("energy"))));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SolarPanelEntity(pos, state);
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        if (blockEntity instanceof AbstractMachineEntity base) {
            NbtCompound nbt = new NbtCompound();
            base.writeNbt(nbt);
            NbtCompound tag2 = new NbtCompound();
            tag2.putLong("energy", nbt.getLong("energy"));
            NbtCompound tag = new NbtCompound();
            tag.put("BlockEntityTag", tag2);
            stack.setNbt(tag);
        }
    }

    @Override
    public ArrayList<Direction> getConnectableDirs(BlockState state) {
        return downList;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, BlockRegistry.solar_panel_entity, SolarPanelEntity::serverTick);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.0625, 0, 0.0625, 0.9375, 0.625, 0.9375);
    }
}
