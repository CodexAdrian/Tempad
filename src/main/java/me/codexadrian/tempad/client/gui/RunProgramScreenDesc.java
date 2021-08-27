package me.codexadrian.tempad.client.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import io.netty.buffer.Unpooled;
import me.codexadrian.tempad.Tempad;
import me.codexadrian.tempad.client.widgets.DynamicButton;
import me.codexadrian.tempad.client.widgets.HighlightedTextButton;
import me.codexadrian.tempad.client.widgets.ScalableText;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static me.codexadrian.tempad.Tempad.*;

public class RunProgramScreenDesc extends LightweightGuiDescription {
    public int color;
    public RunProgramScreenDesc(InteractionHand hand, ItemStack stack, int color) {
        int scale = 16;
        this.color = color;
        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        root.setSize(480, 256);
        this.addPainters();
        AtomicInteger index = new AtomicInteger();
        WListPanel<BlockPos, HighlightedTextButton> positionList = new WListPanel<>(Arrays.stream(stack.getOrCreateTag().getLongArray("locations")).mapToObj(BlockPos::of).collect(Collectors.toSet()).stream().toList(), HighlightedTextButton::new, (blockPos, tempadButton) -> {
            tempadButton.setTextComponent(new TextComponent(blockPos.toString()));
            tempadButton.setColor(ORANGE,  blend(Color.getColor("tempad_bg", color), Color.black).getRGB());
            tempadButton.setSize(scale * 10, 12);
            tempadButton.setOnClick(() -> {
                FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                buf.writeBlockPos(blockPos);
                buf.writeInt(index.getAndIncrement());
                Minecraft.getInstance().setScreen(null);
                ClientPlayNetworking.send(Tempad.TIMEDOOR_PACKET, buf);
            });
        });
        root.add(positionList, 17 * scale, 0, 12 * scale, 12 * scale);

        WTextField nameField = new WTextField(new TextComponent("Name here"));
        nameField.setEnabledColor(ORANGE);
        //root.add(nameField, 12, 0, 5, 1);

        WButton timedoor = new WButton(new TranslatableComponent("gui.tempad.timedoorbutton"));
        timedoor.setOnClick(() -> {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeEnum(hand);
            buf.writeInt(0);
            Minecraft.getInstance().setScreen(null);
            ClientPlayNetworking.send(Tempad.TIMEDOOR_PACKET, buf);
        });
        //root.add(timedoor, 0, 3, 4, 1);

        WButton setPosition = new DynamicButton(new TranslatableComponent("gui.tempad.positionbutton"));

        setPosition.setIcon(new ItemIcon(new ItemStack(Items.DIAMOND)));
        setPosition.setOnClick(() -> {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeEnum(hand);
            ClientPlayNetworking.send(Tempad.LOCATION_PACKET, buf);
        });
        //root.add(setPosition, 0, 4, 4, 1);

        ScalableText label = new ScalableText(new TextComponent("Tempad"), ORANGE);
        label.setSize(2 * scale, 2 * scale);
        root.add(label, 0, 0);

        root.validate(this);
    }

    @Override
    public void addPainters() {
        WPanel root = getRootPanel();
        drawUnifiedBackground(root, color);
    }
}
