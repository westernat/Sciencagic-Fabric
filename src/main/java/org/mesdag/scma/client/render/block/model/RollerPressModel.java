package org.mesdag.scma.client.render.block.model;

import net.minecraft.util.Identifier;
import org.mesdag.scma.block.energy.entity.machine.RollerPressEntity;
import org.mesdag.scma.util.SCMAIdentifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class RollerPressModel extends AnimatedGeoModel<RollerPressEntity> {
    @Override
    public Identifier getModelLocation(RollerPressEntity object) {
        return new SCMAIdentifier("geo/block/roller_press.geo.json");
    }

    @Override
    public Identifier getTextureLocation(RollerPressEntity object) {
        return new SCMAIdentifier("textures/block/machine/roller_press.png");
    }

    @Override
    public Identifier getAnimationFileLocation(RollerPressEntity animatable) {
        return new SCMAIdentifier("animations/block/roller_press.animation.json");
    }
}
