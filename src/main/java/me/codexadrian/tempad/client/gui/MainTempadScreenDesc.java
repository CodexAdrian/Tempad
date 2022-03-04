package me.codexadrian.tempad.client.gui;

import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import me.codexadrian.tempad.client.api.gui.ColorSelectScreen;
import me.codexadrian.tempad.client.widgets.libguilegacy.HighlightedTextButton;
import me.codexadrian.tempad.client.widgets.libguilegacy.ScalableText;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

import java.awt.*;

import static me.codexadrian.tempad.Tempad.*;

public class MainTempadScreenDesc extends TempadGUIDescription {
    public MainTempadScreenDesc(int color, Player player, InteractionHand hand) {
        super(color, player, hand);
        int scale = 16;
        WPlainPanel root = new WPlainPanel();
        this.setRootPanel(root);
        root.setSize(480, 256);
        this.addPainters();

        int leftAlign = scale * 17 + 2;

        ScalableText actionList = new ScalableText(new TranslatableComponent("gui.tempad.header_line_1"), color);
        actionList.setSize(5 * scale, 18);
        root.add(actionList, leftAlign, 5 * scale + 3);

        ScalableText selectProgram = new ScalableText(new TranslatableComponent("gui.tempad.header_line_2"), color);
        selectProgram.setSize(5 * scale, 18);
        root.add(selectProgram, leftAlign, 7 * scale - 5);

        HighlightedTextButton options = new HighlightedTextButton(new TranslatableComponent("gui.tempad.options"), color, blend(Color.getColor("tempad_bg", color), Color.black).getRGB());
        options.setSize(5 * scale, 12);
        options.setOnClick(() -> Minecraft.getInstance().setScreen(new TempadInterfaceGui(new ColorSelectScreenDesc(color, player, hand))));
        root.add(options, leftAlign, 8 * scale + 3);

        HighlightedTextButton runProgram = new HighlightedTextButton(new TranslatableComponent("gui.tempad.run_program"), color, blend(Color.getColor("tempad_bg", color), Color.black).getRGB());
        runProgram.setSize(7 * scale, 12);
        runProgram.setOnClick(() -> Minecraft.getInstance().setScreen(new TempadInterfaceGui(new RunProgramScreenDesc(false,false,  null, hand, player,  color))));
        root.add(runProgram, leftAlign, 9 * scale + 3);

        HighlightedTextButton wiki = new HighlightedTextButton(new TranslatableComponent("gui.tempad.wiki"), color, blend(Color.getColor("tempad_bg", color), Color.black).getRGB());
        wiki.setSize(3 * scale, 12);
        root.add(wiki, leftAlign, 10 * scale + 3);

        WSprite TVAlogo = new WSprite(new ResourceLocation(MODID, "textures/widget/tva_logo.png"));
        TVAlogo.setTint(color);
        root.add(TVAlogo, scale/2, scale * 4, scale * 16, scale * 8);

        root.validate(this);
    }
}
