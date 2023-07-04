package org.mesdag.scma.block.energy.wire;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import org.mesdag.scma.registry.BlockRegistry;

public class CopperWireEntity extends AbstractWireEntity {
    public CopperWireEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.copper_wire_entity, pos, state);
    }
}
