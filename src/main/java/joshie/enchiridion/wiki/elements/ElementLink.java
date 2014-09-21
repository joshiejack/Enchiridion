package joshie.enchiridion.wiki.elements;

import java.awt.Cursor;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.google.gson.annotations.Expose;

public class ElementLink extends Element {
    @Expose
    private int color = 0xFFFFFFFF;
    String path = "";
    @Expose
    String mod;
    @Expose
    String tab;
    @Expose
    String cat;
    @Expose
    String page;
    
    @Override
    public ElementLink setToDefault() {
        this.width = 100;
        this.height = 20;
        this.path = "Enchiridion.Default.Basics.Menu";
        this.mod = "Enchiridion";
        this.tab = "Default";
        this.cat = "Basics";
        this.page = "Menu";
        return this;
    }

    @Override
    public void display(boolean isEditMode) {
        if (isEditMode) {
            GL11.glPushMatrix();
            GL11.glScalef(size, size, size);            
            wiki.mc.fontRenderer.drawSplitString(path, getX(), getY(), width, color);
            GL11.glPopMatrix();
        }
    }

    //Edit mode return doesn't matter
    @Override
    public boolean releaseButton(int x, int y, int button, boolean isEditMode) {
        if (!isEditMode && button == 0) {
            if (isMouseOver(x, y)) {
                if (mod != null && tab != null && cat != null && page != null) {
                    wiki.setPage(mod, tab, cat, page);
                }
            }
        }

        return super.releaseButton(x, y, button, isEditMode);
    }

    @Override
    public void keyTyped(char character, int key) {
        if (isSelected) {
            if (character == 22) {
                add(GuiScreen.getClipboardString());
            } else if (key == 14) {
                delete();
            } else if (key == 28 || key == 156) {
                add("\n");
            } else if (ChatAllowedCharacters.isAllowedCharacter(character)) {
                add(Character.toString(character));
            }
        }
    }

    public void add(String string) {
        path += string;
    }

    private void delete() {
        if (path != null && path.length() > 0) {
            path = path.substring(0, path.length() - 1);
        }
    }

    @Override
    public void addEditButtons(List list) {
        return;
    }

    @Override
    public void onSelected() {
        return;
    }

    @Override
    public void onDeselected() {
        String[] arr = path.split("\\.");
        if (arr.length == 4) {
            mod = arr[0];
            tab = arr[1];
            cat = arr[2];
            page = arr[3];
        }
    }
}
