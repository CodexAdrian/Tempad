package me.codexadrian.tempad.client.gui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;

public class TempadInterfaceGui extends CottonClientScreen {
    TempadGUIDescription tempadGUIDescription;
    public TempadInterfaceGui(TempadGUIDescription guiDescription) {
        super(guiDescription);
        this.tempadGUIDescription = guiDescription;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
