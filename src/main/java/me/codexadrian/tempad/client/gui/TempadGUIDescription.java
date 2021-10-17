package me.codexadrian.tempad.client.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WPanel;

import static me.codexadrian.tempad.Tempad.drawUnifiedBackground;

public class TempadGUIDescription extends LightweightGuiDescription {
    public int color;

    public TempadGUIDescription(int color) {
        this.color = color;
    }

    @Override
    public void addPainters() {
        WPanel root = getRootPanel();
        drawUnifiedBackground(root, color, true);
    }
}
