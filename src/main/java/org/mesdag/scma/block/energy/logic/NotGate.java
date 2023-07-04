package org.mesdag.scma.block.energy.logic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;
import java.util.List;

public class NotGate extends DiodeGate {
    private static final BooleanProperty LOCKED = BooleanProperty.of("locked");
    private final Test test;

    public NotGate() {
        super();
        this.test = new Test(0, 0);
        setDefaultState(getStateManager().getDefaultState().with(LOCKED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(LOCKED);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction dir, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!state.get(LOCKED)) {
            ArrayList<Direction> inputDirs = getInputDirs(state);
            if (inputDirs.contains(dir)) {
                state = state.with(INPUT, isEmittingSignal(world.getBlockState(pos.offset(dir)), dir));
            }
            if (test.counter > 15) {
                if (((World) world).getTime() - test.time < 10L) {
                    state = state.with(LOCKED, true);
                } else {
                    test.time = ((World) world).getTime();
                    test.counter = 0;
                }
            } else {
                ++test.counter;
            }
        }
        return state;
    }

    @Override
    public ArrayList<Direction> getOutputDirs(BlockState state) {
        Direction dir;
        switch (state.get(FACING)) {
            case SOUTH -> dir = Direction.NORTH;
            case WEST -> dir = Direction.EAST;
            case EAST -> dir = Direction.WEST;
            default -> dir = Direction.SOUTH;
        }
        if (state.get(INPUT)) return new ArrayList<>();
        else return new ArrayList<>(List.of(dir));
    }

    static class Test {
        long time;
        int counter;

        public Test(long time, int counter) {
            this.time = time;
            this.counter = counter;
        }
    }
}
