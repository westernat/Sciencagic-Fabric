package org.mesdag.scma.statusEffect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class ElectricShock extends StatusEffect {

    public ElectricShock() {
        super(StatusEffectCategory.HARMFUL, 0x66ccff);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i = 80 >> amplifier;
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (amplifier > 1) entity.setFireTicks(amplifier * 20);
        entity.damage(DamageSource.MAGIC, (float) (amplifier));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, amplifier * 10, amplifier + 1));
    }
}