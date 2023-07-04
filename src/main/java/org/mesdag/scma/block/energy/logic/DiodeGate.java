package org.mesdag.scma.block.energy.logic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;

import static org.mesdag.scma.util.DirLists.*;

public class DiodeGate extends LogicGate {
    protected static final BooleanProperty INPUT = BooleanProperty.of("input");

    public DiodeGate() {
        super();
        setDefaultState(getStateManager().getDefaultState().with(INPUT, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(INPUT);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        Direction dir = getInputDirs(state).get(0);
        state = state.with(INPUT, isEmittingSignal(ctx.getWorld().getBlockState(ctx.getBlockPos().offset(dir)), dir));
        return state;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction dir, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!world.isClient()) {
            ArrayList<Direction> inputDirs = getInputDirs(state);
            if (inputDirs.contains(dir)) {
                return state.with(INPUT, isEmittingSignal(world.getBlockState(pos.offset(dir)), dir));
            }
        }
        return state;
    }

    @Override
    public ArrayList<Direction> getInputDirs(BlockState state) {
        switch (state.get(FACING)) {
            case SOUTH -> {
                return southList;
            }
            case WEST -> {
                return westList;
            }
            case EAST -> {
                return eastList;
            }
            default -> {
                return northList;
            }
        }
    }

    @Override
    public ArrayList<Direction> getOutputDirs(BlockState state) {
        ArrayList<Direction> dirList;
        switch (state.get(FACING)) {
            case SOUTH -> dirList = northList;
            case WEST -> dirList = eastList;
            case EAST -> dirList = westList;
            default -> dirList = southList;
        }
        if (state.get(INPUT)) return dirList;
        else return emptyList;
    }

    @Override
    public ArrayList<Direction> getConnectableDirs(BlockState state) {
        switch (state.get(FACING)) {
            case WEST, EAST -> {
                return eastWestList;
            }
            default -> {
                return northSouthList;
            }
        }
    }
}
