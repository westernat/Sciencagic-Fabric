package org.mesdag.scma.block.energy.machine;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;

import static org.mesdag.scma.util.DirLists.*;

public abstract class AbstractFacingMachineBlock extends AbstractMachineBlock {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;


    public AbstractFacingMachineBlock() {
        super();
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(LIT, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public ArrayList<Direction> getConnectableDirs(BlockState state) {
        switch (state.get(FACING)) {
            case SOUTH -> {
                return exceptSouthList;
            }
            case WEST -> {
                return exceptWestList;
            }
            case EAST -> {
                return exceptEastList;
            }
            default -> {
                return exceptNorthList;
            }
        }
    }
}
