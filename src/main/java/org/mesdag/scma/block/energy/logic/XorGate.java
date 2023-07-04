package org.mesdag.scma.block.energy.logic;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;

public class XorGate extends TriodeGate {
    public XorGate() {
        super();
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
        if (state.get(INPUT_A) ^ state.get(INPUT_B)) return new ArrayList<>(List.of(dir));
        else return new ArrayList<>();
    }
}
