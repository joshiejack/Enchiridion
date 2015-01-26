package joshie.enchiridion.designer.features;

import joshie.enchiridion.designer.gui.GuiDesigner;

import com.google.gson.annotations.Expose;

public abstract class Feature {
    @Expose
    protected int xPos;
    @Expose
    protected int yPos;
    
    protected GuiDesigner gui;
    protected int x;
    protected int y;
    
    public void draw(GuiDesigner gui, int x, int y) {
        this.gui = gui;
        this.x = x + xPos;
        this.y = y + yPos;
        drawFeature();
    }
    
    public abstract void drawFeature();
}
