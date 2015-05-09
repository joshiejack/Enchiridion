package joshie.enchiridion.designer;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
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
    public void drawStack(IItemStack stack) {
        if (isBook) {
            DesignerHelper.drawStack(stack.getItemStack(), getLeft(stack.getX()), getTop(stack.getY()), size * stack.getScale());
        }
    }

    @Override
    public void drawResource(double x1, double y1, double x2, double y2, int color) {
        // TODO Auto-generated method stub
        
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
}
