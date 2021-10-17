package me.codexadrian.tempad.client.gui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class TempadInterfaceGui extends CottonClientScreen {
    TempadGUIDescription tempadGUIDescription;

    public TempadInterfaceGui(TempadGUIDescription guiDescription) {
        super(guiDescription);
        this.tempadGUIDescription = guiDescription;
    }
    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(new CottonClientScreen(new MainTempadScreenDesc(tempadGUIDescription.color, tempadGUIDescription.player, tempadGUIDescription.hand)) {
            @Override
            public boolean isPauseScreen() {
                return false;
            }
        });
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
