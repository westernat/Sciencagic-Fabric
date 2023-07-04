package org.mesdag.scma.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.mesdag.scma.registry.StatusEffectRegistry;

public class MoscoviumOre extends Block {
    public MoscoviumOre() {
        super(FabricBlockSettings.of(Material.STONE).strength(3.5F).requiresTool().sounds(BlockSoundGroup.DEEPSLATE));
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.electric_shock_effect, 50, 1));
        }
    }
}
