package me.codexadrian.tempad.client.widgets.libguilegacy;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.icon.Icon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class DynamicButton extends WButton {
    private ResourceLocation WIDGET_LOCATION = new ResourceLocation("textures/gui/widgets.png");
    private Component label;
    private int textColor;
    protected HorizontalAlignment alignment = HorizontalAlignment.CENTER;

    @Nullable
    private Icon icon = null;

    public DynamicButton() {
    }

    public DynamicButton(@Nullable Icon icon) {
        super(icon);
        this.icon = icon;
    }

    public DynamicButton(Component label) {
        super(label);
        this.label = label;
    }

    public DynamicButton(@Nullable Icon icon, Component label) {
        super(icon, label);
        this.icon = icon;
        this.label = label;
    }

    public void paint(PoseStack matrices, int x, int y, int mouseX, int mouseY) {
        boolean hovered = (mouseX >= 0 && mouseY >= 0 && mouseX < getWidth() && mouseY < getHeight());
        int state = 1; //1=regular. 2=hovered. 0=disabled.
        if (hovered || isFocused()) {
            state = 2;
        }

        float px = 1 / 256f;
        float buttonLeft = 0 * px;
        float buttonTop = (46 + (state * 20)) * px;
        int halfWidth = getWidth() / 2;
        if (halfWidth > 198) halfWidth = 198;
        float buttonWidth = halfWidth * px;
        float buttonHeight = 20 * px;

        float buttonEndLeft = (200 - (getWidth() / 2)) * px;

        ResourceLocation texture = getTexture();
        ScreenDrawing.texturedRect(matrices, x, y, getWidth() / 2, 20, texture, buttonLeft, buttonTop, buttonLeft + buttonWidth, buttonTop + buttonHeight, 0xFFFFFFFF);
        ScreenDrawing.texturedRect(matrices, x + (getWidth() / 2), y, getWidth() / 2, 20, texture, buttonEndLeft, buttonTop, 200 * px, buttonTop + buttonHeight, 0xFFFFFFFF);

        if (icon != null) {
            icon.paint(matrices, x + 1, y + 1, 16);
        }

        if (label != null) {
            int xOffset = (icon != null && alignment == HorizontalAlignment.LEFT) ? 18 : 0;
            ScreenDrawing.drawStringWithShadow(matrices, label.getVisualOrderText(), alignment, x + xOffset, y + ((20 - 8) / 2), width, textColor);
        }
    }

    @Environment(EnvType.CLIENT)
    public void setTexture(ResourceLocation location) {
        this.WIDGET_LOCATION = location;
    }

    @Environment(EnvType.CLIENT)
    public ResourceLocation getTexture() {
        return this.WIDGET_LOCATION;
    }

    @Override
    public WButton setLabel(Component label) {
        this.label = label;
        return this;
    }

    @Override
    public WButton setIcon(@Nullable Icon icon) {
        this.icon = icon;
        return this;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }
}
