package org.mesdag.scma.block.energy.network;

import org.mesdag.scma.block.energy.entity.generator.IGenerator;
import org.mesdag.scma.block.energy.entity.machine.IMachine;

import java.util.HashSet;
import java.util.Set;

public class Network {
    //为每个网络分配一个id
    private final int id;
    //存储网络的各个结点
    private final Set<NetworkNode> nodes;
    private final Set<IGenerator> generators;
    private final Set<IMachine> machines;
    private long remainder;


    public Network(int id) {
        this.id = id;
        this.nodes = new HashSet<>();
        this.generators = new HashSet<>();
        this.machines = new HashSet<>();
        this.remainder = 0;
    }

    //获取网络id
    public int getId() {
        return id;
    }

    //获取该网络的所有结点
    public Set<NetworkNode> getNodes() {
        return nodes;
    }

    //增加结点
    public void addNode(NetworkNode node) {
        nodes.add(node);
    }

    //移除结点
    public void removeNode(NetworkNode node) {
        nodes.remove(node);
    }

    public Set<IGenerator> getGenerators() {
        return generators;
    }

    public void updateGenerator(IGenerator generator, boolean remove) {
        if (remove) {
            generators.remove(generator);
        } else {
            generators.add(generator);
        }
    }

    public void addAllGenerators(Set<IGenerator> generatorSet) {
        generators.addAll(generatorSet);
    }

    public Set<IMachine> getMachines() {
        return machines;
    }

    public void updateMachine(IMachine machine, boolean remove) {
        if (remove) {
            machines.remove(machine);
        } else {
            machines.add(machine);
        }
    }

    public void addAllMachines(Set<IMachine> machineSet) {
        machines.addAll(machineSet);
    }


    public long getRemainder() {
        return remainder;
    }

    public boolean hasRemainder() {
        return remainder > 0;
    }

    public void setRemainder(long value) {
        remainder = value;
    }

    //摧毁网络，将网络各结点“放生”
    public void destroy() {
        for (NetworkNode node : nodes) {
            node.setNetwork(null);
        }
        nodes.clear();
        generators.clear();
        machines.clear();
    }

    //调用时保证n1大小大于n2
    private static Network mergeInPrior(Network n1, Network n2) {
        //将n2的所有结点的所属网络设置为n1
        for (NetworkNode node : n2.nodes) {
            node.setNetwork(n1);
        }
        //将n2的所有节点加入到n1中
        n1.nodes.addAll(n2.nodes);
        n1.addAllGenerators(n2.generators);
        n1.addAllMachines(n2.machines);
        //清空n2
        n2.nodes.clear();
        n2.generators.clear();
        n2.machines.clear();
        return n1;
    }

    //按照大小合并网络
    public static Network merge(Network n1, Network n2) {
        if (n1.nodes.size() >= n2.nodes.size()) return mergeInPrior(n1, n2);
        else return mergeInPrior(n2, n1);
    }
}