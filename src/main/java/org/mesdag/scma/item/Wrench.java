package org.mesdag.scma.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.mesdag.scma.block.energy.entity.machine.AbstractMachineEntity;
import org.mesdag.scma.block.energy.machine.AbstractMachineBlock;

import static org.mesdag.scma.registry.Groups.ITEMS;

public class Wrench extends Item {
    public Wrench() {
        super(new Item.Settings().group(ITEMS).maxCount(1));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext ctx) {
        PlayerEntity player = ctx.getPlayer();
        if (player != null && player.isSneaky()) {
            World world = ctx.getWorld();
            BlockPos pos = ctx.getBlockPos();
            BlockState state = world.getBlockState(pos);
            if (state.contains(Properties.HORIZONTAL_FACING)) {
                world.setBlockState(pos, state.with(Properties.HORIZONTAL_FACING, player.getHorizontalFacing().getOpposite()));
            } else if (state.contains(Properties.FACING)) {
                world.setBlockState(pos, state.cycle(Properties.FACING));
            } else if (state.getBlock() instanceof AbstractMachineBlock) {
                AbstractMachineEntity entity = (AbstractMachineEntity) world.getBlockEntity(pos);
                NbtCompound nbt = new NbtCompound();
                nbt.putInt("generationTime", 0);
                nbt.putLong("energy", 50000);
                entity.readNbt(nbt);
            } else {
                return ActionResult.PASS;
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
}
