package joshie.enchiridion.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;


public interface IDrawHelper {
    public void drawStack(ItemStack stack, double x, double y, float scale);
    
    public void drawIcon(IIcon icon, double x, double y, int width, int height, float size);
    
    public void drawRect(int left, int top, int right, int bottom, int color);

    public void drawTexturedRect(double x, double y, int u, int v, int w, int h, float size);

    public void drawTexturedReversedRect(double x, double y, int u, int v, int w, int h, float size);
    
    public void drawText(String text, double x, double y, int color, int wrap, float size);
}
