package joshie.enchiridion.designer.features;

import joshie.enchiridion.designer.GuiDesigner;
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
    protected double width;
    @Expose
    protected double height;
    
    protected GuiDesigner gui;
    private boolean isSelected;
    private boolean isHeld;
    private int prevX;
    private int prevY;
    protected int left;
    protected int right;
    protected int top;
    protected int bottom;
    
    public Feature() {
        this.width = 50;
        this.height = 50;
    }
    
    public void recalculate(int x, int y) {
        left = x + xPos;
        right = (int) (x + xPos + (width * 2));
        top = y + yPos;
        bottom = (int) (y + yPos + (height * 2));
    }
    
    public void draw(GuiDesigner gui, int x, int y) {
        this.gui = gui;
        recalculate(x, y);
        drawFeature();
        //If We are in edit mode draw the boxes around the feature
        if(gui.canEdit && isSelected) {
            gui.drawRect(left - 4, top - 4, left, top, 0xFF2693FF);
            gui.drawRect(right, top - 4, right + 4, top, 0xFF2693FF);
            gui.drawRect(left - 4, bottom, left, bottom + 4, 0xFF2693FF);
            gui.drawRect(right, bottom, right + 4, bottom + 4, 0XFFFFFF00);
        }
    }
    
    private boolean noOtherSelected() {
        return gui.canvas.selected == null;
    }
    
    private void clearSelected() {
        gui.canvas.selected = null;
    }
    
    private void setSelected() {
        gui.canvas.selected = this;
    }
    
    public abstract void drawFeature();
    
    public boolean isOverFeature(int x, int y) {
        return x >= xPos && x <= xPos + (width * 2) && y >= (yPos - (height * 2)) && y <= yPos;
    }
    
    public boolean isOverCorner(int x, int y) {
        if ((x >= xPos - 4 && x <= xPos) || (x >= xPos + (width * 2) && x <= xPos + (width * 2) + 4)) {
            if((y >= yPos - (height * 2) - 4 && y <= yPos - (height * 2)) || (y >= yPos && y <= yPos + 4)) {
                return true;
            }
        }
        
        return false;
    }

    public void click(int x, int y, boolean isEditMode) {
        //If it is editmode let's select or deselect this item whenever we click on it
        if(isEditMode) {
            //If you click inside the box, then we will set the held to true
            if(isOverFeature(x, y) && noOtherSelected()) {
                isHeld = true;
                isSelected = true;
                prevX = x;
                prevY = y;
            } else {
                if(isSelected) {
                    clearSelected();
                }
                
                isSelected = false;
            }
        }
    }
    
    public void release(int x, int y) {
        if(isHeld) {
            isHeld = false;
        }
    }
    
    public void follow(int x, int y) {
        if(isHeld) {
            xPos += x - prevX;
            yPos += y - prevY;
            prevX = x;
            prevY = y;
        }
    }
}
