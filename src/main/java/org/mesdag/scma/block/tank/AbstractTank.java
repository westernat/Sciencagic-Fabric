package org.mesdag.scma.block.tank;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static org.mesdag.scma.util.Maps.dirMap;
import static org.mesdag.scma.util.Properties.*;

public abstract class AbstractTank extends Block implements BlockEntityProvider {
    public AbstractTank() {
        super(FabricBlockSettings.of(Material.METAL).strength(1.5F).nonOpaque());
        setDefaultState(getStateManager().getDefaultState()
                .with(DOWN, false)
                .with(UP, false)
                .with(NORTH, false)
                .with(SOUTH, false)
                .with(WEST, false)
                .with(EAST, false)
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(DOWN, UP, NORTH, SOUTH, WEST, EAST);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        World world = ctx.getWorld();
        BlockState state = getDefaultState();
        if (!world.isClient) {
            BlockPos pos = ctx.getBlockPos();
            for (Direction dir : Direction.values()) {
                state = state.with(dirMap.get(dir), world.getBlockEntity(pos.offset(dir)) instanceof FluidTankEntity neighborEntity && world.getBlockEntity(pos) instanceof FluidTankEntity selfEntity && Objects.equals(selfEntity.getFluidType(), neighborEntity.getFluidType()));
            }
        }
        return state;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!world.isClient()) {
            state = state.with(dirMap.get(direction), world.getBlockEntity(neighborPos) instanceof FluidTankEntity neighborEntity && world.getBlockEntity(pos) instanceof FluidTankEntity selfEntity && Objects.equals(selfEntity.getFluidType(), neighborEntity.getFluidType()));
        }
        return state;
    }
}
