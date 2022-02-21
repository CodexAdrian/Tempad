/*
package me.codexadrian.tempad.client.widgets.libguilegacy;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.Nullable;

public class ColorButton extends WWidget {
    public int buttonColor;
    public int borderColor = 0xFF_FFFFFF;
    public int width, height = 32;
    @Nullable
    private Runnable onClick;

    public ColorButton(int color) {
        this.buttonColor = color;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public InputResult onClick(int x, int y, int button) {
        super.onClick(x, y, button);

        if (x>=0 && y>=0 && x<this.width && y<this.height) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));

            if (onClick!=null) onClick.run();
            return InputResult.PROCESSED;
        }
        return InputResult.IGNORED;
    }
    @Override
    public void paint(PoseStack matrices, int x, int y, int mouseX, int mouseY) {
        matrices.pushPose();
        ScreenDrawing.coloredRect(matrices, x-1, y-1, width+2, height+2, borderColor);
        ScreenDrawing.coloredRect(matrices, x, y, width, height, buttonColor);
        matrices.popPose();
    }

    @Override
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void setOnClick(@Nullable Runnable onClick) {
        this.onClick = onClick;
    }
}

*/
