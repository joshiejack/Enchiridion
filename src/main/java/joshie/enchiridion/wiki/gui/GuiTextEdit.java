package joshie.enchiridion.wiki.gui;

import joshie.enchiridion.api.IGuiDisablesMenu;
import joshie.enchiridion.api.ITextEditable;
import joshie.enchiridion.wiki.WikiHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;

public class GuiTextEdit extends GuiExtension {
    private static ITextEditable editable = null;
    private static int position;
    private static int tick;
    private static boolean white;
    private static String blink;
    
    public static void clear() {
        GuiTextEdit.editable = null;
        GuiTextEdit.position = 0;
        GuiMenu.isEditing = false;
    }
    
    public static boolean isSelected(ITextEditable editable) {
        return editable == GuiTextEdit.editable;
    }
    
    public static void select(ITextEditable editable) {
        select(editable, editable.getText().length());
    }

    public static void select(ITextEditable editable, int position) {
        if(!(editable instanceof IGuiDisablesMenu)) {
            WikiHelper.clearEditGUIs();
            GuiMenu.isEditing = true;
        }
        
        GuiTextEdit.editable = editable;
        GuiTextEdit.position = position;
    }

    @Override
    public void keyTyped(char character, int key) {        
        if (editable != null) {
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

    private void cursorLeft(int count) {
        int left = position - count;
        if (left < 0) {
            position = 0;
        } else position = left;
    }

    private void cursorRight(int count) {
        String text = editable.getText();
        int right = position + count;
        if (right > text.length()) {
            position = text.length();
        } else position = right;
    }

    private void add(String string) {
        String text = editable.getText();
        StringBuilder builder = new StringBuilder(text);
        text = builder.insert(position, string).toString();
        editable.setText(text);
        cursorRight(string.length());
    }

    private void delete(int count) {
        String text = editable.getText();
        if ((count < 0 && position > 0) || (count >= 0 && position < text.length())) {
            StringBuilder builder = new StringBuilder(text);
            text = builder.deleteCharAt(position + count).toString();
            editable.setText(text);
            if (count < 0) cursorLeft(-count);
            else if (count >= 0) cursorRight(count);
        }
    }
    
    public static String getText(ITextEditable editable) {
        return getText(editable, editable.getText());
    }

    public static String getText(ITextEditable editable, String text, Object...objects) {
        if (GuiTextEdit.editable == editable && editable.canEdit(objects)) {
            tick++;
            if (blink == null) blink = "F";
            if (tick % 60 == 0) {
                if (white) {
                    white = false;
                    blink = "666666";
                } else {
                    white = true;
                    blink = "FFFFFF";
                }
            }

            if (white) {
                return new StringBuilder(text).insert(Math.min(position, text.length()), "[*cursor*]").toString();
            } else {
                return new StringBuilder(text).insert(Math.min(position, text.length()), "[*/cursor*]").toString();
            }
        } else return text;
    }
}
