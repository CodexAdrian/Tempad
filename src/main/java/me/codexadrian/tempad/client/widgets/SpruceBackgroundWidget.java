package me.codexadrian.tempad.client.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.util.ScissorManager;
import dev.lambdaurora.spruceui.widget.AbstractSpruceWidget;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import me.codexadrian.tempad.Tempad;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

import static me.codexadrian.tempad.Tempad.MODID;
import static me.codexadrian.tempad.Tempad.blend;

public class SpruceBackgroundWidget extends AbstractSpruceWidget {
    private final int PANEL_WIDTH;
    private final int PANEL_HEIGHT;
    private final int color;
    public static final ResourceLocation texture = new ResourceLocation(MODID, "textures/widget/tempad_grid.png");

    public SpruceBackgroundWidget(Position position, int width, int height, int color) {
        super(position);
        this.PANEL_WIDTH = width;
        this.PANEL_HEIGHT = height;
        this.color = color;
    }

    @Override
    protected void renderWidget(PoseStack matrices, int mouseX, int mouseY, float delta) {
        int backgroundColor = blend(Color.getColor("orange", color), Color.gray).getRGB();
        if (width <= 0) width = 1;
        if (height <= 0) height = 1;
        int u1 = 0;
        int v1 = 0;
        int u2 = 30;
        int v2 = 16;
        Tempad.coloredRect(matrices, getX(), getY(), PANEL_WIDTH + 4,PANEL_HEIGHT + 4, color);
        Tempad.texturedRect(matrices, texture, u1, u2, v1, v2, getX() + 2, getY() + 2, PANEL_WIDTH, PANEL_HEIGHT, backgroundColor);
    }
}
