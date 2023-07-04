package org.mesdag.scma.block.magical;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class AlchemyTable extends Block implements BlockEntityProvider {
    private static final VoxelShape TOP = VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.625, 0.25, 1.625).offset(-0.3125, 0.875, -0.3125);
    private static final VoxelShape MEDIUM = VoxelShapes.cuboid(0.0, 0.0, 0.0, 0.75, 0.6875, 0.75).offset(0.125, 0.1875, 0.125);
    private static final VoxelShape BOTTOM = VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.125, 0.1875, 1.125).offset(-0.0625, 0.0, -0.0625);
    private static final VoxelShape SHAPE = VoxelShapes.union(TOP, MEDIUM, BOTTOM);


    public AlchemyTable() {
        super(FabricBlockSettings.of(Material.GLASS).strength(1.0F).sounds(BlockSoundGroup.WOOD));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AlchemyTableEntity(pos, state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
}
