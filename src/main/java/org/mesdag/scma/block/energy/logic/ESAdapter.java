package org.mesdag.scma.block.energy.logic;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.mesdag.scma.block.energy.IConnectable;
import org.mesdag.scma.block.energy.entity.logic.ESAdapterEntity;
import org.mesdag.scma.registry.BlockRegistry;

import java.util.ArrayList;

import static org.mesdag.scma.util.DirLists.*;

public class ESAdapter extends BlockWithEntity implements IConnectable {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty INPUT = BooleanProperty.of("input");


    public ESAdapter() {
        super(FabricBlockSettings.of(Material.GLASS).strength(0.2f).sounds(BlockSoundGroup.GLASS));
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(INPUT, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, INPUT);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ESAdapterEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
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

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.125, 0.125, 0.125, 0.875, 0.875, 0.875);
    }

    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, BlockRegistry.es_adapter_entity, ESAdapterEntity::serverTick);
    }
}
