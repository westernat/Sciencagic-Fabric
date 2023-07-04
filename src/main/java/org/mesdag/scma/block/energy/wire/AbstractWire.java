package org.mesdag.scma.block.energy.wire;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.mesdag.scma.block.energy.IConnectable;

import java.util.ArrayList;

import static org.mesdag.scma.util.Maps.dirMap;
import static org.mesdag.scma.util.Properties.*;

public abstract class AbstractWire extends Block implements IConnectable, BlockEntityProvider {
    public AbstractWire() {
        super(FabricBlockSettings.of(Material.METAL).strength(0.2f).sounds(BlockSoundGroup.METAL));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(DOWN, UP, NORTH, SOUTH, WEST, EAST);
    }

    @Override
    public ArrayList<Direction> getConnectableDirs(BlockState state) {
        ArrayList<Direction> arrayList = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            if (state.get(dirMap.get(dir))) arrayList.add(dir);
        }
        return arrayList;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView blockView, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.3125, 0.3125, 0.3125, 0.6875, 0.6875, 0.6875);
    }
}