package org.mesdag.scma.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.mesdag.scma.block.energy.IConnectable;
import org.mesdag.scma.block.energy.entity.IEnergy;
import org.mesdag.scma.block.energy.entity.generator.AbstractGeneratorEntity;
import org.mesdag.scma.block.energy.entity.generator.IGenerator;
import org.mesdag.scma.block.energy.entity.machine.AbstractMachineEntity;
import org.mesdag.scma.block.energy.entity.machine.IMachine;
import org.mesdag.scma.block.energy.network.Network;
import org.mesdag.scma.block.energy.wire.AbstractWireEntity;

import java.util.ArrayList;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;
import static org.mesdag.scma.block.energy.network.PathService.isPosHasBlockEntity;

public class Debugger {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("scma").requires(source -> source.hasPermissionLevel(4))
            .then(literal("get")
                .then(argument("pos", BlockPosArgumentType.blockPos())
                    .then(literal("energy").executes(ctx -> getBlockInfo(ctx.getSource(), BlockPosArgumentType.getLoadedBlockPos(ctx, "pos"), Mode.ENERGY)))
                    .then(literal("network").executes(ctx -> getBlockInfo(ctx.getSource(), BlockPosArgumentType.getLoadedBlockPos(ctx, "pos"), Mode.NETWORK)))
                )
            )
            .then(literal("set")
                .then(argument("pos", BlockPosArgumentType.blockPos())
                    .then(argument("amount", IntegerArgumentType.integer(0, Integer.MAX_VALUE))
                        .then(literal("energy").executes(ctx -> setBlockValue(ctx.getSource(), BlockPosArgumentType.getLoadedBlockPos(ctx, "pos"), Mode.ENERGY, IntegerArgumentType.getInteger(ctx, "amount"))))
                        .then(literal("energyBuffer").executes(ctx -> setBlockValue(ctx.getSource(), BlockPosArgumentType.getLoadedBlockPos(ctx, "pos"), Mode.ENERGY_BUFFER, IntegerArgumentType.getInteger(ctx, "amount"))))
                        .then(literal("generationTime").executes(ctx -> setBlockValue(ctx.getSource(), BlockPosArgumentType.getLoadedBlockPos(ctx, "pos"), Mode.GENERATION_TIME, IntegerArgumentType.getInteger(ctx, "amount"))))
                    )
                )
            )
        );
    }

    private static int getBlockInfo(ServerCommandSource source, BlockPos pos, Mode mode) throws CommandSyntaxException {
        ServerWorld world = source.getWorld();
        BlockState state = world.getBlockState(pos);
        MutableText name = state.getBlock().getName().append(" at " + pos.toShortString());
        if (state.hasBlockEntity()) {
            BlockEntity entity = world.getBlockEntity(pos);
            if (mode == Mode.ENERGY && entity instanceof IEnergy E) {
                String text = name.getString() + ':';
                NbtCompound nbt = new NbtCompound();
                E.writeNbt(nbt);
                text += "\n  energy: " + nbt.getLong("energy");
                if (entity instanceof AbstractGeneratorEntity) {
                    text += "\n  energyBuffer: " + nbt.getLong("energyBuffer");
                } else if (entity instanceof AbstractMachineEntity) {
                    text += "\n  generationTime: " + nbt.getLong("generationTime");
                }
                source.sendFeedback(new LiteralText(text), false);
            } else if (mode == Mode.NETWORK && state.getBlock() instanceof IConnectable) {
                if (entity instanceof AbstractWireEntity WE) {
                    StringBuilder text = new StringBuilder(name.getString() + ':');
                    Network network = WE.getNetwork();
                    text.append("\n  Network#").append(network.getId());
                    text.append("\n  Generators:");
                    boolean isEmpty = true;
                    for (IGenerator G : network.getGenerators()) {
                        isEmpty = false;
                        text.append("\n    ").append(G.getCachedState().getBlock().getName().getString()).append(" at ").append(G.getPos().toShortString());
                    }
                    if (isEmpty) text.append("\n    None");
                    text.append("\n  Machines:");
                    isEmpty= true;
                    for (IMachine M : network.getMachines()) {
                        isEmpty = false;
                        text.append("\n    ").append(M.getCachedState().getBlock().getName().getString()).append(" at ").append(M.getPos().toShortString());
                    }
                    if (isEmpty) text.append("\n    None");
                    text.append("\n  Has Remainder: ").append(WE.hasRemainder());
                    source.sendFeedback(new LiteralText(text.toString()), false);
                } else if (entity instanceof IEnergy) {
                    ArrayList<Direction> dirs = new ArrayList<>();
                    if (entity instanceof IGenerator G) {
                        dirs = G.getOutputDirs(state);
                    } else if (entity instanceof IMachine M) {
                        dirs = M.getInputDirs(state);
                    }
                    StringBuilder text = new StringBuilder(name.getString() + ':');
                    boolean flag = false;
                    for (Direction dir : dirs) {
                        BlockPos offsetPos = pos.offset(dir);
                        if (isPosHasBlockEntity(world, offsetPos) && world.getBlockEntity(offsetPos) instanceof AbstractWireEntity W) {
                            flag = true;
                            text.append("\n  Network#").append(W.getNetwork().getId()).append(" of ").append(dir.getName());
                        }
                    }
                    if (flag) {
                        source.sendFeedback(new LiteralText(text.toString()), false);
                    } else {
                        source.sendFeedback(name.append(new TranslatableText("command.debugger.nonNetwork")), false);
                    }
                }
            } else {
                throw new SimpleCommandExceptionType(name.append(new TranslatableText("command.debugger.nonEnergy"))).create();
            }
            source.sendFeedback(new TranslatableText("command.debugger.succeed").formatted(Formatting.GREEN), false);
            return 1;
        }
        throw new SimpleCommandExceptionType(name.append(new TranslatableText("command.debugger.nonBlockEntity"))).create();
    }

    private static int setBlockValue(ServerCommandSource source, BlockPos pos, Mode mode, int amount) throws CommandSyntaxException {
        ServerWorld world = source.getWorld();
        BlockState state = world.getBlockState(pos);
        MutableText name = state.getBlock().getName().append(" at " + pos.toShortString());
        if (state.hasBlockEntity()) {
            BlockEntity entity = world.getBlockEntity(pos);
            String key;
            long capacity;
            NbtCompound nbt = new NbtCompound();
            if (mode == Mode.ENERGY && entity instanceof IEnergy E) {
                key = "energy";
                capacity = E.getSelf().capacity;
                E.writeNbt(nbt);
            } else if (mode == Mode.ENERGY_BUFFER && entity instanceof AbstractGeneratorEntity G) {
                key = "energyBuffer";
                capacity = G.getSelf().capacity;
                G.writeNbt(nbt);
            } else if (mode == Mode.GENERATION_TIME && entity instanceof IMachine M) {
                key = "generationTime";
                capacity = M.getSelf().capacity;
                M.writeNbt(nbt);
            } else {
                throw new SimpleCommandExceptionType(new TranslatableText("command.debugger.failed")).create();
            }
            if (0 <= amount && amount <= capacity) {
                nbt.putLong(key, amount);
                entity.readNbt(nbt);
                source.sendFeedback(new TranslatableText("command.debugger.succeed").formatted(Formatting.GREEN), false);
                return 1;
            } else {
                throw new SimpleCommandExceptionType(new TranslatableText("command.debugger.outOfBounds").append("[0, " + capacity + "] ").append("of " + name.getString())).create();
            }
        }
        throw new SimpleCommandExceptionType(name.append(new TranslatableText("command.debugger.nonBlockEntity"))).create();
    }

    public enum Mode {
        ENERGY,
        ENERGY_BUFFER,
        GENERATION_TIME,
        NETWORK
    }
}
