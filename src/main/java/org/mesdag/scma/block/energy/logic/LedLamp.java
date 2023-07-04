package org.mesdag.scma.block.energy.logic;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import org.mesdag.scma.registry.BlockRegistry;
import org.mesdag.scma.util.WIP;

import java.util.ArrayList;
import java.util.Map;

import static org.mesdag.scma.util.Properties.COLOR;

public class LedLamp extends Block implements ILogic, WIP {
    private static final BooleanProperty LIT = Properties.LIT;

    public LedLamp() {
        super(FabricBlockSettings.of(Material.GLASS).luminance(state -> state.get(Properties.LIT) ? 15 : 0).strength(0.3f).sounds(BlockSoundGroup.GLASS));
        setDefaultState(getStateManager().getDefaultState().with(COLOR, 0).with(LIT, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(COLOR, LIT);
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(LIT, isReceivingSignal(ctx.getWorld(), ctx.getBlockPos()));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction dir, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!world.isClient()) {
            return state.with(LIT, isReceivingSignal((World) world, pos));
        }
        return state;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        Item item = itemStack.getItem();
        if (item == BlockRegistry.led_lamp.asItem()) {
            NbtCompound nbt1 = itemStack.getNbt();
            if (nbt1 != null && nbt1.getCompound("BlockStateTag").getInt("color") == state.get(COLOR)) {
                return ActionResult.PASS;
            } else {
                Map<Integer, String> colorMap = Map.of(0, "WHITE", 1, "RED", 2, "GREEN", 3, "BLUE", 4, "YELLOW");
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
        return ActionResult.PASS;
    }

    @Override
    public ArrayList<Direction> getOutputDirs(BlockState state) {
        return new ArrayList<>();
    }
}
