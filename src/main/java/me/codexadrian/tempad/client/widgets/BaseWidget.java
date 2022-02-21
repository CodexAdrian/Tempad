package me.codexadrian.tempad.client.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;

public abstract class BaseWidget extends GuiComponent implements GuiEventListener, NarratableEntry {
    private final int width, height, widgetX, widgetY;
    public String descriptor = "";

    public BaseWidget(int x, int y, int width, int height) {
        this.widgetX = x;
        this.widgetY= y;
        this.width = width;
        this.height = height;
    }

    public abstract void renderWidget(PoseStack matrices, int mouseX, int mouseY, float delta);

    public int getX() {
        return widgetX;
    }

    public int getY() {
        return widgetY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.HOVERED;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    @Override
    public void updateNarration(NarrationElementOutput builder) {
        builder.add(NarratedElementType.TITLE, this.descriptor);
    }

}
