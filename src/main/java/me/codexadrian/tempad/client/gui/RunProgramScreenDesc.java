package me.codexadrian.tempad.client.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.cottonmc.cotton.gui.widget.WPanel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import io.netty.buffer.Unpooled;
import me.codexadrian.tempad.Tempad;
import me.codexadrian.tempad.TempadLocation;
import me.codexadrian.tempad.client.widgets.ColorableTextField;
import me.codexadrian.tempad.client.widgets.HighlightedTextButton;
import me.codexadrian.tempad.client.widgets.ScalableText;
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

import java.awt.*;
import java.nio.charset.StandardCharsets;

import static me.codexadrian.tempad.Tempad.*;

public class RunProgramScreenDesc extends TempadGUIDescription {
    boolean locationBuilder;

    public RunProgramScreenDesc(boolean locationBuilder, @Nullable TempadLocation location, InteractionHand hand, Player player, int color) {
        super(color);
        ItemStack stack = player.getItemInHand(hand);
        this.locationBuilder = locationBuilder;
        int scale = 16;
        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        root.setSize(480, 256);
        this.addPainters();
        int darkerColor = blend(Color.getColor("tempad_bg", color), Color.black).getRGB();
        int evenDarkerColor = blend(Color.getColor("tempad_bg", darkerColor), Color.black).getRGB();
        if(stack.hasTag()) {
            WListPanel<TempadLocation, HighlightedTextButton> positionList = new WListPanel<>(TempadLocation.getLocationsFromStack(stack), HighlightedTextButton::new, (tempadLocation, tempadButton) -> {
                tempadButton.setTextComponent(new TextComponent(tempadLocation.name()));
                tempadButton.setColor(color, darkerColor);
                tempadButton.setSize(scale * 10, 12);
                tempadButton.setVerticalPadding(.5F);
                tempadButton.setOnClick(() -> {
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    buf.writeResourceLocation(tempadLocation.key().location());
                    buf.writeBlockPos(tempadLocation.position());
                    Minecraft.getInstance().setScreen(null);
                    ClientPlayNetworking.send(Tempad.TIMEDOOR_PACKET, buf);
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
        newPosition.setOnClick(() -> Minecraft.getInstance().setScreen(new TempadInterfaceGui(new RunProgramScreenDesc(!locationBuilder, !locationBuilder ? new TempadLocation(null, null, player.blockPosition()) : null,  hand, player, color))));
        root.add(newPosition, 17 * scale, 12, 12 * scale, scale);

        if (locationBuilder) {
            root.add(nameField, (int)(scale * 5.5), scale * 11, scale * 6, scale);
            HighlightedTextButton addLocation = new HighlightedTextButton(new TranslatableComponent("gui.tempad.create_location"), color, darkerColor);
            addLocation.setSize(scale * 7, 12);
            addLocation.setOnClick(() -> {
                String nameFieldText = nameField.getText();
                if(nameFieldText != null) {
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    buf.writeInt(nameFieldText.length());
                    buf.writeCharSequence(nameFieldText, StandardCharsets.UTF_8);
                    buf.writeEnum(hand);
                    ClientPlayNetworking.send(Tempad.LOCATION_PACKET, buf);
                }

                //root.remove(addLocation);
                Minecraft.getInstance().setScreen(new TempadInterfaceGui(new RunProgramScreenDesc(false, null, hand, player, color)));
            });
            root.add(addLocation, scale * 5 + 8, scale * 12 + 9);
        } else {

        }

        if(location != null) {
            ScalableText onScreenLocation = new ScalableText(new TextComponent("(" + location.position().toShortString() + ")"), color);
            root.add(onScreenLocation, scale * 5, scale, scale * 7, scale);
        }

        root.validate(this);
    }

    @Override
    public void addPainters() {
        WPanel root = getRootPanel();
        drawUnifiedBackground(root, color);
    }
}
