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
import org.mesdag.scma.registry.BlockRegistry;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.ArrayList;

import static org.mesdag.scma.util.DirLists.downList;

public class SolarPanelEntity extends BlockEntity implements IGenerator {
    private static final long capacity = 20000;
    private static final long io = 100;
    public final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(capacity, io, io) {
        @Override
        protected void onFinalCommit() {
            markDirty();
        }
    };


    public SolarPanelEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.solar_panel_entity, pos, state);
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, SolarPanelEntity entity) {
        if (world.isDay() && entity.energyStorage.getAmount() != capacity) {
            int energy;
            switch ((int) Math.floorDiv(world.getTimeOfDay(), 1000)) {
                case 0, 12 -> energy = 2;
                case 1, 11 -> energy = 4;
                case 2, 10 -> energy = 6;
                case 3, 9 -> energy = 8;
                case 4, 8 -> energy = 10;
                default -> energy = 12;
            }
            entity.tryInsert(energy);
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
        return downList;
    }

    @Override
    public SimpleEnergyStorage getSelf() {
        return energyStorage;
    }
}
