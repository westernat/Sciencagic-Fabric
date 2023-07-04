package org.mesdag.scma.client.render.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.mesdag.scma.block.energy.entity.machine.RollerPressEntity;
import org.mesdag.scma.client.render.block.model.RollerPressModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

@Environment(EnvType.CLIENT)
public class RollerPressRenderer extends GeoBlockRenderer<RollerPressEntity> {
    public RollerPressRenderer(BlockEntityRendererFactory.Context ctx) {
        super(new RollerPressModel());
    }

    @Override
    public RenderLayer getRenderType(RollerPressEntity animatable, float partialTick, MatrixStack poseStack, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, int packedLight, Identifier texture) {
        return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
    }
}
