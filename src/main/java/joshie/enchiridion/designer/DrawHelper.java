package joshie.enchiridion.designer;

import joshie.enchiridion.api.IDrawHelper;
import joshie.enchiridion.api.IItemStack;

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
    public void drawStack(IItemStack stack, double x, double y, float scale) {
        if (isBook) {
            DesignerHelper.drawStack(stack.getItemStack(), getLeft(x), getTop(y), size * scale);
        }
    }

    @Override
    public void drawResource(double x1, double y1, double x2, double y2, int color) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void drawTexturedRect(double x, double y, int u, int v, int w, int h) {
        DesignerHelper.drawTexturedRect(getLeft(x), getTop(y), u, v, w, h, size);
    }
}
