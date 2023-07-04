package org.mesdag.scma.block.magical;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.mesdag.scma.registry.BlockRegistry;

public class AlchemyTableEntity extends BlockEntity {
    public AlchemyTableEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.alchemy_table_entity, pos, state);
    }


}
