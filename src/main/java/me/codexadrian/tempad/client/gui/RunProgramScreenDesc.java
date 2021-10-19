package me.codexadrian.tempad.client.gui;

import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import io.netty.buffer.Unpooled;
import me.codexadrian.tempad.Tempad;
import me.codexadrian.tempad.client.widgets.ColorableTextField;
import me.codexadrian.tempad.client.widgets.HighlightedTextButton;
import me.codexadrian.tempad.client.widgets.ScalableText;
import me.codexadrian.tempad.tempad.LocationData;
import me.codexadrian.tempad.tempad.TempadComponent;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import javax.xml.stream.Location;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static me.codexadrian.tempad.Tempad.MODID;
import static me.codexadrian.tempad.Tempad.blend;

public class RunProgramScreenDesc extends TempadGUIDescription {
    boolean locationBuilder;

    public RunProgramScreenDesc(boolean locationBuilder, boolean pointManager, @Nullable LocationData location, InteractionHand hand, Player player, int color) {
        super(color, player, hand);
        ItemStack stack = player.getItemInHand(hand);
        this.locationBuilder = locationBuilder;
        int scale = 16;
        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        root.setSize(480, 256);
        this.addPainters();
        int darkerColor = blend(Color.getColor("tempad_bg", color), Color.black).getRGB();
        int evenDarkerColor = blend(Color.getColor("tempad_bg", darkerColor), Color.black).getRGB();
        if (stack.hasTag()) {
            TempadComponent component = TempadComponent.fromStack(stack);
            WListPanel<LocationData, HighlightedTextButton> positionList = new WListPanel<>(new ArrayList<>(component.getLocations()), HighlightedTextButton::new, (tempadLocation, tempadButton) -> {
                tempadButton.setTextComponent(new TextComponent(tempadLocation.getName()));
                tempadButton.setColor(color, darkerColor);
                tempadButton.setSize(scale * 10, 12);
                tempadButton.setVerticalPadding(.5F);
                tempadButton.setOnClick(() -> {
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    buf.writeResourceLocation(tempadLocation.getLevelKey().location());
                    buf.writeBlockPos(tempadLocation.getBlockPos());
                    //Minecraft.getInstance().setScreen(null);
                    Minecraft.getInstance().setScreen(new TempadInterfaceGui(new RunProgramScreenDesc(false, true, tempadLocation, hand, player, color)));
                    //ClientPlayNetworking.send(Tempad.TIMEDOOR_PACKET, buf);
                });
            });
            root.add(positionList, 17 * scale, 28, 12 * scale, 12 * scale);
        }


        WSprite TimedoorSprite = new WSprite(new ResourceLocation(MODID, "textures/widget/timedoor_sprite.png"));
        TimedoorSprite.setTint(color);
        root.add(TimedoorSprite, scale * 4, scale * 2, scale * 9, scale * 9);

        ColorableTextField nameField = new ColorableTextField(new TranslatableComponent("gui.tempad.textfield"))
                .setBoxColor(evenDarkerColor)
                .setBoxFocusColor(color)
                .setBoxUnfocusedColor(darkerColor)
                .setEnabledColor(color)
                .setDisabledColor(darkerColor)
                .setSuggestionColor(darkerColor);
        nameField.setMaxLength(10);


        HighlightedTextButton newPosition = new HighlightedTextButton(new TranslatableComponent("gui.tempad.new_location"), color, darkerColor);
        newPosition.setSize(scale * 3, 12);
        newPosition.setOnClick(() -> Minecraft.getInstance().setScreen(new TempadInterfaceGui(new RunProgramScreenDesc(!locationBuilder, false, !locationBuilder ? new LocationData("", null, player.blockPosition()) : null, hand, player, color))));
        root.add(newPosition, 17 * scale, 12, 12 * scale, scale);

        if (locationBuilder) locationBuilder(root, nameField, color, darkerColor, hand);

        if (pointManager) pointManager(root, location, color, darkerColor, hand);

        if (location != null) {
            ScalableText onScreenLocation = new ScalableText(new TextComponent("(" + location.getBlockPos().toShortString() + ")"), color);
            root.add(onScreenLocation, scale * 5, scale, scale * 7, scale);
        }

        root.validate(this);
    }

    public static void locationBuilder(WPlainPanel root, ColorableTextField nameField, int color, int darkerColor, InteractionHand hand) {
        int scale = 16;
        root.add(nameField, (int) (scale * 5.5), scale * 11, scale * 6, scale);
        HighlightedTextButton addLocation = new HighlightedTextButton(new TranslatableComponent("gui.tempad.create_location"), color, darkerColor);
        addLocation.setSize(scale * 7, 12);
        addLocation.setOnClick(() -> {
            String nameFieldText = nameField.getText();
            if (nameFieldText != null) {
                FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                buf.writeInt(nameFieldText.length());
                buf.writeCharSequence(nameFieldText, StandardCharsets.UTF_8);
                buf.writeEnum(hand);
                ClientPlayNetworking.send(Tempad.LOCATION_PACKET, buf);
            }
            Minecraft.getInstance().setScreen(null);
            //root.remove(addLocation);
            //Minecraft.getInstance().setScreen(new TempadInterfaceGui(new RunProgramScreenDesc(false, null, hand, player, color)));
        });
        root.add(addLocation, scale * 5 + 8, scale * 12 + 9);
    }

    public static void pointManager(WPlainPanel root, LocationData tempadLocation, int color, int darkerColor, InteractionHand hand) {
        HighlightedTextButton teleportButton = new HighlightedTextButton(new TextComponent("Teleport"), color, darkerColor);
        teleportButton.setOnClick(() -> {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeResourceLocation(tempadLocation.getLevelKey().location());
            buf.writeBlockPos(tempadLocation.getBlockPos());
            Minecraft.getInstance().setScreen(null);
            ClientPlayNetworking.send(Tempad.TIMEDOOR_PACKET, buf);
        });
        teleportButton.setSize(16 * 5, 16);
        root.add(teleportButton, 16 * 6, 16 * 11);

        HighlightedTextButton deleteButton = new HighlightedTextButton(new TextComponent("Delete"), color, darkerColor);
        deleteButton.setOnClick(() -> {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeEnum(hand);
            buf.writeUUID(tempadLocation.getId());
            Minecraft.getInstance().setScreen(null);
            ClientPlayNetworking.send(Tempad.DELETE_LOCATION_PACKET, buf);
        });
        deleteButton.setSize(16 * 5, 16);
        root.add(deleteButton, 16 * 6, 16 * 12);

    }
}
