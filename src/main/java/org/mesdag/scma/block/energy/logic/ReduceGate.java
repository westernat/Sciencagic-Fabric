package org.mesdag.scma.block.energy.logic;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;

public class ReduceGate extends PentodeGate {
    public ReduceGate() {
        super();
    }

    @Override
    public ArrayList<Direction> getOutputDirs(BlockState state) {
        ArrayList<Direction> dirs;
        int value = (state.get(INPUT_A) ? 1 : 0) + (state.get(INPUT_B) ? -1 : 0) + (state.get(INPUT_C) ? -1 : 0);
        if (value == 1) {
            switch (state.get(FACING)) {
                case SOUTH -> dirs = new ArrayList<>(List.of(Direction.NORTH));
                case WEST -> dirs = new ArrayList<>(List.of(Direction.EAST));
                case EAST -> dirs = new ArrayList<>(List.of(Direction.WEST));
                default -> dirs = new ArrayList<>(List.of(Direction.SOUTH));
            }
        } else if (value == 0) {
            dirs = new ArrayList<>();
        } else if (value == -1) {
            switch (state.get(FACING)) {
                case SOUTH -> dirs = new ArrayList<>(List.of(Direction.NORTH, Direction.WEST));
                case WEST -> dirs = new ArrayList<>(List.of(Direction.EAST, Direction.NORTH));
                case EAST -> dirs = new ArrayList<>(List.of(Direction.WEST, Direction.SOUTH));
                default -> dirs = new ArrayList<>(List.of(Direction.SOUTH, Direction.EAST));
            }
        } else {// value == -2
            switch (state.get(FACING)) {
                case SOUTH -> dirs = new ArrayList<>(List.of(Direction.WEST));
                case WEST -> dirs = new ArrayList<>(List.of(Direction.NORTH));
                case EAST -> dirs = new ArrayList<>(List.of(Direction.SOUTH));
                default -> dirs = new ArrayList<>(List.of(Direction.EAST));
            }
        }
        return dirs;
    }
}
