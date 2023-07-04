package org.mesdag.scma.block.energy.entity.generator;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.mesdag.scma.block.energy.generator.MoscoviumBlock;
import org.mesdag.scma.registry.BlockRegistry;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.ArrayList;

import static org.mesdag.scma.util.DirLists.allList;
import static org.mesdag.scma.util.DirLists.emptyList;
import static org.mesdag.scma.util.Properties.ON_USE;

public class MoscoviumBlockEntity extends BlockEntity implements IGenerator {
    private static final long capacity = 10000;
    private static final long io = 10;
    public final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(capacity, io, io) {
        @Override
        protected void onFinalCommit() {
            markDirty();
        }
    };


    public MoscoviumBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.moscovium_block_entity, pos, state);
        energyStorage.amount = 0;
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, MoscoviumBlockEntity entity) {
        SimpleEnergyStorage self = entity.energyStorage;
        if (self.getAmount() != capacity) {
            if (world.isDay()) entity.tryInsert(1);
            int status = (int) Math.floorDiv(self.getAmount(), 2500);
            if (status != state.get(MoscoviumBlock.STATUS)) {
                world.setBlockState(pos, state.with(MoscoviumBlock.STATUS, status));
            }
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putLong("energy", energyStorage.amount);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        energyStorage.amount = nbt.getLong("energy");
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbt();
    }

    @Override
    public ArrayList<Direction> getOutputDirs(BlockState state) {
        return state.get(ON_USE) ? allList : emptyList;
    }

    @Override
    public SimpleEnergyStorage getSelf() {
        return energyStorage;
    }

    @Override
    public long maxExtract() {
        return getCachedState().get(ON_USE) ? Math.min(io, energyStorage.amount) : 0;
    }
}
