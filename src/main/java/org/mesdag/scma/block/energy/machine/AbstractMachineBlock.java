package org.mesdag.scma.block.energy.machine;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.mesdag.scma.block.ImplInventory;
import org.mesdag.scma.block.energy.IConnectable;
import org.mesdag.scma.block.energy.entity.machine.AbstractMachineEntity;

import java.util.ArrayList;

import static org.mesdag.scma.util.DirLists.downList;

public abstract class AbstractMachineBlock extends BlockWithEntity implements IConnectable {
    public static final BooleanProperty LIT = Properties.LIT;


    public AbstractMachineBlock() {
        super(FabricBlockSettings.of(Material.METAL).luminance(state -> state.get(Properties.LIT) ? 7 : 0).strength(1.0f).sounds(BlockSoundGroup.METAL));
        setDefaultState(getDefaultState().with(LIT, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
            if (screenHandlerFactory != null) {
                player.openHandledScreen(screenHandlerFactory);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ImplInventory I) {
                ItemScatterer.spawn(world, pos, I);
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
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
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }

    @Override
    public ArrayList<Direction> getConnectableDirs(BlockState state) {
        return downList;
    }
}
