package org.mesdag.scma.block.energy.entity.machine;

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
import static org.mesdag.scma.util.DirLists.downList;

public abstract class AbstractMachineEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplInventory, IMachine {
    protected final long capacity;
    protected final long io;
    protected final SimpleEnergyStorage energyStorage;
    protected final int maxGenerationTime;
    protected final PropertyDelegate propertyDelegate;
    protected DefaultedList<ItemStack> inventory;
    protected int generationTime;
    protected boolean isGenerating;


    public AbstractMachineEntity(long capacity, long io, int size, int maxGenerationTime, BlockEntityType<?> type, BlockPos pos, BlockState state) {
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
        this.maxGenerationTime = maxGenerationTime;
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                if (index == 0) {
                    return AbstractMachineEntity.this.generationTime;
                } else if (index == 1) {
                    return AbstractMachineEntity.this.maxGenerationTime;
                } else if (index == 2) {
                    return (int) AbstractMachineEntity.this.energyStorage.amount;
                } else {
                    return 0;
                }
            }

            @Override
            public void set(int index, int value) {
                if (index == 0) {
                    AbstractMachineEntity.this.generationTime = value;
                } else if (index == 2) {
                    AbstractMachineEntity.this.energyStorage.amount = value;
                }
            }

            @Override
            public int size() {
                return 3;
            }
        };
        this.generationTime = 0;
        this.isGenerating = false;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        energyStorage.amount = nbt.getLong("energy");
        generationTime = nbt.getInt("generationTime");
        Inventories.readNbt(nbt, this.inventory);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putLong("energy", energyStorage.amount);
        nbt.putInt("generationTime", generationTime);
        Inventories.writeNbt(nbt, this.inventory);
        super.writeNbt(nbt);
    }

    public NbtCompound getContextNbt() {
        NbtCompound nbt = new NbtCompound();
        writeNbt(nbt);
        return nbt;
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
    public SimpleEnergyStorage getSelf() {
        return energyStorage;
    }

    public boolean couldContinue() {
        return generationTime < maxGenerationTime;
    }

    public boolean shouldComplete() {
        return generationTime >= maxGenerationTime;
    }

    public void getEnergyFromBattery() {
        if (energyStorage.amount < capacity) {
            ItemStack batteryStack = inventory.get(0);
            if (!batteryStack.isEmpty() && batteryStack.getItem() instanceof IChargeable battery) {
                NbtCompound nbt = batteryStack.getOrCreateNbt();
                if (!nbt.contains("energy")) {
                    nbt.putLong("energy", 0);
                }
                long batteryEnergy = nbt.getLong("energy");
                if (batteryEnergy > 0) {
                    long l = battery.maxExtract(maxInsert(), batteryEnergy);
                    nbt.putLong("energy", batteryEnergy - l);
                    tryInsert(l);
                }
            }
        }
    }

    public void updateState(World world, BlockPos pos, BlockState state) {
        if (isGenerating) {
            if (!state.get(LIT)) world.setBlockState(pos, state.with(LIT, true));
        } else {
            if (state.get(LIT)) world.setBlockState(pos, state.with(LIT, false));
        }
    }

    public int getGenerationTime() {
        return generationTime;
    }

    public void startGenerating() {
        ++generationTime;
        if(!isGenerating) isGenerating = true;
    }

    public void stopGenerating() {
        if (generationTime > 0) --generationTime;
        if (isGenerating) isGenerating = false;
    }

    @Override
    public ArrayList<Direction> getInputDirs(BlockState state) {
        return downList;
    }
}
