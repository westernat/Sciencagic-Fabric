package org.mesdag.scma.client.screen;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.mesdag.scma.screen.MeltingForgingHandler;
import org.mesdag.scma.util.SCMAIdentifier;

public class MeltingForgingScreen extends AbstractMachineScreen<MeltingForgingHandler> {
    private static final Identifier TEXTURE = new SCMAIdentifier("textures/gui/machine/melting_forging.png");

    public MeltingForgingScreen(MeltingForgingHandler handler, PlayerInventory inventory, Text title) {
        super(TEXTURE, handler, inventory, title);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        super.drawBackground(matrices, delta, mouseX, mouseY);
        drawTexture(matrices, x + 57, y + 30, 182, 0, handler.getGenerationProgress(), 26);
    }
}
