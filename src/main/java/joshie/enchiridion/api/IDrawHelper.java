package joshie.enchiridion.api;


public interface IDrawHelper {
    public void drawStack(IItemStack stack, double x, double y, float size);
    
    public void drawResource(double x1, double y1, double x2, double y2, int color);

    public void drawTexturedRect(double x, double y, int u, int v, int w, int h);
}
