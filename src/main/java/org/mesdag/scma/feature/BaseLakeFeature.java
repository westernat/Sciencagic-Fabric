package org.mesdag.scma.feature;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Random;

public class BaseLakeFeature extends Feature<DefaultFeatureConfig> {
    private static final BlockState air = Blocks.AIR.getDefaultState();
    protected final BlockState fluidBlockState;
    protected final BlockState barrierBlockState;


    public BaseLakeFeature(BlockState fluidBlockState, BlockState barrierBlockState) {
        super(DefaultFeatureConfig.CODEC);
        this.fluidBlockState = fluidBlockState;
        this.barrierBlockState = barrierBlockState;
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        BlockPos blockPos = context.getOrigin();
        StructureWorldAccess structureWorldAccess = context.getWorld();
        Random random = context.getRandom();
        if (blockPos.getY() <= structureWorldAccess.getBottomY() + 4) {
            return false;
        } else {
            blockPos = blockPos.down(4);
            boolean[] bls = new boolean[2048];
            int i = random.nextInt(4) + 4;
            for (int j = 0; j < i; ++j) {
                double d = random.nextDouble() * 6.0 + 3.0;
                double e = random.nextDouble() * 4.0 + 2.0;
                double f = random.nextDouble() * 6.0 + 3.0;
                double g = random.nextDouble() * (16.0 - d - 2.0) + 1.0 + d / 2.0;
                double h = random.nextDouble() * (8.0 - e - 4.0) + 2.0 + e / 2.0;
                double k = random.nextDouble() * (16.0 - f - 2.0) + 1.0 + f / 2.0;
                for (int l = 1; l < 15; ++l) {
                    for (int m = 1; m < 15; ++m) {
                        for (int n = 1; n < 7; ++n) {
                            double o = ((double) l - g) / (d / 2.0);
                            double p = ((double) n - h) / (e / 2.0);
                            double q = ((double) m - k) / (f / 2.0);
                            double r = o * o + p * p + q * q;
                            if (r < 1.0) {
                                bls[(l * 16 + m) * 8 + n] = true;
                            }
                        }
                    }
                }
            }
            int t;
            int s;
            int u;
            boolean v;
            int count = 0;
            for(s = 0; s < 16; ++s) {
                for(t = 0; t < 16; ++t) {
                    for(u = 0; u < 8; ++u) {
                        v = !bls[(s * 16 + t) * 8 + u] && (s < 15 && bls[((s + 1) * 16 + t) * 8 + u] || s > 0 && bls[((s - 1) * 16 + t) * 8 + u] || t < 15 && bls[(s * 16 + t + 1) * 8 + u] || t > 0 && bls[(s * 16 + (t - 1)) * 8 + u] || u < 7 && bls[(s * 16 + t) * 8 + u + 1] || u > 0 && bls[(s * 16 + t) * 8 + (u - 1)]);
                        if (v) {
                            Material material = structureWorldAccess.getBlockState(blockPos.add(s, u, t)).getMaterial();
                            if (u >= 4 && material.isLiquid()) {
                                ++count;
                            }
                            if (u < 4 && !material.isSolid()) {
                                ++count;
                            }
                        }
                    }
                }
            }
            if (count > 48) return false;
            boolean bl2;
            for (s = 0; s < 16; ++s) {
                for (t = 0; t < 16; ++t) {
                    for (u = 0; u < 8; ++u) {
                        if (bls[(s * 16 + t) * 8 + u]) {
                            BlockPos blockPos2 = blockPos.add(s, u, t);
                            if (canReplace(structureWorldAccess.getBlockState(blockPos2))) {
                                bl2 = u >= 4;
                                structureWorldAccess.setBlockState(blockPos2, bl2 ? air : fluidBlockState, 2);
                                if (bl2) {
                                    structureWorldAccess.createAndScheduleBlockTick(blockPos2, air.getBlock(), 0);
                                    markBlocksAboveForPostProcessing(structureWorldAccess, blockPos2);
                                }
                            }
                        }
                        bl2 = !bls[(s * 16 + t) * 8 + u] && (s < 15 && bls[((s + 1) * 16 + t) * 8 + u] || s > 0 && bls[((s - 1) * 16 + t) * 8 + u] || s < 15 && bls[(s * 16 + t + 1) * 8 + u] || t > 0 && bls[(s * 16 + (t - 1)) * 8 + u] || u < 7 && bls[(s * 16 + t) * 8 + u + 1] || u > 0 && bls[(s * 16 + t) * 8 + (u - 1)]);
                        if (bl2) {
                            BlockState blockState3 = structureWorldAccess.getBlockState(blockPos.add(s, u, t));
                            if ((blockState3.getMaterial().isSolid() || u < 4) && !blockState3.isIn(BlockTags.LAVA_POOL_STONE_CANNOT_REPLACE)) {
                                BlockPos blockPos3 = blockPos.add(s, u, t);
                                structureWorldAccess.setBlockState(blockPos3, barrierBlockState, 2);
                                markBlocksAboveForPostProcessing(structureWorldAccess, blockPos3);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean canReplace(BlockState state) {
        return !state.isIn(BlockTags.FEATURES_CANNOT_REPLACE);
    }
}
