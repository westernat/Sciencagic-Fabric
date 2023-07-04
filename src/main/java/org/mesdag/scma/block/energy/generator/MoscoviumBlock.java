package org.mesdag.scma.block.energy.generator;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.mesdag.scma.block.energy.IConnectable;
import org.mesdag.scma.block.energy.entity.generator.MoscoviumBlockEntity;
import org.mesdag.scma.block.energy.entity.machine.AbstractMachineEntity;
import org.mesdag.scma.registry.BlockRegistry;
import org.mesdag.scma.registry.StatusEffectRegistry;

import java.util.List;

import static org.mesdag.scma.util.Properties.ON_USE;

public class MoscoviumBlock extends BlockWithEntity implements IConnectable {
    public static final IntProperty STATUS = IntProperty.of("status", 0, 4);

    public MoscoviumBlock() {
        super(FabricBlockSettings.of(Material.METAL).luminance(state -> state.get(MoscoviumBlock.STATUS)).strength(4.0F).requiresTool().sounds(BlockSoundGroup.METAL));
        setDefaultState(getStateManager().getDefaultState()
                .with(ON_USE, false)
                .with(STATUS, 4)
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ON_USE, STATUS);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(new TranslatableText("block.scma.moscovium_block.tip1").formatted(Formatting.GRAY));
        tooltip.add(new TranslatableText("energy.capacity").append("10000"));
        tooltip.add(new TranslatableText("energy.io").append("10"));
        tooltip.add(new TranslatableText("energy.energy").append(Long.toString(stack.getOrCreateNbt().getCompound("BlockEntityTag").getLong("energy"))));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MoscoviumBlockEntity(pos, state);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (entity instanceof LivingEntity livingEntity && world.getBlockEntity(pos) instanceof MoscoviumBlockEntity blockEntity) {
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.electric_shock_effect, 200, 2));
            blockEntity.tryExtract(5);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player.isSneaky()) {
            if (state.get(ON_USE) && player.getStackInHand(hand).isEmpty()) {
                world.setBlockState(pos, state.with(ON_USE, false));
                return ActionResult.SUCCESS;
            }
        } else {
            if (!state.get(ON_USE) && player.getStackInHand(hand).isOf(BlockRegistry.copper_wire.asItem())) {
                world.setBlockState(pos, state.with(ON_USE, true));
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        if (blockEntity instanceof AbstractMachineEntity base) {
            NbtCompound nbt = new NbtCompound();
            base.writeNbt(nbt);
            NbtCompound tag2 = new NbtCompound();
            tag2.putLong("energy", nbt.getLong("energy"));
            NbtCompound tag = new NbtCompound();
            tag.put("BlockEntityTag", tag2);
            stack.setNbt(tag);
        }
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, BlockRegistry.moscovium_block_entity, MoscoviumBlockEntity::serverTick);
    }
}
