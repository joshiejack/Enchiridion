package joshie.enchiridion.designer.features;

import joshie.enchiridion.designer.DesignerHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;

import com.google.gson.annotations.Expose;

public class FeatureText extends Feature {
    @Expose
    private String text = "Lorem ipsum";
    @Expose
    private int color = 4210752;
    @Expose
    private int wrap = 500;

    private int position;
    private int tick;
    private String blink;
    private boolean white;

    @Override
    public void drawFeature() {
        DesignerHelper.drawSplitString(getText(), left, top, wrap, color);
    }
    
    @Override
    public void setSelected() {
        super.setSelected();
        position = text.length();
    }
    
    public String getText() {
        if (isSelected) {
            tick++;
            if (tick % 60 == 0) {
                if (white) {
                    white = false;
                } else {
                    white = true;
                }
            }

            if (white) {
                return new StringBuilder(text).insert(Math.min(position, text.length()), "[*cursor*]").toString();
            } else {
                return new StringBuilder(text).insert(Math.min(position, text.length()), "[*/cursor*]").toString();
            }
        } else return text;
    }

    private void cursorLeft(int count) {
        int left = position - count;
        if (left < 0) {
            position = 0;
        } else position = left;
    }

    private void cursorRight(int count) {
        String text = this.text;
        int right = position + count;
        if (right > text.length()) {
            position = text.length();
        } else position = right;
    }

    private void add(String string) {
        String text = this.text;
        StringBuilder builder = new StringBuilder(text);
        text = builder.insert(position, string).toString();
        this.text = text;
        cursorRight(string.length());
    }

    private void delete(int count) {
        String text = this.text;
        if ((count < 0 && position > 0) || (count >= 0 && position < text.length())) {
            StringBuilder builder = new StringBuilder(text);
            text = builder.deleteCharAt(position + count).toString();
            this.text = text;
            if (count < 0) cursorLeft(-count);
            else if (count >= 0) cursorRight(count);
        }
    }

    @Override
    public void keyTyped(char character, int key) {
        if (text != null && isSelected) {
            if (key == 203) {
                cursorLeft(1);
            } else if (key == 205) {
                cursorRight(1);
            } else if (character == 22) {
                add(GuiScreen.getClipboardString());
            } else if (key == 14) {
                delete(-1);
            } else if (key == 211) {
                delete(0);
            } else if (key == 28 || key == 156) {
                add("\n");
            } else if (ChatAllowedCharacters.isAllowedCharacter(character)) {
                add(Character.toString(character));
            }
        }
    }

    @Override
    public void loadEditor() {
        // DRAW TEXT+++, TEXT---, BBCODE MODE

    }
}
