package org.mesdag.scma.block.energy.entity.machine;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;

import static org.mesdag.scma.util.DirLists.*;

public abstract class AbstractFacingMachineEntity extends AbstractMachineEntity {
    public AbstractFacingMachineEntity(long capacity, long io, int size, int maxGenerationTime, BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(capacity, io, size, maxGenerationTime, type, pos, state);
    }

    @Override
    public ArrayList<Direction> getInputDirs(BlockState state) {
        switch (state.get(Properties.HORIZONTAL_FACING)) {
            case SOUTH -> {
                return exceptSouthList;
            }
            case EAST -> {
                return exceptEastList;
            }
            case WEST -> {
                return exceptWestList;
            }
            default -> {
                return exceptNorthList;
            }
        }
    }
}
