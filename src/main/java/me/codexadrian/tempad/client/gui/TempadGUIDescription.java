package me.codexadrian.tempad.client.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WPanel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import static me.codexadrian.tempad.Tempad.drawUnifiedBackground;

public class TempadGUIDescription extends LightweightGuiDescription {
    public int color;
    @NotNull Player player;
    @NotNull InteractionHand hand;


    public TempadGUIDescription(int color, @NotNull Player player, @NotNull InteractionHand hand) {
        this.color = color;
        this.player = player;
        this.hand = hand;
    }

    @Override
    public void addPainters() {
        WPanel root = getRootPanel();
        drawUnifiedBackground(root, color, true);
    }
}
