package joshie.enchiridion.api;


public interface IDrawHelper {
    public void drawStack(IItemStack stack);
    
    public void drawResource(double x1, double y1, double x2, double y2, int color);

    public void drawTexturedRect(double x, double y, int u, int v, int w, int h, float size);

    public void drawTexturedReversedRect(double x, double y, int u, int v, int w, int h, float size);

    public boolean isMouseOver(IItemStack stack);
}
