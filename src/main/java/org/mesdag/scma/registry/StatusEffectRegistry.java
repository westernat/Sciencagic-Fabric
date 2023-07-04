package org.mesdag.scma.registry;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.util.registry.Registry;
import org.mesdag.scma.statusEffect.ElectricShock;
import org.mesdag.scma.util.SCMAIdentifier;

import static org.mesdag.scma.SCMA.LOGGER;

public class StatusEffectRegistry {
    // StatusEffect
    public static final ElectricShock electric_shock_effect = new ElectricShock();

    // Potions
    public static final Potion electric_shock_potion = new Potion(new StatusEffectInstance(electric_shock_effect, 1800));

    public static void initialize() {
        Registry.register(Registry.STATUS_EFFECT, new SCMAIdentifier("electric_shock"), electric_shock_effect);
        Registry.register(Registry.POTION, new SCMAIdentifier("electric_shock"), electric_shock_potion);

        LOGGER.info("Status effects loaded");
    }
}
