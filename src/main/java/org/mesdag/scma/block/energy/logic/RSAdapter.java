package org.mesdag.scma.block.energy.logic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.mesdag.scma.item.Wrench;

public class RSAdapter extends DiodeGate {
    private static final BooleanProperty R2S = BooleanProperty.of("r2s");

    public RSAdapter() {
        super();
        setDefaultState(getStateManager().getDefaultState().with(R2S, true));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(R2S);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        Direction dir = getInputDirs(state).get(0);
        state = state.with(INPUT, ctx.getWorld().isEmittingRedstonePower(ctx.getBlockPos().offset(dir), dir));
        return state;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction dir, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (getInputDirs(state).contains(dir)) {
            if (state.get(R2S)) {
                state = state.with(INPUT, ((World) world).isEmittingRedstonePower(neighborPos, dir));
            } else {
                state = state.with(INPUT, isEmittingSignal(neighborState, dir));
            }
        }
        return state;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (world.getBlockState(fromPos).getBlock() != Blocks.REDSTONE_WIRE) {
            Direction dir = Direction.fromVector(fromPos.subtract(pos));
            if (getInputDirs(state).contains(dir)) {
                if (state.get(R2S)) {
                    world.setBlockState(pos, state.with(INPUT, world.isReceivingRedstonePower(fromPos)));
                } else {
                    world.setBlockState(pos, state.with(INPUT, isEmittingSignal(world.getBlockState(pos.offset(dir)), dir)));
                }
            }
        }
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        Direction dir = state.get(FACING);
        BlockPos blockPos = pos.offset(dir.getOpposite());
        world.updateNeighbor(blockPos, this, pos);
        world.updateNeighborsExcept(blockPos, this, dir);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player.getStackInHand(hand).getItem() instanceof Wrench) {
            world.setBlockState(pos, state.cycle(R2S));
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return !state.get(R2S);
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction dir) {
        return dir == state.get(FACING) && state.get(INPUT) ? 15 : 0;
    }
}
