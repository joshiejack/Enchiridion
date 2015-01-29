package joshie.enchiridion.designer.features;

import joshie.enchiridion.designer.GuiDesigner;
import joshie.enchiridion.wiki.WikiHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderItem;

import com.google.gson.annotations.Expose;

public abstract class Feature {
    public static final RenderBlocks renderer = new RenderBlocks();
    public static final RenderItem itemRenderer = new RenderItem();
    
    @Expose
    protected int xPos;
    @Expose
    protected int yPos;
    @Expose
    protected int width;
    @Expose
    protected int height;
    
    protected GuiDesigner gui;
    protected int x;
    protected int y;
    private boolean isSelected;
    
    public Feature() {
        this.width = 100;
        this.height = 100;
    }
    
    public void draw(GuiDesigner gui, int x, int y) {
        this.gui = gui;
        this.x = x + xPos;
        this.y = y + yPos;
        drawFeature();
        //If We are in edit mode draw the boxes around the feature
        if(gui.canEdit && isSelected) {
            gui.drawRect(x - 4, y - 4, x, y, 0xFF2693FF);
            gui.drawRect(x + width, y - 4, x + width + 4, y, 0xFF2693FF);
            gui.drawRect(x - 4, y + height, x, y + height + 4, 0xFF2693FF);
            gui.drawRect(x + width, y + height, x + width + 4, y + height + 4, 0XFFFFFF00);
        }
    }
    
    public abstract void drawFeature();

    public void click(int x, int y, boolean isEditMode) {
        //If it is editmode let's select or deselect this item whenever we click on it
        if(isEditMode) {
            if(x >= xPos && x <= xPos + width && y <= yPos && y >= yPos - height) {
                isSelected = !isSelected;
            }
        }
    }
}
