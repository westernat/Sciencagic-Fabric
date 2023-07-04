package org.mesdag.scma.client.render.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import org.mesdag.scma.block.tool.PestleDish;
import org.mesdag.scma.block.tool.PestleDishEntity;

import java.nio.FloatBuffer;
import java.util.HashMap;

import static org.mesdag.scma.util.Properties.ON_USE;

@Environment(EnvType.CLIENT)
public class PestleDishRenderer implements BlockEntityRenderer<PestleDishEntity> {
    private static final ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
    private static final HashMap<PestleDishEntity, double[]> map = new HashMap<>();
    private static final double[] empty = new double[]{0.0, 0.0};
    private static final Matrix4f matrix1 = new Matrix4f();
    private static Matrix4f matrix2;

    public PestleDishRenderer(BlockEntityRendererFactory.Context ctx) {
        matrix2 = new Matrix4f(new Quaternion(Vec3f.POSITIVE_Y, (float) (Math.PI / 8), false));
        matrix2.multiplyByTranslation(0.125F, 0.0F, 0.125F);
        /*matrix2.multiply(new Quaternion(Vec3f.POSITIVE_X, (float) (Math.PI / 6), false));*/
    }

    @Override
    public void render(PestleDishEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (!map.containsKey(entity)) map.put(entity, empty);
        NbtCompound nbt = entity.getContextNbt();
        int level = nbt.getInt("pestleLevel");
        if (level != 0) {
            double tick;
            double[] d = map.get(entity);
            if (entity.getCachedState().get(ON_USE)) {
                double neoAngle = d[0] + 1.0;
                tick = (neoAngle + tickDelta) / 16.0 * level;
                map.put(entity, new double[]{neoAngle, tick});
            } else {
                tick = d[1];
            }
            matrix1.readRowMajor(FloatBuffer.wrap(new float[]{
                    (MathHelper.cos((float) tick) / 4.0F), 0.0F, (MathHelper.sin((float) tick) / 4.0F), 0.5F,
                    0.0F, 1.0F, 0.0F, 0.0F,
                    (-MathHelper.sin((float) tick) / 4.0F), 0.0F, (MathHelper.cos((float) tick) / 4.0F), 0.5F,
                    0.0F, 0.0F, 0.0F, 1.0F
            }));
            matrices.push();
            matrices.multiplyPositionMatrix(matrix1);
            matrices.translate(0.7, 0.3, 0.7);
            matrices.scale(3.2F, 1.2F, 3.2F);
            itemRenderer.renderItem(PestleDish.getPestleByLevel(level), ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
            matrices.pop();
        }
        int[] materials = nbt.getIntArray("materials");
        if (materials.length != 0) {
            matrices.push();
            matrices.translate(0.45F, 0.3F, 0.65F);
            matrices.scale(0.6F, 0.6F, 0.6F);
            for (int i : materials) {
                matrices.multiplyPositionMatrix(matrix2);
                itemRenderer.renderItem(new ItemStack(Item.byRawId(i)), ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
            }
            matrices.pop();
        }
    }
}
