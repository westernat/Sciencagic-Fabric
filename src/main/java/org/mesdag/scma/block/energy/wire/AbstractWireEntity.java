package org.mesdag.scma.block.energy.wire;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import org.mesdag.scma.block.energy.network.Network;
import org.mesdag.scma.block.energy.network.NetworkNode;

import java.util.ArrayList;

public abstract class AbstractWireEntity extends BlockEntity {
    protected NetworkNode networkNode;

    public AbstractWireEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
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

    public void setNetworkNode(NetworkNode node) {
        networkNode = node;
    }

    public NetworkNode getNetworkNode() {
        return networkNode;
    }

    public Network getNetwork() {
        return networkNode == null ? null : networkNode.getNetwork();
    }

    public ArrayList<Direction> getPossibleConnection() {
        BlockState state = getCachedState();
        return ((AbstractWire) state.getBlock()).getConnectableDirs(state);
    }

    public boolean hasRemainder() {
        return getNetwork() != null && getNetwork().hasRemainder();
    }
}
