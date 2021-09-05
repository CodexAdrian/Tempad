package me.codexadrian.tempad.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WPanel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WSlider;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.data.Color;
import me.codexadrian.tempad.TempadClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.MultiBufferSource;

import static me.codexadrian.tempad.Tempad.drawUnifiedBackground;

public class OptionsScreenDesc extends TempadGUIDescription {
    public int color;
    private int hue = 8;
    private int saturation = 100;
    private int lum = 50;

    public OptionsScreenDesc(int passedColor) {
        super(passedColor);
        int scale = 16;
        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        root.setSize(480, 256);
        this.addPainters();
        WSlider hue = new WSlider(0, 100, Axis.VERTICAL);
        //root.add(hue, 0, 2 * scale, scale, 3 * scale);

        WColorBox col = new WColorBox();
        //root.add(col, 3 * scale,2 * scale, scale, 3 * scale);
        hue.setValue(8);
        hue.setValueChangeListener((i)->{
            this.hue = i;
            updateCol(col);
            System.out.println("h: "+this.hue +" s: "+this.saturation + " l: "+this.lum);
            System.out.println("col is now "+Integer.toHexString(col.boxColor));
            this.addPainters();
        });
        WSlider saturation = new WSlider(0, 100, Axis.VERTICAL);
        //root.add(saturation, scale, 2 * scale, scale, 3 * scale);
        saturation.setValue(100);
        saturation.setValueChangeListener((i)->{
            this.saturation = i;
            updateCol(col);
            this.addPainters();
        });
        WSlider lum = new WSlider(20, 100, Axis.VERTICAL);
        //root.add(lum, 2 * scale, 2 * scale, scale, 3 * scale);
        lum.setValue(50);
        lum.setValueChangeListener((i)->{
            this.lum = i;
            updateCol(col);
            this.addPainters();
        });

        ColorWheel wheel = new ColorWheel();
        root.add(wheel, scale * 10, scale, scale * 3, scale * 3);
        wheel.setDimensions(scale * 3, scale * 3);

        updateCol(col);
        addPainters();
    }
    private void updateCol(WColorBox col) {
        Color.HSL hsl = new Color.HSL(hue /100f, saturation /100f, lum /100f);
        col.setColor(hsl.toRgb());
        this.color = hsl.toRgb();
    }

    public void addPainters() {
        WPanel root = getRootPanel();
        drawUnifiedBackground(root, this.color);
    }

    public static class WColorBox extends WWidget {
        protected int boxColor = 0xFF_FFFFFF;
        public WColorBox() {}

        public void setColor(int col) {
            this.boxColor = col;
        }

        @Override
        public void paint(PoseStack matrices, int x, int y, int mouseX, int mouseY) {
            ScreenDrawing.coloredRect(matrices, x, y, this.getWidth(), this.getHeight(), this.boxColor);
        }
    }

    public static class ColorWheel extends WWidget {
        int width, height;
        @Override
        public void paint(PoseStack matrices, int x, int y, int mouseX, int mouseY) {
            matrices.pushPose();
            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            VertexConsumer buffer1 = bufferSource.getBuffer(TempadClient.colorWheelRendertype);
            matrices.scale(getWidth(), getHeight(), 1);
            matrices.translate(x, y, -1);
            Matrix4f model = matrices.last().pose();
            buffer1.vertex(model, 1, 1, 0).endVertex();
            buffer1.vertex(model, 0, 1, 0).endVertex();
            buffer1.vertex(model, 0, 0, 0).endVertex();
            buffer1.vertex(model, 1, 0, 0).endVertex();
            VertexConsumer buffer2 = bufferSource.getBuffer(TempadClient.colorTriangleRendertype);
            buffer2.vertex(model, 0.5F, 1, 0).endVertex();
            buffer2.vertex(model, 0, 0, 0).endVertex();
            buffer2.vertex(model, 0, 1, 0).endVertex();
            matrices.popPose();
            bufferSource.endLastBatch();
        }

        public void setDimensions(int width, int height) {
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
    }
}
