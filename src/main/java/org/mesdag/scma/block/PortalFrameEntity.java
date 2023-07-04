package org.mesdag.scma.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.mesdag.scma.registry.BlockRegistry;

public class PortalFrameEntity extends BlockEntity {
    public PortalFrameEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.portal_frame_entity, pos, state);
    }

    public static <T extends BlockEntity> void clientTick(World world, BlockPos blockPos, BlockState state, T t) {
        if (t instanceof PortalFrameEntity entity) {

        }
    }

    public static <T extends BlockEntity> void serverTick(World world, BlockPos blockPos, BlockState state, T t) {
        if (t instanceof PortalFrameEntity entity) {

        }
    }
}
