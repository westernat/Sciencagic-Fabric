package org.mesdag.scma.block.energy.entity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.mesdag.scma.block.energy.IConnectable;
import team.reborn.energy.api.base.SimpleEnergyStorage;

public interface IEnergy extends IConnectable {
    SimpleEnergyStorage getSelf();

    World getWorld();

    BlockPos getPos();

    BlockState getCachedState();

    void writeNbt(NbtCompound nbt);

    void readNbt(NbtCompound nbt);
}
