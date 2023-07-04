package org.mesdag.scma.block.energy.logic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.mesdag.scma.item.Wrench;

import java.util.ArrayList;
import java.util.List;

public class SwitchGate extends DiodeGate {
    private static final BooleanProperty SWITCH = BooleanProperty.of("switch");

    public SwitchGate() {
        super();
        setDefaultState(getStateManager().getDefaultState().with(SWITCH, false));
    }

    @Override
    protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(SWITCH);
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
        if (state.get(INPUT) && state.get(SWITCH)) return new ArrayList<>(List.of(dir));
        else return new ArrayList<>();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.getItem() instanceof Wrench) {
            world.setBlockState(pos, state.cycle(SWITCH));
            return ActionResult.SUCCESS;
        } else {
            return ActionResult.PASS;
        }
    }
}
