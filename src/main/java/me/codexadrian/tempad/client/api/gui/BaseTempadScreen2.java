package me.codexadrian.tempad.client.api.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import me.codexadrian.tempad.Tempad;
import me.codexadrian.tempad.client.widgets.BaseWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

import java.awt.*;

import static me.codexadrian.tempad.Tempad.MODID;
import static me.codexadrian.tempad.Tempad.blend;

public class BaseTempadScreen2 extends Screen {
    public static final int PANEL_WIDTH = 480;
    public static final int PANEL_HEIGHT = 256;
    public final Player player;
    public final InteractionHand hand;
    public int color;

    private static final ResourceLocation texture = new ResourceLocation(MODID, "textures/widget/tempad_grid.png");

    public BaseTempadScreen2(int color, Player player, InteractionHand hand, Component component) {
        super(component);
        this.color = color;
        this.player = player;
        this.hand = hand;
    }


    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack);
        children().forEach(guiEventListener -> ((BaseWidget) guiEventListener).renderWidget(poseStack, mouseX, mouseY, delta));
        super.render(poseStack, mouseX, mouseY, delta);
    }

    @Override
    public void renderBackground(PoseStack matrices) {
        super.renderBackground(matrices);
        int backgroundColor = blend(Color.getColor("orange", color), Color.gray).getRGB();
        int u1 = 0;
        int v1 = 0;
        int u2 = 30;
        int v2 = 16;
        Tempad.coloredRect(matrices, getCornerX(), getCornerY(), PANEL_WIDTH + 4,PANEL_HEIGHT + 4, color);
        Tempad.texturedRect(matrices, texture, u1, u2, v1, v2, getCornerX() + 2, getCornerY() + 2, PANEL_WIDTH, PANEL_HEIGHT, backgroundColor);
    }

    public int getCornerX() {
        return this.width/2 - PANEL_WIDTH/2;
    }

    public int getCornerY() {
        return this.height/2 - PANEL_HEIGHT/2;
    }

    public Player getPlayer() {
        return player;
    }

    public InteractionHand getHand() {
        return hand;
    }

    public int getColor() {
        return color;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
