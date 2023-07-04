package org.mesdag.scma.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;
import org.mesdag.scma.entity.Amiya;
import org.mesdag.scma.util.SCMAIdentifier;

public class ServerEntityRegistry {
    public static final EntityType<Amiya> amiya = ServerEntityRegistry.register("amiya", SpawnGroup.CREATURE, Amiya::new, 0.75f, 1.8f);

    public static <T extends Entity> EntityType<T> register(String id, SpawnGroup spawnGroup, EntityType.EntityFactory<T> factory, float boxWidth, float boxHeight) {
        return Registry.register(
                Registry.ENTITY_TYPE,
                new SCMAIdentifier(id),
                FabricEntityTypeBuilder
                        .create(spawnGroup, factory)
                        .dimensions(EntityDimensions.fixed(boxWidth, boxHeight)).build()
        );
    }
}
