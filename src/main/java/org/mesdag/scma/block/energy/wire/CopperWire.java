package org.mesdag.scma.block.energy.wire;


import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.StateManager;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mesdag.scma.block.energy.IConnectable;
import org.mesdag.scma.block.energy.logic.LogicCable;

import java.util.List;

import static org.mesdag.scma.registry.BlockRegistry.copper_wire;
import static org.mesdag.scma.util.Maps.colorMap;
import static org.mesdag.scma.util.Maps.dirMap;
import static org.mesdag.scma.util.Properties.*;

public class CopperWire extends AbstractWire {
    public CopperWire() {
        super();
        setDefaultState(getStateManager().getDefaultState()
            .with(DOWN, false)
            .with(UP, false)
            .with(NORTH, false)
            .with(SOUTH, false)
            .with(WEST, false)
            .with(EAST, false)
            .with(COLOR, 0)
        );
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CopperWireEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(COLOR);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, BlockView world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(new TranslatableText("block.scma.copper_wire.tip").formatted(Formatting.GRAY));
        String name = colorMap.get(itemStack.getOrCreateNbt().getInt("CustomModelData"));
        tooltip.add(new TranslatableText("color." + name).formatted(Formatting.byName(name)));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        World world = ctx.getWorld();
        BlockState state = getDefaultState();
        if (!world.isClient) {
            BlockPos pos = ctx.getBlockPos();
            for (Direction dir : Direction.values()) {
                if (neighborCanConnect(state, world.getBlockState(pos.offset(dir)), dir)) {
                    state = state.with(dirMap.get(dir), true);
                }
            }
        }
        return state;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction dir, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!world.isClient()) {
            return state.with(dirMap.get(dir), neighborCanConnect(state, neighborState, dir));
        }
        return state;
    }

    public static boolean neighborCanConnect(BlockState state, BlockState neighborState, Direction dir) {
        Block neighborBlock = neighborState.getBlock();
        if (neighborBlock instanceof AbstractWire) {
            int color = state.get(COLOR);
            int neighborColor = neighborState.get(COLOR);
            return color == neighborColor || color == 4 || neighborColor == 4;
        } else {
            return neighborBlock instanceof IConnectable C && !(neighborBlock instanceof LogicCable) && C.getConnectableDirs(neighborState).contains(dir.getOpposite());
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, @NotNull PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            ItemStack itemStack = player.getStackInHand(hand);
            Item item = itemStack.getItem();
            if (item == copper_wire.asItem()) {
                NbtCompound nbt1 = itemStack.getNbt();
                if (nbt1 != null && nbt1.getCompound("BlockStateTag").getInt("color") == state.get(COLOR)) {
                    return ActionResult.PASS;
                } else {
                    int color = state.get(COLOR);
                    NbtCompound tag2 = new NbtCompound();
                    tag2.putInt("color", color);
                    NbtCompound tag = new NbtCompound();
                    tag.put("BlockStateTag", tag2);
                    tag.putInt("CustomModelData", color);
                    itemStack.setNbt(tag);
                    String name = colorMap.get(color);
                    player.sendMessage(new TranslatableText("color.set").append(new TranslatableText("color." + name)).formatted(Formatting.byName(name)), true);
                }
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }
}
