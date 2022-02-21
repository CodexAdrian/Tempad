package me.codexadrian.tempad.client.widgets.libguilegacy;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import net.minecraft.network.chat.Component;

public class ScalableText extends WWidget {
    public int textColor;
    public Component textComponent;
    public int width, height = 18;

    protected HorizontalAlignment alignment = HorizontalAlignment.LEFT;

    public ScalableText(Component component, int textColor) {
        this.textComponent = component;
        this.textColor = textColor;
    }

    @Override
    public void paint(PoseStack matrices, int x, int y, int mouseX, int mouseY) {
        matrices.pushPose();
        matrices.translate(x * (1- this.getHeight() * 2/ 16F), y * (1 - this.getHeight() * 2/ 16F), 0);
        matrices.scale(this.getHeight() * 2 / 16F, this.getHeight() * 2 / 16F, 0);
        ScreenDrawing.drawStringWithShadow(matrices, textComponent.getVisualOrderText(), alignment, x, y, this.getWidth(), textColor);
        matrices.popPose();
    }

    @Override
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
