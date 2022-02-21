package me.codexadrian.tempad.client.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.widget.AbstractSpruceWidget;
import me.codexadrian.tempad.Tempad;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

import static me.codexadrian.tempad.Tempad.MODID;
import static me.codexadrian.tempad.Tempad.blend;

public class SpruceBackgroundWidget extends BaseWidget {
    private static final int PANEL_WIDTH = 480;
    private static final int PANEL_HEIGHT = 256;
    private final int color;
    public static final ResourceLocation texture = new ResourceLocation(MODID, "textures/widget/tempad_grid.png");

    public SpruceBackgroundWidget(int x, int y, int color) {
        super(x, y, PANEL_WIDTH, PANEL_HEIGHT);
        this.color = color;
    }

    @Override
    public void renderWidget(PoseStack matrices, int mouseX, int mouseY, float delta) {
        int backgroundColor = blend(Color.getColor("orange", color), Color.gray).getRGB();
        int u1 = 0;
        int v1 = 0;
        int u2 = 30;
        int v2 = 16;
        Tempad.coloredRect(matrices, getX(), getY(), getWidth() + 4,getHeight() + 4, color);
        Tempad.texturedRect(matrices, texture, u1, u2, v1, v2, getX() + 2, getY() + 2, PANEL_WIDTH, PANEL_HEIGHT, backgroundColor);
    }
}
