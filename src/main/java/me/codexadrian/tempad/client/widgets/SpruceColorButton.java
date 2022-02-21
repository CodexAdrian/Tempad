package me.codexadrian.tempad.client.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import me.codexadrian.tempad.Tempad;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import java.awt.*;

import static me.codexadrian.tempad.Tempad.blend;

public class SpruceColorButton extends BaseWidget {
    int color;
    public final int borderColor;
    public SpruceColorButton(int x, int y, int color, int width, int height, SpruceButtonWidget.PressAction action) {
        this(x, y, color, 0xFF_000000, width, height, action);
    }

    public SpruceColorButton(int x, int y, int color, int borderColor, int width, int height, SpruceButtonWidget.PressAction action) {
        super(x, y, width, height);
        this.color = color;
        this.borderColor = borderColor;
    }

    @Override
    public void renderWidget(PoseStack matrices, int mouseX, int mouseY, float delta) {
        matrices.pushPose();
        Tempad.coloredRect(matrices, getX()-1, getY()-1, getWidth()+2, getHeight()+2, blend(Color.gray, Color.getColor("border", color)).getRGB());
        Tempad.coloredRect(matrices, getX(), getY(), getWidth(), getHeight(), color);
        matrices.popPose();
    }

}
