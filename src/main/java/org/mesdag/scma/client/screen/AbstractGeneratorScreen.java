package org.mesdag.scma.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.mesdag.scma.screen.AbstractGeneratorHandler;

public abstract class AbstractGeneratorScreen<T extends AbstractGeneratorHandler> extends HandledScreen<T> {
    protected final Identifier texture;

    public AbstractGeneratorScreen(Identifier texture, T handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.texture = texture;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, texture);
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
        if (handler.hasEnergy()) {
            int e = handler.getEnergyProgress();
            drawTexture(matrices, x + 147, y + 29 + 36 - e, 176, 36 - e, 6, e); // 能量条
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }
}
