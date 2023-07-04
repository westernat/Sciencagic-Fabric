package org.mesdag.scma.block.energy.network;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.mesdag.scma.block.energy.entity.IEnergy;
import org.mesdag.scma.block.energy.entity.generator.IGenerator;
import org.mesdag.scma.block.energy.entity.machine.IMachine;
import org.mesdag.scma.block.energy.wire.AbstractWireEntity;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

import static org.mesdag.scma.SCMA.LOGGER;
import static org.mesdag.scma.util.Properties.ON_USE;

public class PathService {
    //创建一个静态实例
    public static final PathService INSTANCE = new PathService();

    //注册时间
    public void initialize() {
        ServerBlockEntityEvents.BLOCK_ENTITY_LOAD.register(this::onBlockEntityLoad);
        ServerBlockEntityEvents.BLOCK_ENTITY_UNLOAD.register(this::onBlockEntityUnload);
        ServerTickEvents.START_SERVER_TICK.register(this::energyBehaviorTick);
        ServerTickEvents.END_SERVER_TICK.register(this::pathFindingTick);
        ServerLifecycleEvents.SERVER_STARTING.register(this::onServerStarting);

        LOGGER.info("Path service loaded");
    }

    private Queue<NetworkNode> queue;
    private Queue<IEnergy> energyQueue;

    private void onServerStarting(MinecraftServer server) {
        this.queue = new ArrayDeque<>();
        this.energyQueue = new ArrayDeque<>();
    }

    //方块加载时，创捷网络结点
    public void onBlockEntityLoad(BlockEntity blockEntity, ServerWorld world) {
        if (blockEntity instanceof AbstractWireEntity WN) {
            NetworkNode node = NetworkService.INSTANCE.createNetworkNode(WN);
            //新建的网络结点放入处理队列中
            queue.add(node);
        } else if (blockEntity instanceof IEnergy E) {
            energyQueue.add(E);
        }
    }

    //方块卸载时，删除网络结点和所属网络
    private void onBlockEntityUnload(BlockEntity blockEntity, ServerWorld world) {
        if (blockEntity instanceof AbstractWireEntity WE) {
            Network network = WE.getNetworkNode().getNetwork();
            NetworkService.INSTANCE.removeNetworkNode(WE.getNetworkNode());
            if (network != null) {
                //网络已损坏，需要将所拥有的全部结点重新处理，放入处理队列中
                queue.addAll(network.getNodes());
                NetworkService.INSTANCE.removeNetwork(network);
            }
        } else if (blockEntity instanceof IEnergy E) {
            energyQueue.add(E);
        }
    }

    private void energyBehaviorTick(MinecraftServer server) {
        for (Network network : NetworkService.INSTANCE.getNetworks()) {
            int generatorSize = network.getGenerators().size();
            long totalGenerated = 0;
            long totalConsumed = 0;
            if (generatorSize > 0) {
                for (IGenerator generator : network.getGenerators()) {
                    totalGenerated += generator.maxExtract();
                }
                if (totalGenerated > 0 && network.getMachines().size() > 0) {
                    boolean shouldExtract = false;
                    for (IMachine machine : network.getMachines()) {
                        long consumed = Math.min(machine.maxInsert(), totalGenerated);
                        machine.tryInsert(consumed);
                        totalGenerated -= consumed;
                        totalConsumed += consumed;
                        shouldExtract = true;
                    }
                    long spread = totalConsumed / generatorSize;
                    if (shouldExtract && spread > 0) {
                        for (IGenerator generator : network.getGenerators()) {
                            generator.tryExtract(Math.min(spread, generator.maxExtract()));
                        }
                    }
                }
            }
            network.setRemainder(totalGenerated);
        }
    }

