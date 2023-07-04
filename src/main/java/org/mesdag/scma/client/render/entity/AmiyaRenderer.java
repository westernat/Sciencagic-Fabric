package org.mesdag.scma.client.render.entity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import org.mesdag.scma.client.render.entity.model.AmiyaModel;
import org.mesdag.scma.entity.Amiya;
import org.mesdag.scma.util.SCMAIdentifier;

import static org.mesdag.scma.client.render.ModelLayers.amiya_model_layer;


public class AmiyaRenderer extends MobEntityRenderer<Amiya, AmiyaModel> {
    public AmiyaRenderer(EntityRendererFactory.Context context) {
        super(context, new AmiyaModel(context.getPart(amiya_model_layer)), 0.5f);
    }

    @Override
    public Identifier getTexture(Amiya entity) {
        return new SCMAIdentifier("textures/entity/amiya.png");
    }
}
