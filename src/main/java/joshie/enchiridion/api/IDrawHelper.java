package joshie.enchiridion.api;

import net.minecraft.util.IIcon;


public interface IDrawHelper {
    public void drawStack(IItemStack stack);
    
    public void drawIcon(IIcon icon, double x, double y, int width, int height, float size);

    public void drawTexturedRect(double x, double y, int u, int v, int w, int h, float size);

    public void drawTexturedReversedRect(double x, double y, int u, int v, int w, int h, float size);
    
    public void drawText(String text, double x, double y, int color, float size);

    public boolean isMouseOver(IItemStack stack);

    public boolean isOverArea(double x, double y, int width, int height, float scale);
}
