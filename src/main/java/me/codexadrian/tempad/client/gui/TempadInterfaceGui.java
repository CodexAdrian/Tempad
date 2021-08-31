package me.codexadrian.tempad.client.gui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;

public class TempadInterfaceGui extends CottonClientScreen {
    public TempadInterfaceGui(GuiDescription guiDescription) {
        super(guiDescription);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
