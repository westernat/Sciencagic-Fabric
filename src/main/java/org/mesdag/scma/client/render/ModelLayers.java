package org.mesdag.scma.client.render;

import net.minecraft.client.render.entity.model.EntityModelLayer;
import org.mesdag.scma.util.SCMAIdentifier;

public class ModelLayers {
    public static final EntityModelLayer amiya_model_layer = register("amiya");
    public static final EntityModelLayer roller = register("roller");


    public static EntityModelLayer register(String id) {
        return register(id, "main");
    }

    public static EntityModelLayer register(String id, String name) {
        return new EntityModelLayer(new SCMAIdentifier(id), name);
    }
}
