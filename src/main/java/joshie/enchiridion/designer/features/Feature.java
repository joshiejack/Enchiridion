package joshie.enchiridion.designer.features;

import joshie.enchiridion.designer.gui.GuiDesigner;
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
    
    public void draw(GuiDesigner gui, int x, int y) {
        this.gui = gui;
        this.x = x + xPos;
        this.y = y + yPos;
        drawFeature();
    }
    
    public abstract void drawFeature();
}
