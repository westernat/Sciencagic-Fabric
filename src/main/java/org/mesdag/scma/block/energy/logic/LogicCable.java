package org.mesdag.scma.block.energy.logic;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.mesdag.scma.block.energy.IConnectable;
import org.mesdag.scma.util.WIP;

import java.util.ArrayList;

import static org.mesdag.scma.util.DirLists.allList;
import static org.mesdag.scma.util.DirLists.emptyList;
import static org.mesdag.scma.util.Maps.dirMap;
import static org.mesdag.scma.util.Properties.*;

public class LogicCable extends Block implements IConnectable, WIP {
    private static final IntProperty SOURCE = IntProperty.of("source", 0, 6);
    public static final IntProperty CONNECTED = IntProperty.of("connected", 0, 2);


    public LogicCable() {
        super(FabricBlockSettings.of(Material.METAL).strength(0.2f).sounds(BlockSoundGroup.METAL));
        setDefaultState(getDefaultState()
            .with(DOWN, false)
            .with(UP, false)
            .with(NORTH, false)
            .with(SOUTH, false)
            .with(WEST, false)
            .with(EAST, false)
            .with(SOURCE, 0)
            .with(CONNECTED, 0)
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(DOWN, UP, NORTH, SOUTH, WEST, EAST, SOURCE, CONNECTED);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView blockView, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.375, 0.375, 0.375, 0.625, 0.625, 0.625);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction dir, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!world.isClient()) {
            int amount = state.get(CONNECTED);
            if (neighborCanConnect(state, neighborState, dir)) {
                if (neighborIsSource(state, neighborState, dir)) state = state.with(SOURCE, dir.getId() + 1);
                return state.with(dirMap.get(dir), true).with(CONNECTED, amount + 1);
            } else {
                if (amount > 0) state = state.with(CONNECTED, amount - 1);
                return state.with(SOURCE, 0).with(dirMap.get(dir), false);
            }
        }
        return state;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = getDefaultState();
        World world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();
        for (Direction dir : Direction.values()) {
            BlockState neighborState = world.getBlockState(pos.offset(dir));
            if (neighborCanConnect(state, neighborState, dir)) {
                int amount = state.get(CONNECTED);
                if (neighborIsSource(state, neighborState, dir)) state = state.with(SOURCE, dir.getId() + 1);
                state = state.with(dirMap.get(dir), true).with(CONNECTED, amount + 1);
                if (amount == 1) return state;
            }
        }
        return state;
    }

    public static boolean neighborIsSource(BlockState state, BlockState neighborState, Direction dir) {
        Block block = neighborState.getBlock();
        if (block instanceof LogicCable) {
            // 如果隔壁的源不是自己,并且隔壁的还有源,则返回true
            return neighborCanConnect(state, neighborState, dir) && neighborState.get(SOURCE) != 0 && !neighborState.get(SOURCE).equals(dir.getOpposite().getId() + 1);
        }
        // 如果隔壁所有发信端中含有可用发信端,则返回true
        return block instanceof IConnectable C && C.getOutputDirs(neighborState).contains(dir.getOpposite());
    }

    public static boolean neighborCanConnect(BlockState state, BlockState neighborState, Direction dir) {
        Block neighborBlock = neighborState.getBlock();
        if (neighborBlock instanceof LogicCable) {
            return cableCanConnect(state, dir) && cableCanConnect(neighborState, dir.getOpposite());
        }
        return neighborBlock instanceof ILogic L && L.getConnectableDirs(neighborState).contains(dir.getOpposite());
    }

    public static boolean cableCanConnect(BlockState state, Direction dir) {
        return state.get(CONNECTED) < 2 && !state.get(dirMap.get(dir));
    }

    @Override
    public ArrayList<Direction> getOutputDirs(BlockState state) {
        return state.get(SOURCE) == 0 ? emptyList : allList;
    }
}
