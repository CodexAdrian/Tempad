/*
package me.codexadrian.tempad.client.widgets.libguilegacy;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ColorableTextField extends WTextField {
    public static final int OFFSET_X_TEXT = 4;
    //public static final int OFFSET_Y_TEXT = 6;

    @Environment(EnvType.CLIENT)
    private Font font;

    private String text = "";
    private int maxLength = 16;
    private boolean editable = true;
    private int suggestionColor = 0xFF_A0A0A0;
    private int focusedColor = 0xFF_FFFFA0;
    private int unfocusedColor = 0xFF_A0A0A0;
    private int boxColor = 0xFF333333;
    private int enabledColor = 0xE0E0E0;
    private int uneditableColor = 0x707070;

    @Nullable
    private Component suggestion = null;

    private int cursor = 0;
    */
/**
     * If not -1, select is the "anchor point" of a selection. That is, if you hit shift+left with
     * no existing selection, the selection will be anchored to where you were, but the cursor will
     * move left, expanding the selection as you continue to move left. If you move to the right,
     * eventually you'll overtake the anchor, drop the anchor at the same place and start expanding
     * the selection rightwards instead.
     *//*

    private int select = -1;

    private Consumer<String> onChanged;

    private Predicate<String> textPredicate;

    @Environment(EnvType.CLIENT)
    @Nullable
    private BackgroundPainter backgroundPainter;

    public ColorableTextField() {
    }

    public ColorableTextField(@Nullable Component suggestion) {
        this.suggestion = suggestion;
    }

    public void setText(String s) {
        if (this.textPredicate==null || this.textPredicate.test(s)) {
            this.text = (s.length()>maxLength) ? s.substring(0,maxLength) : s;
            if (onChanged!=null) onChanged.accept(this.text);
        }
    }

    public String getText() {
        return this.text;
    }

    @Override
    public boolean canResize() {
        return true;
    }

    @Override
    public void setSize(int x, int y) {
        super.setSize(x, 20);
    }

    public void setCursorPos(int location) {
        this.cursor = Mth.clamp(location, 0, this.text.length());
    }

    public int getMaxLength() {
        return this.maxLength;
    }

    public int getCursor() {
        return this.cursor;
    }

    @Nullable
    public String getSelection() {
        if (select<0) return null;
        if (select==cursor) return null;

        //Tidy some things
        if (select>text.length()) select = text.length();
        if (cursor<0) cursor = 0;
        if (cursor>text.length()) cursor = text.length();

        int start = Math.min(select, cursor);
        int end = Math.max(select, cursor);

        return text.substring(start, end);
    }

    public boolean isEditable() {
        return this.editable;
    }

    @Environment(EnvType.CLIENT)
    protected void renderTextField(PoseStack matrices, int x, int y) {
        if (this.font==null) this.font = Minecraft.getInstance().font;
        
        int borderColor = (this.isFocused()) ? focusedColor : unfocusedColor;
        ScreenDrawing.coloredRect(matrices, x-1, y-1, width+2, height+2, borderColor);
        ScreenDrawing.coloredRect(matrices, x, y, width, height, boxColor);


        int textColor = this.editable ? this.enabledColor : this.uneditableColor;

        //TODO: Scroll offset
        String trimText = font.plainSubstrByWidth(this.text, this.width-OFFSET_X_TEXT);

        boolean selection = (select!=-1);
        boolean focused = this.isFocused(); //this.isFocused() && this.focusedTicks / 6 % 2 == 0 && boolean_1; //Blinks the cursor

        //int textWidth = font.getStringWidth(trimText);
        //int textAnchor = (font.isRightToLeft()) ?
        //		x + OFFSET_X_TEXT + textWidth :
        //		x + OFFSET_X_TEXT;

        int textX = x + OFFSET_X_TEXT;
        //(font.isRightToLeft()) ?
        //textAnchor - textWidth :
        //textAnchor;

        int textY = y + (height - 8) / 2;

        //TODO: Adjust by scroll offset
        int adjustedCursor = this.cursor;
        if (adjustedCursor > trimText.length()) {
            adjustedCursor = trimText.length();
        }

        int preCursorAdvance = textX;
        if (!trimText.isEmpty()) {
            String string_2 = trimText.substring(0,adjustedCursor);
            preCursorAdvance = font.drawShadow(matrices, string_2, textX, textY, textColor);
        }

        if (adjustedCursor<trimText.length()) {
            font.drawShadow(matrices, trimText.substring(adjustedCursor), preCursorAdvance-1, (float)textY, textColor);
        }


        if (text.length()==0 && this.suggestion != null) {
            font.drawShadow(matrices, this.suggestion, textX, textY, suggestionColor);
        }

        //int var10002;
        //int var10003;
        if (focused && !selection) {
            if (adjustedCursor<trimText.length()) {
                //int caretLoc = ColorableTextField.getCaretOffset(text, cursor);
                //if (caretLoc<0) {
                //	caretLoc = textX+MinecraftClient.getInstance().textRenderer.getStringWidth(trimText)-caretLoc;
                //} else {
                //	caretLoc = textX+caretLoc-1;
                //}
                ScreenDrawing.coloredRect(matrices, preCursorAdvance-1, textY-2, 1, 12, 0xFFD0D0D0);
                //if (boolean_3) {
                //	int var10001 = int_7 - 1;
                //	var10002 = int_9 + 1;
                //	var10003 = int_7 + 1;
                //
                //	DrawableHelper.fill(int_9, var10001, var10002, var10003 + 9, -3092272);

            } else {
                font.drawShadow(matrices, "_", preCursorAdvance, textY, textColor);
            }
        }

        if (selection) {
            int a = ColorableTextField.getCaretOffset(text, cursor);
            int b = ColorableTextField.getCaretOffset(text, select);
            if (b<a) {
                int tmp = b;
                b = a;
                a = tmp;
            }
            invertedRect(matrices, textX+a-1, textY-1, Math.min(b-a, width - OFFSET_X_TEXT), 12);
        }
    }

    @Environment(EnvType.CLIENT)
    private void invertedRect(PoseStack matrices, int x, int y, int width, int height) {
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        Matrix4f model = matrices.last().pose();
        RenderSystem.setShaderColor(0.0F, 0.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.disableTexture();
        RenderSystem.enableColorLogicOp();
        RenderSystem.logicOp(GlStateManager.LogicOp.INVERT);
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
        buffer.vertex(model, x,       y+height, 0).endVertex();
        buffer.vertex(model, x+width, y+height, 0).endVertex();
        buffer.vertex(model, x+width, y,        0).endVertex();
        buffer.vertex(model, x,       y,        0).endVertex();
        buffer.end();
        BufferUploader.end(buffer);
        RenderSystem.disableColorLogicOp();
        RenderSystem.enableTexture();
    }

    public ColorableTextField setTextPredicate(Predicate<String> predicate_1) {
        this.textPredicate = predicate_1;
        return this;
    }

    public ColorableTextField setChangedListener(Consumer<String> listener) {
        this.onChanged = listener;
        return this;
    }

    public ColorableTextField setMaxLength(int max) {
        this.maxLength = max;
        if (this.text.length() > max) {
            this.text = this.text.substring(0, max);
            this.onChanged.accept(this.text);
        }
        return this;
    }

    public ColorableTextField setEnabledColor(int col) {
        this.enabledColor = col;
        return this;
    }

    public ColorableTextField setDisabledColor(int col) {
        this.uneditableColor = col;
        return this;
    }
    
    public ColorableTextField setBoxFocusColor(int color) {
        this.focusedColor = color;
        return this;
    }

    public ColorableTextField setBoxUnfocusedColor(int color) {
        this.unfocusedColor = color;
        return this;
    }
    
    public ColorableTextField setBoxColor(int color) {
        this.boxColor = color;
        return this;
    }

    public ColorableTextField setSuggestionColor(int color) {
        this.suggestionColor = color;
        return this;
    }

    public ColorableTextField setEditable(boolean editable) {
        this.editable = editable;
        return this;
    }

    @Nullable
    public Component getSuggestion() {
        return suggestion;
    }

    public ColorableTextField setSuggestion(@Nullable String suggestion) {
        this.suggestion = suggestion != null ? new TextComponent(suggestion) : null;
        return this;
    }

    public ColorableTextField setSuggestion(@Nullable Component suggestion) {
        this.suggestion = suggestion;
        return this;
    }

    @Environment(EnvType.CLIENT)
    public ColorableTextField setBackgroundPainter(BackgroundPainter painter) {
        this.backgroundPainter = painter;
        return this;
    }

    public boolean canFocus() {
        return true;
    }

    @Override
    public void onFocusGained() {
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void paint(PoseStack matrices, int x, int y, int mouseX, int mouseY) {
        renderTextField(matrices, x, y);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public InputResult onClick(int x, int y, int button) {
        requestFocus();
        cursor = getCaretPos(this.text, x-OFFSET_X_TEXT);
        return InputResult.PROCESSED;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void onCharTyped(char ch) {
        if (this.text.length()<this.maxLength) {
            //snap cursor into bounds if it went astray
            if (cursor<0) cursor=0;
            if (cursor>this.text.length()) cursor = this.text.length();

            String before = this.text.substring(0, cursor);
            String after = this.text.substring(cursor, this.text.length());
            this.text = before+ch+after;
            cursor++;
            if (onChanged != null) onChanged.accept(text);
        }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void onKeyPressed(int ch, int key, int modifiers) {
        if (!this.editable) return;

        if (Screen.isCopy(ch)) {
            String selection = getSelection();
            if (selection!=null) {
                Minecraft.getInstance().keyboardHandler.setClipboard(selection);
            }

            return;
        } else if (Screen.isPaste(ch)) {
            if (select!=-1) {
                int a = select;
                int b = cursor;
                if (b<a) {
                    int tmp = b;
                    b = a;
                    a = tmp;
                }
                String before = this.text.substring(0, a);
                String after = this.text.substring(b);

                String clip = Minecraft.getInstance().keyboardHandler.getClipboard();
                text = before+clip+after;
                select = -1;
                cursor = (before+clip).length();
            } else {
                String before = this.text.substring(0, cursor);
                String after = this.text.substring(cursor, this.text.length());

                String clip = Minecraft.getInstance().keyboardHandler.getClipboard();
                text = before + clip + after;
                cursor += clip.length();
                if (text.length()>this.maxLength) {
                    text = text.substring(0, maxLength);
                    if (cursor>text.length()) cursor = text.length();
                }
            }

            if (onChanged != null) onChanged.accept(text);
            return;
        } else if (Screen.isSelectAll(ch)) {
            select = 0;
            cursor = text.length();
            return;
        }

        //System.out.println("Ch: "+ch+", Key: "+key+", Mod: "+modifiers);

        if (modifiers==0) {
            if (ch==GLFW.GLFW_KEY_DELETE || ch==GLFW.GLFW_KEY_BACKSPACE) {
                if (text.length()>0 && cursor>0) {
                    if (select>=0 && select!=cursor) {
                        int a = select;
                        int b = cursor;
                        if (b<a) {
                            int tmp = b;
                            b = a;
                            a = tmp;
                        }
                        String before = this.text.substring(0, a);
                        String after = this.text.substring(b);
                        text = before+after;
                        if (cursor==b) cursor = a;
                        select = -1;
                    } else {
                        String before = this.text.substring(0, cursor);
                        String after = this.text.substring(cursor, this.text.length());

                        before = before.substring(0,before.length()-1);
                        text = before+after;
                        cursor--;
                    }

                    if (onChanged != null) onChanged.accept(text);
                }
            } else if (ch== GLFW.GLFW_KEY_LEFT) {
                if (select!=-1) {
                    cursor = Math.min(cursor, select);
                    select = -1; //Clear the selection anchor
                } else {
                    if (cursor>0) cursor--;
                }
            } else if (ch==GLFW.GLFW_KEY_RIGHT) {
                if (select!=-1) {
                    cursor = Math.max(cursor, select);
                    select = -1; //Clear the selection anchor
                } else {
                    if (cursor<text.length()) cursor++;
                }
            } else {
                //System.out.println("Ch: "+ch+", Key: "+key);
            }
        } else {
            if (modifiers==GLFW.GLFW_MOD_SHIFT) {
                if (ch==GLFW.GLFW_KEY_LEFT) {
                    if (select==-1) select = cursor;
                    if (cursor>0) cursor--;
                    if (select==cursor) select = -1;
                } else if (ch==GLFW.GLFW_KEY_RIGHT) {
                    if (select==-1) select = cursor;
                    if (cursor<text.length()) cursor++;
                    if (select==cursor) select = -1;
                }
            }
        }
    }

    */
/**
     * From an X offset past the left edge of a TextRenderer.draw, finds out what the closest caret
     * position (division between letters) is.
     * @param s
     * @param x
     * @return
     *//*

    @Environment(EnvType.CLIENT)
    public static int getCaretPos(String s, int x) {
        if (x<=0) return 0;

        Font font = Minecraft.getInstance().font;
        int lastAdvance = 0;
        for(int i=0; i<s.length()-1; i++) {
            int advance = font.width(s.substring(0,i+1));
            int charAdvance = advance-lastAdvance;
            if (x<advance + (charAdvance/2)) return i+1;

            lastAdvance = advance;
        }

        return s.length();
    }

    */
/**
     * From a caret position, finds out what the x-offset to draw the caret is.
     * @param s
     * @param pos
     * @return
     *//*

    @Environment(EnvType.CLIENT)
    public static int getCaretOffset(String s, int pos) {
        if (pos==0) return 0;//-1;

        Font font = Minecraft.getInstance().font;
        int ofs = font.width(s.substring(0, pos))+1;
        return ofs; //(font.isRightToLeft()) ? -ofs : ofs;
    }

}
*/
