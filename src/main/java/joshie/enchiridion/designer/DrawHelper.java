package joshie.enchiridion.designer;

import joshie.enchiridion.api.IDrawHelper;
import joshie.enchiridion.api.IItemStack;
import net.minecraft.util.IIcon;

public class DrawHelper implements IDrawHelper {
    private static boolean isBook;
    private static int left;
    private static int top;
    private static double height;
    private static double width;
    private static float size;
    
    public static void update(boolean isBook, int left, int top, double height, double width, float size) {
        DrawHelper.isBook = isBook;
        DrawHelper.left = left;
        DrawHelper.top = top;
        DrawHelper.height = height;
        DrawHelper.width = width;
        DrawHelper.size = size;
    }
    
    public static int getLeft(double x) {
        return (int) (left + ((x / 150D) * width));
    }
    
    public static int getTop(double y) {       
        return (int) (top + ((y / 100D) * height));
    }

    @Override
    public void drawStack(IItemStack stack) {
        if (isBook) {
            DesignerHelper.drawStack(stack.getItemStack(), getLeft(stack.getX()), getTop(stack.getY()), size * stack.getScale());
        }
    }

    @Override
    public void drawIcon(IIcon icon, double x, double y, int width, int height, float scale) {
        DesignerHelper.drawIcon(icon, getLeft(x), getTop(y), width, height, size * scale);
        
    }
    
    public static void testDrawRect(int x, int y, int u, int v, int w, int h, float scale) {
        DesignerHelper.drawTexturedRect(left + x, top + y, u, v, w, h, size * scale);
    }

    @Override
    public void drawTexturedRect(double x, double y, int u, int v, int w, int h, float scale) {
        DesignerHelper.drawTexturedRect(getLeft(x), getTop(y), u, v, w, h, size * scale);
    }

    @Override
    public void drawTexturedReversedRect(double x, double y, int u, int v, int w, int h, float scale) {
        DesignerHelper.drawReversedTexturedRect(getLeft(x), getTop(y), u, v, w, h, size * scale);
    }

    @Override
    public boolean isMouseOver(IItemStack stack) {
        if (stack == null || stack.getItemStack() == null) return false;
        int left = getLeft(stack.getX());
        int top = getTop(stack.getY());
        int scaled = (int) (16 * stack.getScale() * size);
        int right = left + scaled;
        int bottom = top + scaled;
        int x = DesignerHelper.getGui().mouseX;
        int y = DesignerHelper.getGui().mouseY;      
        return x >= left && x <= right && y >= top && y <= bottom;
    }
    
    @Override
    public boolean isOverArea(double x2, double y2, int width, int height, float scale) {
        int left = getLeft(x2);
        int top = getTop(y2);
        int scaledX = (int) (width * scale * size);
        int scaledY = (int) (height * scale * size);
        int right = left + scaledX;
        int bottom = top + scaledY;
        int x = DesignerHelper.getGui().mouseX;
        int y = DesignerHelper.getGui().mouseY;      
        return x >= left && x <= right && y >= top && y <= bottom;
    }

    @Override
    public void drawText(String text, double x, double y, int color, float scale) {
        DesignerHelper.drawSplitScaledString(text, getLeft(x), getTop(y), 300, color, size * scale);
    }
}
