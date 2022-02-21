package me.codexadrian.tempad.client.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import me.codexadrian.tempad.Tempad;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.resources.ResourceLocation;

public class SpruceIconWidget extends BaseWidget {

    private final ResourceLocation image;
    private final int tint;

    public SpruceIconWidget(int x, int y, ResourceLocation image, int width, int height, int tint) {
        super(x, y, width, height);
        this.image = image;
        this.tint = tint;
    }

    @Override
    public void renderWidget(PoseStack matrices, int mouseX, int mouseY, float delta) {
        Tempad.texturedRect(matrices, image, 0, 0, 1, 1, getX(), getY(), getWidth(), getHeight(), tint);
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {

    }
}
