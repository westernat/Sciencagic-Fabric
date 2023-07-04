package org.mesdag.scma.block.energy.entity.generator;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.state.property.Properties;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.mesdag.scma.block.ImplInventory;
import org.mesdag.scma.item.energy.IChargeable;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.ArrayList;

import static org.mesdag.scma.block.energy.generator.AbstractGeneratorBlock.LIT;
import static org.mesdag.scma.util.DirLists.*;

public abstract class AbstractGeneratorEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplInventory, IGenerator {
    protected final int capacity;
    protected final long io;
    protected final SimpleEnergyStorage energyStorage;
    protected final int maxProgress;
    protected final PropertyDelegate propertyDelegate;
    protected DefaultedList<ItemStack> inventory;
    protected int progress;
    protected long energyBuffer;


    public AbstractGeneratorEntity(int capacity, long io, int size, int maxProgress, BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.inventory = DefaultedList.ofSize(size, ItemStack.EMPTY);
        this.capacity = capacity;
        this.io = io;
        this.energyStorage = new SimpleEnergyStorage(capacity, io, io) {
            @Override
            protected void onFinalCommit() {
                markDirty();
            }
        };
        this.maxProgress = maxProgress;
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                if (index == 0) {
                    return AbstractGeneratorEntity.this.progress;
                } else if (index == 1) {
                    return AbstractGeneratorEntity.this.maxProgress;
                } else if (index == 2) {
                    return (int) AbstractGeneratorEntity.this.energyStorage.amount;
                } else {
                    return 0;
                }
            }

            @Override
            public void set(int index, int value) {
                if (index == 0) {
                    AbstractGeneratorEntity.this.progress = value;
                } else if (index == 2) {
                    AbstractGeneratorEntity.this.energyStorage.amount = value;
                }
            }

            @Override
            public int size() {
                return 3;
            }
        };
        this.progress = 0;
        this.energyBuffer = 0;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        energyStorage.amount = nbt.getLong("energy");
        progress = nbt.getInt("progress");
        energyBuffer = nbt.getLong("energyBuffer");
        Inventories.readNbt(nbt, this.inventory);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putLong("energy", energyStorage.amount);
        nbt.putInt("progress", progress);
        nbt.putLong("energyBuffer", energyBuffer);
        Inventories.writeNbt(nbt, this.inventory);
        super.writeNbt(nbt);
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
        switch (state.get(Properties.HORIZONTAL_FACING)) {
            case SOUTH -> {
                return exceptSouthList;
            }
            case EAST -> {
                return exceptEastList;
            }
            case WEST -> {
                return exceptWestList;
            }
            default -> {
                return exceptNorthList;
            }
        }
    }

    @Override
    public SimpleEnergyStorage getSelf() {
        return energyStorage;
    }

    public void updateState(World world, BlockPos pos, BlockState state) {
        if (energyBuffer > 0) {
            if (!state.get(LIT)) world.setBlockState(pos, state.with(LIT, true));
        } else {
            if (state.get(LIT)) world.setBlockState(pos, state.with(LIT, false));
        }
    }

    public void chargingBattery() {
        ItemStack batteryStack = inventory.get(0);
        if (!batteryStack.isEmpty() && batteryStack.getItem() instanceof IChargeable battery) {
            NbtCompound nbt = batteryStack.getOrCreateNbt();
            if (!nbt.contains("energy")) {
                nbt.putLong("energy", 0);
            }
            long batteryEnergy = nbt.getLong("energy");
            if (energyBuffer > 0) {
                long l = Math.min(energyBuffer, battery.maxInsert(io, batteryEnergy));
                energyBuffer -= l;
                nbt.putLong("energy", batteryEnergy + l);
            } else {
                long l = Math.min(getStoredEnergy(), battery.maxInsert(io, batteryEnergy));
                nbt.putLong("energy", batteryEnergy + l);
                tryExtract(l);
            }
        }
    }

    public void chargingSelf() {
        if (energyBuffer > 0 && getStoredEnergy() < capacity) {
            long l = maxInsert(energyBuffer);
            energyBuffer -= l;
            tryInsert(l);
        }
    }

    public void updateGenerationTime() {
        progress = (int) (energyBuffer * maxProgress / capacity);
    }
}
