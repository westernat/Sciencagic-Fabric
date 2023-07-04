package org.mesdag.scma.block.energy.entity.logic;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.mesdag.scma.block.energy.entity.generator.IGenerator;
import org.mesdag.scma.block.energy.logic.ESAdapter;
import org.mesdag.scma.block.energy.wire.AbstractWireEntity;
import org.mesdag.scma.registry.BlockRegistry;

import static org.mesdag.scma.block.energy.logic.ESAdapter.INPUT;

public class ESAdapterEntity extends BlockEntity {
    public ESAdapterEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.es_adapter_entity, pos, state);
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, ESAdapterEntity entity) {
        Direction inputDir = ((ESAdapter) state.getBlock()).getInputDirs(state).get(0);
        BlockPos offsetPos = pos.offset(inputDir);
        if (world.isPosLoaded(offsetPos.getX(), offsetPos.getZ())) {
            BlockEntity neighborEntity = world.getBlockEntity(offsetPos);
            boolean wireSignal = neighborEntity instanceof AbstractWireEntity WE && WE.hasRemainder();
            boolean generatorSignal = neighborEntity instanceof IGenerator G && G.getOutputDirs(world.getBlockState(offsetPos)).contains(inputDir.getOpposite());
            if (wireSignal || generatorSignal) {
                if (!state.get(INPUT)) world.setBlockState(pos, state.with(INPUT, true));
            } else {
                if (state.get(INPUT)) world.setBlockState(pos, state.with(INPUT, false));
            }
        }
    }
}
