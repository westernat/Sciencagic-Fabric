package org.mesdag.scma.block.energy.logic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.mesdag.scma.block.energy.IConnectable;

import static org.mesdag.scma.block.energy.logic.LogicCable.CONNECTED;


public interface ILogic extends IConnectable {
    default boolean isReceivingSignal(World world, BlockPos pos) {
        for (Direction dir : Direction.values()) {
            if (isEmittingSignal(world.getBlockState(pos.offset(dir)), dir))
                return true;
        }
        return false;
    }

    default boolean isEmittingSignal(BlockState neighborState, Direction dir) {
        Block neighborBlock = neighborState.getBlock();
        if (neighborBlock instanceof LogicCable LC) {
            return neighborState.get(CONNECTED) < 2 && LC.getOutputDirs(neighborState).contains(dir.getOpposite());
        } else if (neighborBlock instanceof IConnectable C) {
            return C.getOutputDirs(neighborState).contains(dir.getOpposite());
        }
        return false;
    }
}
