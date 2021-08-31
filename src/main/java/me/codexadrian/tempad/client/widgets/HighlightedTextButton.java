package me.codexadrian.tempad.client.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.Nullable;

public class HighlightedTextButton extends WWidget {

    public int colorOn, colorOff;
    public Component textComponent;
    public int width, height = 16;
    @Nullable
    private Runnable onClick;

    protected HorizontalAlignment alignment = HorizontalAlignment.LEFT;
    private float padding;

    public HighlightedTextButton() {
    }

    public HighlightedTextButton(Component component, int colorOn, int colorOff) {
        this.textComponent = component;
        this.colorOn = colorOn;
        this.colorOff = colorOff;
    }

    @Override
    public void paint(PoseStack matrices, int x, int y, int mouseX, int mouseY) {
        matrices.pushPose();
        float height = this.getPaddedHeight() - padding;
        matrices.translate(x * (1- height * 2 / 16F), y * (1 - height * 2 / 16F), 0);
        matrices.scale(height * 2 / 16F, height * 2 / 16F, 0);
        matrices.translate(0, padding/2F, 0);
        int color = isWithinBounds(mouseX, mouseY) ? colorOn : colorOff;
        ScreenDrawing.drawStringWithShadow(matrices, textComponent.getVisualOrderText(), alignment, x, y, this.getWidth(), color);
        matrices.popPose();
    }

    @Environment(EnvType.CLIENT)
    @Override
    public InputResult onClick(int x, int y, int button) {
        super.onClick(x, y, button);

        if (isWithinBounds(x, y)) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));

            if (onClick!=null) onClick.run();
            return InputResult.PROCESSED;
        }
        return InputResult.IGNORED;
    }

    public Component getTextComponent() {
        return textComponent;
    }

    public void setTextComponent(Component textComponent) {
        this.textComponent = textComponent;
    }

    public void setColor(int colorOn, int colorOff) {
        this.colorOn = colorOn;
        this.colorOff = colorOff;
    }
    @Override
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height + (int) Math.ceil(padding);
    }

    public float getPaddedHeight() {
        return height + padding;
    }

    public void setOnClick(@Nullable Runnable onClick) {
        this.onClick = onClick;
    }

    @Override
    public boolean isWithinBounds(int x, int y) {
        return x>=0 && y>=0 && x<this.width && y<this.height;
    }

    public void setVerticalPadding(float padding) {
        this.padding = padding;
    }
}
