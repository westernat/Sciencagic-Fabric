package org.mesdag.scma.block.energy.logic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;

import static org.mesdag.scma.util.DirLists.*;

public class TriodeGate extends LogicGate {
    // 左边为B, 右边为A
    protected static final BooleanProperty INPUT_A = BooleanProperty.of("input_a");
    protected static final BooleanProperty INPUT_B = BooleanProperty.of("input_b");


    public TriodeGate() {
        super();
        setDefaultState(getStateManager().getDefaultState().with(INPUT_A, false).with(INPUT_B, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(INPUT_A, INPUT_B);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        World world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();
        for (Direction dir : getInputDirs(state)) {
            state = getState(state, dir, world.getBlockState(pos.offset(dir)));
        }
        return state;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction dir, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!world.isClient()) {
            ArrayList<Direction> inputDirs = getInputDirs(state);
            if (inputDirs.contains(dir)) return getState(state, dir, neighborState);
        }
        return state;
    }

    private BlockState getState(BlockState state, Direction dir, BlockState neighborState) {
        boolean value = isEmittingSignal(neighborState, dir);
        switch (state.get(FACING)) {
            case SOUTH -> {
                if (dir == Direction.WEST) state = state.with(INPUT_B, value);
                else if (dir == Direction.EAST) state = state.with(INPUT_A, value);
            }
            case WEST -> {
                if (dir == Direction.NORTH) state = state.with(INPUT_B, value);
                else if (dir == Direction.SOUTH) state = state.with(INPUT_A, value);
            }
            case EAST -> {
                if (dir == Direction.NORTH) state = state.with(INPUT_A, value);
                else if (dir == Direction.SOUTH) state = state.with(INPUT_B, value);
            }
            default -> {
                if (dir == Direction.WEST) state = state.with(INPUT_A, value);
                else if (dir == Direction.EAST) state = state.with(INPUT_B, value);
            }
        }
        return state;
    }

    @Override
    public ArrayList<Direction> getInputDirs(BlockState state) {
        switch (state.get(FACING)) {
            case EAST, WEST -> {
                return northSouthList;
            }
            default -> {
                return eastWestList;
            }
        }
    }

    @Override
    public ArrayList<Direction> getOutputDirs(BlockState state) {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Direction> getConnectableDirs(BlockState state) {
        switch (state.get(FACING)) {
            case SOUTH -> {
                return northEastWestList;
            }
            case WEST -> {
                return northSouthEastList;
            }
            case EAST -> {
                return northSouthWestList;
            }
            default -> {
                return southEastWestList;
            }
        }
    }
}
