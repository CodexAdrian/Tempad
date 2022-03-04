package me.codexadrian.tempad.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.codexadrian.tempad.Tempad;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class TempadScreen extends Screen {
    private static final ResourceLocation GRID = new ResourceLocation(Tempad.MODID, "textures/widget/tempad_grid.png");
    private static final ResourceLocation TVA_LOGO = new ResourceLocation(Tempad.MODID, "textures/widget/tva_logo.png");
    private final int color;

    private static final int WIDTH = 480;
    private static final int HEIGHT = 256;

    public TempadScreen(int color) {
        super(Component.nullToEmpty(""));
        this.color = color;
    }

    @Override
    protected void init() {
        super.init();

        addRenderableWidget(new TextButton((width - WIDTH) / 2 + 272, (height - HEIGHT) / 2 + 132, new TranslatableComponent("gui." + Tempad.MODID + ".options"), button -> {
        }));
    }

    private void renderOutline(PoseStack poseStack) {
        int lineWidth = 4;
        fill(poseStack, (width - WIDTH - lineWidth) / 2, (height - HEIGHT - lineWidth) / 2, (width + WIDTH + lineWidth) / 2, (height + HEIGHT + lineWidth) / 2, color | 0xFF000000);
    }

    private void renderGridBackground(PoseStack poseStack, float red, float green, float blue) {
        RenderSystem.setShaderTexture(0, GRID);
        RenderSystem.setShaderColor(red * 0.5f, green * 0.5f, blue * 0.5f, 1f);
        blit(poseStack, (width - WIDTH) / 2, (height - HEIGHT) / 2, WIDTH, HEIGHT, 0, 0, WIDTH, HEIGHT, 16, 16);
    }

    private void renderTvaLogo(PoseStack poseStack, float red, float green, float blue) {
        int tvaWidth = WIDTH / 2 + 16;
        int tvaHeight = HEIGHT / 2;
        RenderSystem.setShaderTexture(0, TVA_LOGO);
        RenderSystem.setShaderColor(red, green, blue, 1f);
        blit(poseStack, width / 2 - tvaWidth + 24, (height - tvaHeight) / 2, tvaWidth, tvaHeight, 0, 0, 32, 16, 32, 16);
    }

    @Override
    public void renderBackground(PoseStack poseStack, int offset) {
        super.renderBackground(poseStack, offset);

        float red = (color >> 16 & 0xFF) / 255f;
        float green = (color >> 8 & 0xFF) / 255f;
        float blue = (color & 0xFF) / 255f;
        renderOutline(poseStack);
        renderGridBackground(poseStack, red, green, blue);
        renderTvaLogo(poseStack, red, green, blue);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private class TextButton extends Button {
        public TextButton(int x, int y, Component component, OnPress onPress) {
            super(x, y, Minecraft.getInstance().font.width(component), Minecraft.getInstance().font.lineHeight, component, onPress);
        }

        @Override
        public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
            Minecraft minecraft = Minecraft.getInstance();
            Font font = minecraft.font;
            renderBg(poseStack, minecraft, mouseX, mouseY);
            poseStack.pushPose();
            drawCenteredString(poseStack, font, getMessage(), x + (TempadScreen.this.width - WIDTH) / 2, y + (TempadScreen.this.height - HEIGHT) / 2, color);
            poseStack.popPose();
        }
    }
}
