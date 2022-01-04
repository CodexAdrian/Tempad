package me.codexadrian.tempad.client.api.gui;

import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.screen.SpruceScreen;
import me.codexadrian.tempad.client.widgets.SpruceBackgroundWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

import static me.codexadrian.tempad.Tempad.MODID;

public class BaseTempadScreen extends Screen {
    public static final int PANEL_WIDTH = 480;
    public static final int PANEL_HEIGHT = 256;
    public final Player player;
    public final InteractionHand hand;
    public int color;
    public static final ResourceLocation texture = new ResourceLocation(MODID, "textures/widget/tempad_grid.png");

    public BaseTempadScreen(int color, Player player, InteractionHand hand, Component component) {
        super(component);
        this.color = color;
        this.player = player;
        this.hand = hand;
    }

    @Override
    protected void init() {
        super.init();
        SpruceBackgroundWidget backgroundWidget = new SpruceBackgroundWidget(Position.of(getLeft(), getTop()), PANEL_WIDTH, PANEL_HEIGHT, color);
        addWidget(backgroundWidget);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public int getLeft() {
        return (this.width / 2) - (PANEL_WIDTH / 2);
    }

    public int getTop() {
        return (this.height / 2) - (PANEL_HEIGHT / 2);
    }
}
