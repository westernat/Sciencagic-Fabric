package org.mesdag.scma.block.energy.logic;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.mesdag.scma.block.energy.IConnectable;
import org.mesdag.scma.item.Wrench;
import org.mesdag.scma.util.WIP;

import java.util.ArrayList;

import static net.minecraft.state.property.Properties.HORIZONTAL_FACING;
import static net.minecraft.state.property.Properties.POWERED;
import static org.mesdag.scma.util.DirLists.*;

public class LogicLever extends HorizontalFacingBlock implements IConnectable, WIP {
    public LogicLever() {
        super(FabricBlockSettings.of(Material.STONE).strength(1.0f).sounds(BlockSoundGroup.STONE));
        setDefaultState(getStateManager().getDefaultState().with(HORIZONTAL_FACING, Direction.NORTH).with(POWERED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING, POWERED);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player.getStackInHand(hand).getItem() instanceof Wrench) {
            world.setBlockState(pos, state.cycle(POWERED));
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.0625, 0.0625, 0.0625, 0.9375, 0.9375, 0.9375);
    }

    @Override
    public ArrayList<Direction> getConnectableDirs(BlockState state) {
        switch (state.get(FACING)) {
            case SOUTH -> {
                return northList;
            }
            case WEST -> {
                return eastList;
            }
            case EAST -> {
                return westList;
            }
            default -> {
                return southList;
            }
        }
    }

    @Override
    public ArrayList<Direction> getOutputDirs(BlockState state) {
        ArrayList<Direction> dirs;
        switch (state.get(FACING)) {
            case SOUTH -> dirs = northList;
            case WEST -> dirs = eastList;
            case EAST -> dirs = westList;
            default -> dirs = southList;
        }
        if (state.get(POWERED)) return dirs;
        else return emptyList;
    }
}
