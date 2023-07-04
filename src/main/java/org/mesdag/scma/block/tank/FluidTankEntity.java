package org.mesdag.scma.block.tank;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.mesdag.scma.registry.BlockRegistry;


public class FluidTankEntity extends BlockEntity {
    private String fluidType = "";


    public FluidTankEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.fluid_tank_entity, pos, state);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putString("fluidType", fluidType);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        fluidType = nbt.getString("fluidType");
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

    public String getFluidType() {
        return fluidType;
    }
}
