package me.codexadrian.tempad.client.gui;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import io.netty.buffer.Unpooled;
import me.codexadrian.tempad.Tempad;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class TempadGuiDescription extends LightweightGuiDescription {
    public TempadGuiDescription(InteractionHand hand, ItemStack stack) {
        int orangeColor = 0xed9821;
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(320, 160);
        root.setInsets(Insets.ROOT_PANEL);
        this.addPainters();

        WButton timedoor = new WButton(new TranslatableComponent("gui.tempad.timedoorbutton"));
        timedoor.setOnClick(() ->{
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeEnum(hand);
            buf.writeInt(0);
            Minecraft.getInstance().setScreen(null);
            ClientPlayNetworking.send(Tempad.TIMEDOOR_PACKET, buf);
        });
        root.add(timedoor, 0, 3, 4, 1);

        WButton setPosition = new WButton(new TranslatableComponent("gui.tempad.positionbutton"));
        setPosition.setIcon(new ItemIcon(new ItemStack(Items.DIAMOND)));
        setPosition.setOnClick(() -> {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeEnum(hand);
            ClientPlayNetworking.send(Tempad.LOCATION_PACKET, buf);
        });
        root.add(setPosition, 0, 4, 4, 1);

        WLabel label = new WLabel(new TextComponent("Tempad"), orangeColor);
        root.add(label, 0, 0, 2, 1);

        root.validate(this);
    }

    @Override
    public void addPainters() {
        getRootPanel().setBackgroundPainter(BackgroundPainter.createColorful(0xFF_262626));
    }
}
