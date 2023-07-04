package org.mesdag.scma.block.energy;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;

import static org.mesdag.scma.util.DirLists.allList;
import static org.mesdag.scma.util.DirLists.emptyList;

public interface IConnectable {
    default ArrayList<Direction> getInputDirs(BlockState state) {
        return emptyList;
    }

    default ArrayList<Direction> getOutputDirs(BlockState state) {
        return emptyList;
    }

    default ArrayList<Direction> getConnectableDirs(BlockState state) {
        return allList;
    }
}
