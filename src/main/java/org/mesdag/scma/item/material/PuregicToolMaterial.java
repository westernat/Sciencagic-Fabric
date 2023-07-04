package org.mesdag.scma.item.material;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import org.mesdag.scma.registry.ItemRegistry;

public class PuregicToolMaterial implements ToolMaterial {
    public static final PuregicToolMaterial INSTANCE = new PuregicToolMaterial();

    @Override
    public int getDurability() {
        return 65535;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 10.0f;
    }

    @Override
    public float getAttackDamage() {
        return 56.0f;
    }

    @Override
    public int getMiningLevel() {
        return 4;
    }

    @Override
    public int getEnchantability() {
        return 22;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(ItemRegistry.puregic_ingot);
    }
}