    private void pathFindingTick(MinecraftServer server) {
        boolean flag = false;
        while (!queue.isEmpty()) {
            flag = true;
            NetworkNode cur = queue.remove();
            World world = cur.getBlockEntity().getWorld();
            //如果未加载则直接跳过
            if (!world.isPosLoaded(cur.getPos().getX(), cur.getPos().getZ())) continue;
            if (cur.getNetwork() == null) {
                //如果没有所属网络则创建一个
                Network network = NetworkService.INSTANCE.createNetwork();
                NetworkService.INSTANCE.addNodeToNetwork(cur, network);
            }
            Network curNetwork = cur.getNetwork();
            for (Direction direction : cur.getPossibleConnection()) {
                BlockPos pos = cur.getPos().offset(direction);
                if (world.isPosLoaded(pos.getX(), pos.getZ())) {
                    BlockEntity blockEntity = world.getBlockEntity(pos);
                    if (blockEntity instanceof AbstractWireEntity wireEntity) {
                        NetworkNode next = wireEntity.getNetworkNode();
                        Network nextNetwork = next.getNetwork();
                        if (nextNetwork == null) {
                            //将结点加入到网络中
                            NetworkService.INSTANCE.addNodeToNetwork(next, curNetwork);
                            queue.add(next);
                        } else if (curNetwork != nextNetwork) {
                            //合并网络
                            NetworkService.INSTANCE.mergeNetwork(curNetwork, nextNetwork);
                            //记得更新正在处理的所属网络！
                            curNetwork = cur.getNetwork();
                        }
                    } else if (blockEntity instanceof IGenerator generator) {
                        curNetwork.updateGenerator(generator, false);
                    } else if (blockEntity instanceof IMachine machine) {
                        curNetwork.updateMachine(machine, false);
                    }
                }
            }
        }
        while (!energyQueue.isEmpty()) {
            flag = true;
            IEnergy energy = energyQueue.remove();
            World world = energy.getWorld();
            BlockPos pos = energy.getPos();
            if (energy instanceof IGenerator generator) {
                boolean remove = !(isPosHasBlockEntity(world, pos) && world.getBlockEntity(pos) instanceof IGenerator);
                BlockState state = energy.getCachedState();
                ArrayList<Direction> outputDirs = state.contains(ON_USE) ? generator.getOutputDirs(state.with(ON_USE, true)) : generator.getOutputDirs(state);
                for (Direction dir : outputDirs) {
                    BlockPos offsetPos = pos.offset(dir);
                    if (isPosHasBlockEntity(world, offsetPos) && world.getBlockEntity(offsetPos) instanceof AbstractWireEntity WE) {
                        WE.getNetwork().updateGenerator(generator, remove);
                    }
                }
            } else if (energy instanceof IMachine machine) {
                boolean remove = !(isPosHasBlockEntity(world, pos) && world.getBlockEntity(pos) instanceof IMachine);
                for (Direction dir : machine.getInputDirs(machine.getCachedState())) {
                    BlockPos offsetPos = pos.offset(dir);
                    if (isPosHasBlockEntity(world, offsetPos) && world.getBlockEntity(offsetPos) instanceof AbstractWireEntity WE) {
                        WE.getNetwork().updateMachine(machine, remove);
                    }
                }
            }
        }
        //刷新节点, 并打印调试信息
        if (flag) {
            LOGGER.info("Path finding finish");
            for (Network network : NetworkService.INSTANCE.getNetworks()) {
                LOGGER.info("Network#{}:", network.getId());
                for (NetworkNode node : network.getNodes()) {
                    LOGGER.info("Node#{}: {}", node.getId(), node.getPos().toShortString());
                }
                for (IGenerator generator : network.getGenerators()) {
                    LOGGER.info("Generator: {}", generator.toString());
                }
                for (IMachine machine : network.getMachines()) {
                    LOGGER.info("Machines: {}", machine.toString());
                }
            }
        }
    }

    public static boolean isPosHasBlockEntity(World world, BlockPos pos) {
        return world.isPosLoaded(pos.getX(), pos.getZ()) && world.getBlockState(pos).hasBlockEntity();
    }
}