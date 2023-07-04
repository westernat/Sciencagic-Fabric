package org.mesdag.scma.client.screen;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.mesdag.scma.screen.RollerPressHandler;
import org.mesdag.scma.util.SCMAIdentifier;

public class RollerPressScreen extends AbstractMachineScreen<RollerPressHandler> {
    private static final Identifier TEXTURE = new SCMAIdentifier("textures/gui/machine/roller_press.png");

    public RollerPressScreen(RollerPressHandler handler, PlayerInventory inventory, Text title) {
        super(TEXTURE, handler, inventory, title);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        super.drawBackground(matrices, delta, mouseX, mouseY);
        drawTexture(matrices, x + 76, y + 39, 182, 0, handler.getGenerationProgress(), 16); // 进度条
    }
}
