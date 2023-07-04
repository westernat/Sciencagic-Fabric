package org.mesdag.scma.block.energy.network;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.mesdag.scma.block.energy.wire.AbstractWireEntity;

import java.util.ArrayList;

public class NetworkNode {
    //为每个结点分配一个id
    private final int id;
    //结点所在的网络
    private Network network;

    //获取结点id
    public int getId() {
        return id;
    }

    //获取结点所在的网络
    public Network getNetwork() {
        return network;
    }

    //设置结点所在的网络
    public void setNetwork(Network network) {
        this.network = network;
    }

    //存储结点所在的BlockEntity
    private final AbstractWireEntity blockEntity;

    //构造函数
    public NetworkNode(int id, AbstractWireEntity blockEntity) {
        this.id = id;
        this.blockEntity = blockEntity;
        this.network = null;
    }

    //获取结点所在位置
    public BlockPos getPos() {
        return blockEntity.getPos();
    }

    //获取结点所在的BlockEntity
    public AbstractWireEntity getBlockEntity() {
        return blockEntity;
    }

    public ArrayList<Direction> getPossibleConnection() {
        return blockEntity.getPossibleConnection();
    }
}