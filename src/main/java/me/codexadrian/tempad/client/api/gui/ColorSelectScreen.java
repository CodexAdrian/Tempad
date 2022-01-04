package me.codexadrian.tempad.client.api.gui;

import dev.lambdaurora.spruceui.Position;
import io.netty.buffer.Unpooled;
import me.codexadrian.tempad.Tempad;
import me.codexadrian.tempad.client.widgets.SpruceColorButton;
import me.codexadrian.tempad.client.widgets.SpruceIconWidget;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

import static me.codexadrian.tempad.Tempad.MODID;
import static me.codexadrian.tempad.Tempad.colors;

public class ColorSelectScreen extends BaseTempadScreen{
    public ColorSelectScreen(int color, Player player, InteractionHand hand) {
        super(color, player, hand, new TranslatableComponent(MODID, "gui.color_screen.title"));
    }

    @Override
    protected void init() {
        super.init();
        SpruceIconWidget doorIcon = new SpruceIconWidget(Position.of(0, 0), new ResourceLocation(MODID, "textures/widget/timedoor_sprite.png"), 16, 16);
        doorIcon.setTint(color);
        addWidget(doorIcon);
        int scale = 16;
        int colorPerRow = 10;
        int margin = 6;
        int startX = width/2 - (scale * 2 * colorPerRow + (colorPerRow - 1) * margin)/2;
        int startY = height/2 - (scale * 2 * (colors.length/colorPerRow) + (colorPerRow - 1) * margin)/4;
        int x = 0;
        int y = 0;
        for (int i = 0; i < colors.length; i++) {
            //Create button here
            //Set it's colors and stuff
            //Set it's position
            int finalI = i;
            SpruceColorButton button = new SpruceColorButton(Position.of(startX + x, startY + y), colors[i], scale * 2, scale * 2, button1 -> {
                FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                buf.writeInt(colors[finalI]);
                this.color = colors[finalI];
                //Minecraft.getInstance().setScreen(null);
                //Minecraft.getInstance().setScreen(new TempadInterfaceGui(new ColorSelectScreenDesc(color, this.player, this.hand)));
                ClientPlayNetworking.send(Tempad.SET_COLOR_PACKET, buf);
            });
            x += scale * 2 + margin;
            if((i+1) % colorPerRow == 0) {
                x = 0;
                y += scale * 2 + margin;
            }
            addWidget(button);
        }
    }
}
