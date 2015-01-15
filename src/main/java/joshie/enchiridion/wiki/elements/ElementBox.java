package joshie.enchiridion.wiki.elements;

import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.google.gson.annotations.Expose;

import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.gui.GuiMain;

public class ElementBox extends Element {
    @Expose
    private int color = 0xFFFFFFFF;
    
    @Override
    public ElementBox setToDefault() {
        this.width = 100;
        this.height = 100;
        return this;
    }

    @Override
    public void display(boolean isEditMode) {
        WikiHelper.drawRect(BASE_X + left, BASE_Y + top, BASE_X + right, BASE_Y + bottom, color);
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
        
    }
    
    @Override
    public void onDeselected() {
        markDirty();
    }
}
