package me.codexadrian.tempad.client.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class TextButton extends Button {
    private float padding;
    private final int color;

    public TextButton(int x, int y, int height, Component component, int color, OnPress onPress) {
        super(x, y, Minecraft.getInstance().font.width(component), Minecraft.getInstance().font.lineHeight, component, onPress);
        this.height = height;
        this.color = color;
    }

    @Override
    public void renderButton(PoseStack matrices, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;

        renderBg(matrices, minecraft, mouseX, mouseY);
        matrices.pushPose();
        matrices.pushPose();
        float height = this.getPaddedHeight() - padding;
        matrices.translate(x * (1- height * 2 / 16F), y * (1 - height * 2 / 16F), 0);
        matrices.scale(height * 2 / 16F, height * 2 / 16F, 0);
        matrices.translate(0, padding/2F, 0);
        int color = isWithinBounds(mouseX, mouseY) ? this.color : getOffColor();
        //drawCenteredString(matrices, font, getMessage(), x + (TempadScreen.this.width - WIDTH) / 2, y + (TempadScreen.this.height - HEIGHT) / 2, color);
        font.drawShadow(matrices, getMessage(), x, y, color);
        drawString(matrices, font, getMessage(), x, y, color);
        matrices.popPose();
    }

    public boolean isWithinBounds(int x, int y) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        return x >= this.x && y >= this.y && x < this.x + font.width(getMessage()) * (getHeight() * 2 / 16F) && y < this.y + this.height;
    }

    @Override
    public int getHeight() {
        return height + (int) Math.ceil(padding);
    }

    public float getPaddedHeight() {
        return height + padding;
    }

    public void setVerticalPadding(float padding) {
        this.padding = padding;
    }

    private int getOffColor() {
        Color c0 = Color.getColor("", color);

        double r = 0.5 * c0.getRed() + 0.5;
        double g = 0.5 * c0.getGreen() + 0.5;
        double b = 0.5 * c0.getBlue() + 0.5;
        double a = 1;

        return new Color((int) r, (int) g, (int) b, (int) a).getRGB();
    }
}
