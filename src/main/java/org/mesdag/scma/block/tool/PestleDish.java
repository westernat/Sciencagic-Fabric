package org.mesdag.scma.block.tool;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.mesdag.scma.item.Pestle;
import org.mesdag.scma.registry.ItemRegistry;

import java.util.*;

import static org.mesdag.scma.util.Properties.ON_USE;


public class PestleDish extends Block implements BlockEntityProvider {
    public static final List<Item> pestles = List.of(ItemRegistry.wooden_pestle, ItemRegistry.terracotta_pestle, ItemRegistry.copper_pestle);

    public static final Map<Item, List<?>> pestleable = Map.of(
            // raw: [progressFix, result, amount]
            Items.CHARCOAL, List.of(-5, ItemRegistry.toner, 2)
    );
    public static final Map<Set<Integer>, List<?>> pestle_mixture = Map.of(

    );

    public PestleDish() {
        super(FabricBlockSettings.of(Material.STONE).strength(1.0f).sounds(BlockSoundGroup.STONE));
        setDefaultState(getStateManager().getDefaultState().with(ON_USE, false));
    }

    private static ItemStack getResult(int[] materials) {
        boolean isNotMixture = true;
        HashSet<Integer> set = new HashSet<>();
        for (int i : materials) {
            if (i != materials[0]) {
                isNotMixture = false;
                break;
            }
            set.add(i);
        }
        if (isNotMixture) {
            List<?> list = pestleable.get(Item.byRawId(materials[0]));
            return new ItemStack((Item) list.get(1), materials.length * (int) list.get(2));
        } else if (pestle_mixture.containsKey(set)) {
            List<?> list = pestle_mixture.get(set);
            return new ItemStack((Item) list.get(0), (int) list.get(1));
        }
        return new ItemStack(ItemRegistry.mixtures, materials.length);
    }

    public static ItemStack getPestleByLevel(int level) {
        return new ItemStack(pestles.get(level - 1));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ON_USE);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PestleDishEntity(pos, state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.0625, 0, 0.0625, 0.9375, 0.4375, 0.9375);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        Item item = itemStack.getItem();
        PestleDishEntity entity = (PestleDishEntity) world.getBlockEntity(pos);
        NbtCompound nbt = entity.getContextNbt();
        if (item instanceof Pestle) {
            if (nbt.getInt("pestleLevel") == 0) {
                player.setStackInHand(hand, ItemStack.EMPTY);
            } else {
                // 交换手中的捣药杵
                ItemStack pestle = getPestleByLevel(nbt.getInt("pestleLevel"));
                pestle.setDamage(nbt.getInt("pestleDamage"));
                player.setStackInHand(hand, pestle);
            }
            entity.setPestle(((Pestle) item).getLevel());
            entity.setPestleDamage(itemStack.getDamage());
        } else if (nbt.getIntArray("materials").length < 16 && !itemStack.isEmpty()) {
            itemStack.decrement(1);
            entity.addIdToMaterials(Item.getRawId(item));
            if (pestleable.containsKey(item)) {
                entity.updateProgress((int) pestleable.get(item).get(0));
            } else {
                entity.updateProgress(-10);
            }
        } else {
            int level = nbt.getInt("pestleLevel");
            if (nbt.getIntArray("materials").length != 0) {
                if (player.isSneaky()) {
                    Item item1 = Item.byRawId(entity.popIdFromMaterials());
                    player.giveItemStack(new ItemStack(item1));
                    if (nbt.getIntArray("materials").length < 2) {
                        entity.setProgress(0);
                    } else if (pestleable.containsKey(item1)) {
                        entity.updateProgress(-(int) pestleable.get(item1).get(0));
                    } else {
                        entity.updateProgress(10);
                    }
                } else if (level != 0) {
                    if (nbt.getInt("progress") < 100) {
                        entity.updateProgress(level);
                        world.setBlockState(pos, state.with(ON_USE, true));
                        world.createAndScheduleBlockTick(pos, this, 4);
                    } else {
                        int damage = nbt.getInt("pestleDamage") + 1;
                        ItemStack pestle = getPestleByLevel(level);
                        if (damage < pestle.getMaxDamage()) {
                            pestle.setDamage(damage);
                            player.giveItemStack(pestle);
                        } else {
                            world.playSound(player, pos, SoundEvents.ITEM_SHIELD_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
                            // world.addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, pestle), false, pos.getX(), pos.getY() + 0.3, pos.getZ(), 0.0, 0.0, 0.0);
                        }
                        player.giveItemStack(getResult(nbt.getIntArray("materials")));
                        entity.allClear();
                    }
                }
            } else if (level != 0) {
                ItemStack pestle = getPestleByLevel(nbt.getInt("pestleLevel"));
                pestle.setDamage(nbt.getInt("pestleDamage"));
                player.giveItemStack(pestle);
                entity.allClear();
            }
        }
        entity.markDirty();
        return ActionResult.SUCCESS;
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack) {
        super.afterBreak(world, player, pos, state, blockEntity, stack);
        NbtCompound nbt = ((PestleDishEntity) blockEntity).getContextNbt();
        int level = nbt.getInt("pestleLevel");
        if (level != 0) {
            ItemStack pestle = getPestleByLevel(level);
            pestle.setDamage(nbt.getInt("pestleDamage"));
            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY() + 0.5, pos.getZ(), pestle));
        }
        for (int i : nbt.getIntArray("materials")) {
            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY() + 0.5, pos.getZ(), new ItemStack(Item.byRawId(i))));
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(ON_USE)) {
            world.setBlockState(pos, state.with(ON_USE, false));
        }
    }
}
