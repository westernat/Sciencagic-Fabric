package org.mesdag.scma.client.screen;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.mesdag.scma.screen.ThermalGeneratorHandler;
import org.mesdag.scma.util.SCMAIdentifier;

public class ThermalGeneratorScreen extends AbstractGeneratorScreen<ThermalGeneratorHandler> {
    private static final Identifier TEXTURE = new SCMAIdentifier("textures/gui/machine/thermal_generator.png");

    public ThermalGeneratorScreen(ThermalGeneratorHandler handler, PlayerInventory inventory, Text title) {
        super(TEXTURE, handler, inventory, title);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        super.drawBackground(matrices, delta, mouseX, mouseY);
        int g = handler.getGenerationProgress();
        drawTexture(matrices, x + 79, y + 28 + 18 - g, 182, 18 - g, 18, g);
    }
}
