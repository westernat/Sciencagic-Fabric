package org.mesdag.scma.block.energy.network;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.mesdag.scma.SCMA;
import org.mesdag.scma.block.energy.wire.AbstractWireEntity;

import java.util.HashSet;
import java.util.Set;

import static org.mesdag.scma.SCMA.LOGGER;

public class NetworkService {
    //记录已经用过的网络ID和结点ID
    private Trie01 networkID, nodeID;
    //管理网络
    private Set<Network> networks;
    //创建一个静态实例
    public static final NetworkService INSTANCE = new NetworkService();

    //注册事件
    public void initialize() {
        ServerLifecycleEvents.SERVER_STARTING.register(this::onServerStarting);
        ServerLifecycleEvents.SERVER_STOPPED.register(this::onServerStopped);

        LOGGER.info("Network service loaded");
    }

    //服务端启动时，初始化
    private void onServerStarting(MinecraftServer server) {
        networkID = new Trie01();
        nodeID = new Trie01();
        networks = new HashSet<>();
    }

    //服务端关闭时，清空
    private void onServerStopped(MinecraftServer server) {
        networkID.clear();
        nodeID.clear();
        networks.clear();
    }

    //创建网络
    public Network createNetwork() {
        Network network = new Network(networkID.insert());
        networks.add(network);
        SCMA.LOGGER.info("Create network#{}", network.getId());
        return network;
    }

    //创建网络节点
    public NetworkNode createNetworkNode(AbstractWireEntity blockEntity) {
        NetworkNode node = new NetworkNode(nodeID.insert(), blockEntity);
        blockEntity.setNetworkNode(node);
        SCMA.LOGGER.info("Create network node#{}", node.getId());
        return node;
    }

    //删除网络
    public void removeNetwork(Network network) {
        SCMA.LOGGER.info("Remove network#{}", network.getId());
        networkID.remove(network.getId());
        networks.remove(network);
        network.destroy();
    }

    //删除网络节点
    public void removeNetworkNode(NetworkNode node) {
        SCMA.LOGGER.info("Remove network node#{}", node.getId());
        if (node.getNetwork() != null) removeNodeInNetwork(node, node.getNetwork());
        nodeID.remove(node.getId());
    }

    //将结点node添加到网络network中
    public void addNodeToNetwork(NetworkNode node, Network network) {
        network.addNode(node);
        node.setNetwork(network);
    }

    //从网络network中移除结点node
    public void removeNodeInNetwork(NetworkNode node, Network network) {
        network.removeNode(node);
        node.setNetwork(null);
    }

    //获取所有网络
    public Set<Network> getNetworks() {
        return networks;
    }

    public void mergeNetwork(Network n1, Network n2) {
        //n3为合并后的网络
        Network n3 = Network.merge(n1, n2);
        //将被合并的网络删除
        if (n3 == n1) removeNetwork(n2);
        else removeNetwork(n1);
    }
}
