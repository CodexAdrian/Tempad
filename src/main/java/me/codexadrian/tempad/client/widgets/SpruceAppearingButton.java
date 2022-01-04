package me.codexadrian.tempad.client.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget;
import me.codexadrian.tempad.Tempad;
import net.minecraft.network.chat.Component;

public class SpruceAppearingButton extends SpruceButtonWidget {
    public SpruceAppearingButton(Position position, int width, int height, Component message, PressAction action) {
        super(position, width, height, message, action);
    }

    @Override
    protected void renderButton(PoseStack matrices, int mouseX, int mouseY, float delta) {
        Tempad.coloredRect(matrices, getX(), getY(), width, height, 0x22_FFFFFF);
    }
}
