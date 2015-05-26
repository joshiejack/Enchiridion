package joshie.enchiridion.wiki.elements;

import java.util.List;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.designer.editor.EditorColorable;
import joshie.enchiridion.designer.editor.EditorColorable.IColorable;

import com.google.gson.annotations.Expose;

public class ElementBox extends Element implements IColorable {
    @Expose
    private int color = 0xFFFFFFFF;

    @Override
    public ElementBox setToDefault() {
        this.width = 100;
        this.height = 100;
        return this;
    }

    @Override
    public void display() {
        EnchiridionAPI.draw.drawRect(left, top, right, bottom, color);
    }

    @Override
    public void updateWidth(int change) {
        width += change;
        if (width <= 1) {
            width = 1;
        }
    }

    @Override
    public void updateHeight(int change) {
        height += change;
        if (height <= 1) {
            height = 1;
        }
    }

    @Override
    public void addEditButtons(List list) {

    }

    @Override
    public void onSelected(int x, int y) {
        EditorColorable.instance.select(this);
        //GuiColorEdit.select(this);
    }

    @Override
    public void onDeselected() {
        markDirty();
    }

    @Override
    public void setColor(int hex) {
        color = hex;
        markDirty();
    }

    @Override
    public int getColor() {
        return color;
    }
}
