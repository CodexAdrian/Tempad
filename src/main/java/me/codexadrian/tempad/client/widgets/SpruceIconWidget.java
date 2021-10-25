package me.codexadrian.tempad.client.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.widget.AbstractSpruceWidget;
import dev.lambdaurora.spruceui.widget.SpruceWidget;
import me.codexadrian.tempad.Tempad;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.resources.ResourceLocation;

public class SpruceIconWidget extends AbstractSpruceWidget {

    private final ResourceLocation image;
    private int tint;

    public SpruceIconWidget(Position position, ResourceLocation image, int width, int height) {
        super(position);
        this.image = image;
        this.width = width;
        this.height = height;
    }

    public void setTint(int color) {
        this.tint = color;
    }

    @Override
    protected void renderWidget(PoseStack matrices, int mouseX, int mouseY, float delta) {
        Tempad.texturedRect(matrices, image, 0, 0, 1, 1, getX(), getY(), width, height, tint);
    }
}
