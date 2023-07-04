package org.mesdag.scma.block.tool;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.mesdag.scma.registry.BlockRegistry;

import java.util.ArrayList;
import java.util.List;

public class PestleDishEntity extends BlockEntity {
    private int pestleLevel = 0;
    private int pestleDamage = 0;
    private List<Integer> materials = new ArrayList<>();
    private int progress = 0;

    public PestleDishEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.pestle_dish_entity, pos, state);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putInt("pestleLevel", pestleLevel);
        nbt.putInt("pestleDamage", pestleDamage);
        nbt.putIntArray("materials", materials);
        nbt.putInt("progress", progress);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        pestleLevel = nbt.getInt("pestleLevel");
        pestleDamage = nbt.getInt("pestleDamage");
        List<Integer> list = new ArrayList<>();
        for (int i : nbt.getIntArray("materials")) list.add(i);
        materials = list;
        progress = nbt.getInt("progress");
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

    public NbtCompound getContextNbt() {
        NbtCompound nbt = new NbtCompound();
        writeNbt(nbt);
        return nbt;
    }

    public void setPestle(int level) {
        pestleLevel = level;
    }

    public void setPestleDamage(int damage) {
        pestleDamage = damage;
    }

    public void addIdToMaterials(int value) {
        materials.add(value);
    }

    public int popIdFromMaterials() {
        return materials.remove(materials.size() - 1);
    }

    public void setProgress(int value) {
        progress = value;
    }

    public void updateProgress(int value) {
        progress += value;
    }

    public void allClear() {
        pestleLevel = 0;
        pestleDamage = 0;
        materials.clear();
        progress = 0;
    }
}
