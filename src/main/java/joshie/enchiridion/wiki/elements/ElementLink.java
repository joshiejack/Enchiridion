package joshie.enchiridion.wiki.elements;

import java.util.List;

import joshie.enchiridion.EClientProxy;
import joshie.enchiridion.api.ITextEditable;
import joshie.enchiridion.wiki.gui.GuiTextEdit;

import org.lwjgl.opengl.GL11;

import com.google.gson.annotations.Expose;

public class ElementLink extends Element implements ITextEditable {
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
            EClientProxy.font.drawSplitString(GuiTextEdit.getText(this, path), getX(), getY(), width, color);
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
    public void addEditButtons(List list) {
        return;
    }

    @Override
    public void onSelected(int x, int y) {
        GuiTextEdit.select(this);
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
        
        markDirty();
    }

    @Override
    public void setText(String text) {
        if (isSelected) {
            this.path = text;
            markDirty();
        }
    }

    @Override
    public String getText() {
        return this.path;
    }

    @Override
    public boolean canEdit(Object... objects) {
        return isSelected;
    }
}
