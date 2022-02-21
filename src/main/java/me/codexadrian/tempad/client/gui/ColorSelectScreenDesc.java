package me.codexadrian.tempad.client.gui;


import io.github.cottonmc.cotton.gui.widget.WPanel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.netty.buffer.Unpooled;
import me.codexadrian.tempad.Tempad;
import me.codexadrian.tempad.client.widgets.libguilegacy.ColorButton;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

import static me.codexadrian.tempad.Tempad.colors;
import static me.codexadrian.tempad.Tempad.drawUnifiedBackground;

public class ColorSelectScreenDesc extends TempadGUIDescription {
    public int color;
    public ColorSelectScreenDesc(int passedColor, Player player, InteractionHand hand) {
        super(passedColor, player, hand);
        int scale = 16;
        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        root.setSize(scale * 30, scale * 16);
        this.addPainters();

        int colorPerRow = 10;
        int margin = 6;
        int startX = scale * 3 + 5;
        int startY = scale * 5 + 2;
        int x = 0;
        int y = 0;
        for (int i = 0; i < colors.length; i++) {
            //Create button here
            //Set it's colors and stuff
            //Set it's position
            ColorButton button = new ColorButton(colors[i]);
            button.setSize(scale * 2, scale * 2);
            int finalI = i;
            button.setOnClick(() -> {
                FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                buf.writeInt(colors[finalI]);
                this.color = colors[finalI];
                //Minecraft.getInstance().setScreen(null);
                Minecraft.getInstance().setScreen(new TempadInterfaceGui(new ColorSelectScreenDesc(color, player, hand)));
                ClientPlayNetworking.send(Tempad.SET_COLOR_PACKET, buf);
            });
            root.add(button, startX + x, startY + y);

            x += scale * 2 + margin;
            if((i+1) % colorPerRow == 0) {
                x = 0;
                y += scale * 2 + margin;
            }
        }
        root.validate(this);
        addPainters();
    }

    @Override
    public void addPainters() {
        WPanel root = getRootPanel();
        drawUnifiedBackground(root, 0xFF_FFFFFF, false);
    }


}
