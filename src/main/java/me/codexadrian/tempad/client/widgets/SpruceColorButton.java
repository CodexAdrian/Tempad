package me.codexadrian.tempad.client.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import me.codexadrian.tempad.Tempad;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import java.awt.*;

import static me.codexadrian.tempad.Tempad.blend;

public class SpruceColorButton extends SpruceButtonWidget {
    int color;
    int borderColor = 0xFF_000000;
    public SpruceColorButton(Position position, int color, int width, int height, PressAction action) {
        super(position, width, height, new TextComponent(""), action);
        this.color = color;
    }

    @Override
    protected void renderButton(PoseStack matrices, int mouseX, int mouseY, float delta) {
        matrices.pushPose();
        Tempad.coloredRect(matrices, getX()-1, getY()-1, width+2, height+2, blend(Color.gray, Color.getColor("border", color)).getRGB());
        Tempad.coloredRect(matrices, getX(), getY(), width, height, color);
        matrices.popPose();
    }
}
