package joshie.enchiridion.wiki.elements;

import java.util.List;

import joshie.enchiridion.EClientProxy;
import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.mode.ButtonBase;
import joshie.enchiridion.wiki.mode.edit.ButtonWikiTextEdit;
import joshie.enchiridion.wiki.mode.edit.ButtonWikiTextEffects;
import joshie.enchiridion.wiki.mode.edit.ButtonWikiTextSize;
import joshie.lib.util.Text;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.google.gson.annotations.Expose;

public class ElementText extends Element {

    @Expose
    private String text = "";
    private boolean init;
    private int position;
    private boolean white;
    private int tick;
    private String blink;

    @Override
    public ElementText setToDefault() {
        this.text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
        this.width = 200;
        this.height = 100;
        return this;
    }

    @Override
    public void display(boolean isEditMode) {
        if (!init) {
            init = ((text = text.replace("Â§", "\u00a7").replace("§", "\u00a7")) != null);
            position = text.length();
        }
        
        GL11.glPushMatrix();
        GL11.glScalef(size, size, size);
        if (isEditMode && isSelected) {
            try {
                tick++;
                if(blink == null) blink = "F";
                if(tick % 60 == 0) {
                    if(white) {
                        white = false;
                        blink = "666666";
                    } else {
                        white = true;
                        blink = "FFFFFF";
                    }
                }
                
                String space = "";
                if(position == text.length()) {
                    space = " ...";
                }
                                
                if(white) {
                    EClientProxy.font.drawSplitString(new StringBuilder(text).insert(position, "[*cursor*]" + space).toString(), (int) ((WikiHelper.theLeft + BASE_X + left) / size), ((int) ((WikiHelper.theTop + BASE_Y + top) / size)), (int)((width * 2) / size) + 4, 0xFFFFFFFF);
                } else {
                    EClientProxy.font.drawSplitString(new StringBuilder(text).insert(position, "[*/cursor*]" + space).toString(), (int) ((WikiHelper.theLeft + BASE_X + left) / size), ((int) ((WikiHelper.theTop + BASE_Y + top) / size)), (int)((width * 2) / size) + 4, 0xFFFFFFFF);
                }
            } catch (Exception e) {}
        } else {
            EClientProxy.font.drawSplitString(text, (int) ((WikiHelper.theLeft + BASE_X + left) / size), ((int) ((WikiHelper.theTop + BASE_Y + top) / size)), (int)((width * 2) / size) + 4, 0xFFFFFFFF);
        }

        GL11.glPopMatrix();
    }

    private void cursorLeft(int count) {
        int left = position - count;
        if (left < 0) {
            position = 0;
        } else position = left;
    }

    private void cursorRight(int count) {
        int right = position + count;
        if (right > text.length()) {
            position = text.length();
        } else position = right;
    }

    public void add(String string) {
        StringBuilder builder = new StringBuilder(text);
        text = builder.insert(position, string).toString();
        cursorRight(string.length());
    }

    private void delete(int count) {
        if ((count < 0 && position > 0) || (count >= 0 && position < text.length())) {
            StringBuilder builder = new StringBuilder(text);
            text = builder.deleteCharAt(position + count).toString();
            if (count < 0) cursorLeft(-count);
            else if (count >= 0) cursorRight(count);
        }
    }

    @Override
    public void keyTyped(char character, int key) {
        if (isSelected) {
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
    public void addEditButtons(List list) {
        int yCoord = 50;
        list.add(new ButtonWikiTextEffects(wiki, wiki.button_id++, 75, yCoord, Text.END, "disable"));
        list.add(new ButtonWikiTextEffects(wiki, wiki.button_id++, 75, yCoord += 50, Text.BOLD, "bold"));
        list.add(new ButtonWikiTextEffects(wiki, wiki.button_id++, 75, yCoord += 50, Text.ITALIC, "italics"));
        list.add(new ButtonWikiTextEffects(wiki, wiki.button_id++, 75, yCoord += 50, Text.UNDERLINE, "underline"));
        list.add(new ButtonWikiTextEffects(wiki, wiki.button_id++, 75, yCoord += 50, Text.STRIKETHROUGH, "strikethrough"));
        list.add(new ButtonWikiTextEffects(wiki, wiki.button_id++, 75, yCoord += 50, Text.OBFUSCATED, "obfuscated"));
        list.add(new ButtonWikiTextSize(wiki, wiki.button_id++, 75, yCoord += 50, 0.1F, "larger"));
        list.add(new ButtonWikiTextSize(wiki, wiki.button_id++, 75, yCoord += 50, -0.1F, "smaller"));
    }

    @Override
    public void onSelected() {
        for (ButtonBase button : wiki.buttons) {
            if (button instanceof ButtonWikiTextEdit) {
                button.visible = true;
            }
        }
    }

    @Override
    public void onDeselected() {
        for (ButtonBase button : wiki.buttons) {
            if (button instanceof ButtonWikiTextEdit) {
                button.visible = false;
            }
        }
    }
}
