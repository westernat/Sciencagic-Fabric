package org.mesdag.scma.client.render.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.mesdag.scma.entity.Amiya;

// Made with Blockbench 4.6.5
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
@Environment(EnvType.CLIENT)
public class AmiyaModel extends EntityModel<Amiya> {
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart leftArm;
    private final ModelPart rightArm;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;

    public AmiyaModel(ModelPart modelPart) {
        this.body = modelPart.getChild("body");
        this.head = body.getChild("head");
        this.leftArm = body.getChild("leftArm");
        this.rightArm = body.getChild("rightArm");
        this.leftLeg = body.getChild("leftLeg");
        this.rightLeg = body.getChild("rightLeg");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        ModelPartData R4 = head.addChild("R4", ModelPartBuilder.create(), ModelTransform.pivot(2.5F, -8.0F, 2.0F));
        R4.addChild("R4_r1", ModelPartBuilder.create().uv(0, 67).cuboid(-1.0F, -6.0F, 0.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, 0.0F, 0.0F, -0.1745F, 0.0F, 0.1309F));
        ModelPartData L4 = head.addChild("L4", ModelPartBuilder.create(), ModelTransform.pivot(-2.5F, -8.0F, 2.0F));
        L4.addChild("L4_r1", ModelPartBuilder.create().uv(0, 67).cuboid(-2.0F, -6.0F, 0.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.5F, 0.0F, 0.0F, -0.1745F, 0.0F, -0.1309F));
        head.addChild("hat", ModelPartBuilder.create().uv(32, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.5F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        ModelPartData leftArm = body.addChild("leftArm", ModelPartBuilder.create().uv(32, 48).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(5.0F, 2.5F, 0.0F));
        leftArm.addChild("leftSleeve", ModelPartBuilder.create().uv(48, 48).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        leftArm.addChild("leftItem", ModelPartBuilder.create(), ModelTransform.pivot(1.0F, 7.0F, 1.0F));
        ModelPartData rightArm = body.addChild("rightArm", ModelPartBuilder.create().uv(40, 16).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-5.0F, 2.5F, 0.0F));
        rightArm.addChild("rightSleeve", ModelPartBuilder.create().uv(40, 32).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        rightArm.addChild("rightItem", ModelPartBuilder.create(), ModelTransform.pivot(-1.0F, 7.0F, 1.0F));
        body.addChild("jacket", ModelPartBuilder.create().uv(23, 38).cuboid(-1.0F, 1.0F, -2.25F, 2.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        ModelPartData rightLeg = body.addChild("rightLeg", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.9F, 12.0F, 0.0F));
        rightLeg.addChild("rightPants", ModelPartBuilder.create().uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        ModelPartData leftLeg = body.addChild("leftLeg", ModelPartBuilder.create().uv(16, 48).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(1.9F, 12.0F, 0.0F));
        leftLeg.addChild("leftPants", ModelPartBuilder.create().uv(0, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        ModelPartData l = body.addChild("l", ModelPartBuilder.create().uv(11, 67).cuboid(-1.0F, 0.0F, -0.5F, 2.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.0F, 0.0F, -2.0F));
        ModelPartData l2 = l.addChild("l2", ModelPartBuilder.create().uv(11, 71).cuboid(-1.0F, 0.0F, -0.5F, 2.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 3.0F, 0.0F));
        l2.addChild("l3", ModelPartBuilder.create().uv(11, 76).cuboid(-0.5F, 0.0F, -0.4F, 1.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.5F, 1.0F, 0.0F));
        ModelPartData r = body.addChild("r", ModelPartBuilder.create().uv(19, 67).cuboid(-1.0F, 0.0F, -0.5F, 2.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(3.0F, 0.0F, -2.0F));
        ModelPartData r2 = r.addChild("r2", ModelPartBuilder.create().uv(19, 71).cuboid(-1.0F, 0.0F, -0.5F, 2.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 3.0F, 0.0F));
        r2.addChild("r3", ModelPartBuilder.create().uv(19, 76).cuboid(-0.5F, 0.0F, -0.4F, 1.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.5F, 1.0F, 0.0F));
        ModelPartData b = body.addChild("b", ModelPartBuilder.create().uv(36, 67).cuboid(-1.5F, 0.0F, -1.0F, 3.0F, 8.0F, 2.0F, new Dilation(0.5F)), ModelTransform.pivot(2.5F, -8.0F, 3.0F));
        ModelPartData b2 = b.addChild("b2", ModelPartBuilder.create().uv(26, 67).cuboid(-1.5F, 0.0F, 0.25F, 3.0F, 6.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 8.0F, -1.0F));
        b2.addChild("b3", ModelPartBuilder.create().uv(25, 74).cuboid(-1.5F, 0.0F, 0.25F, 3.0F, 6.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 6.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 128);
    }

    @Override
    public void setAngles(Amiya entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.head.yaw = headYaw * ((float) Math.PI / 180);
        this.head.pitch = headPitch * ((float) Math.PI / 180);
        this.leftArm.pitch = MathHelper.cos(limbAngle * 0.6662f) * 1.4f * limbDistance;
        this.rightArm.pitch = MathHelper.cos(limbAngle * 0.6662f + (float) Math.PI) * 1.4f * limbDistance;
        this.leftLeg.pitch = MathHelper.cos(limbAngle * 0.6662f + (float) Math.PI) * 1.4f * limbDistance;
        this.rightLeg.pitch = MathHelper.cos(limbAngle * 0.6662f) * 1.4f * limbDistance;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        this.body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}