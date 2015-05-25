package joshie.enchiridion.designer;

import static joshie.enchiridion.helpers.OpenGLHelper.end;
import static joshie.enchiridion.helpers.OpenGLHelper.scaleAll;
import static joshie.enchiridion.helpers.OpenGLHelper.start;
import joshie.enchiridion.EClientProxy;
import joshie.enchiridion.EConfig;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.IDrawHelper;
import joshie.enchiridion.api.IItemStack;
import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.elements.Element;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class DrawHelper implements IDrawHelper {
    public static enum DrawType {
        BOOK, WIKI, RECIPE;
    }

    private static DrawType type;
    private static int left;
    private static int top;
    private static double height;
    private static double width;
    private static float size;

    /** For simpler things **/
    public static void update(DrawType type) {
        DrawHelper.type = type;
    }

    /** For complicated things **/
    public static DrawType update(DrawType type, int left, int top, double height, double width, float size) {
        DrawType previous = DrawHelper.type;
        DrawHelper.type = type;
        DrawHelper.left = left;
        DrawHelper.top = top;
        DrawHelper.height = height;
        DrawHelper.width = width;
        DrawHelper.size = size;
        return previous;
    }

    public static int getLeft(double x) {
        return (int) (left + ((x / 150D) * width));
    }

    public static int getTop(double y) {
        return (int) (top + ((y / 100D) * height));
    }

    public static void drawStack(IItemStack stack) {
        stack.onDisplayTick();
        EnchiridionAPI.draw.drawStack(stack.getItemStack(), stack.getX(), stack.getY(), stack.getScale());
    }
    
    @Override
    public void drawStack(ItemStack stack, double x, double y, float scale) {
        if (type ==DrawType.RECIPE) DesignerHelper.drawStack(stack, getLeft(x), getTop(y), size * scale);
        else if (type == DrawType.BOOK) DesignerHelper.drawStack(stack, (int)x, (int)y, scale);
        else if (type == DrawType.WIKI) {
            int scaledX = (int) ((WikiHelper.theLeft + Element.BASE_X + x) / scale);
            int scaledY = ((int) ((WikiHelper.theTop + Element.BASE_Y + y) / scale));
            
            start();
            scaleAll(size);
            WikiHelper.renderStack(stack, scaledX, scaledY);
            end();
        }
    }

    @Override
    public void drawIcon(IIcon icon, double x, double y, int width, int height, float scale) {
        if (type == DrawType.RECIPE) DesignerHelper.drawIcon(icon, getLeft(x), getTop(y), width, height, size * scale);
    }

    @Override
    public void drawRect(int left, int top, int right, int bottom, int color) {
        if (type == DrawType.RECIPE) DesignerHelper.drawRect(getLeft(left), getTop(top), getLeft(right), getTop(top), color);
        else if (type == DrawType.BOOK) DesignerHelper.drawRect(left, top, right, bottom, color);
        else if (type == DrawType.WIKI) WikiHelper.drawRect(Element.BASE_X + left, Element.BASE_Y + top, Element.BASE_X + right, Element.BASE_Y + bottom, color);
    }

    @Override
    public void drawTexturedRect(double x, double y, int u, int v, int w, int h, float scale) {
        if (type == DrawType.RECIPE) DesignerHelper.drawTexturedRect(getLeft(x), getTop(y), u, v, w, h, size * scale);
    }

    @Override
    public void drawTexturedReversedRect(double x, double y, int u, int v, int w, int h, float scale) {
        if (type == DrawType.RECIPE) DesignerHelper.drawReversedTexturedRect(getLeft(x), getTop(y), u, v, w, h, size * scale);
    }

    @Override
    public void drawText(String text, double x, double y, int color, int wrap, float scale) {
        if (type == DrawType.RECIPE) DesignerHelper.drawSplitScaledString(text, getLeft(x), getTop(y), 300, color, size * scale);
        else if (type == DrawType.WIKI) {
            start();
            scaleAll(scale);

            if (EConfig.SHOW_BB_CODE_IN_EDIT_MODE && WikiHelper.isEditMode()) {
                EClientProxy.font.drawUnformattedSplitString(text, (int) ((WikiHelper.theLeft + Element.BASE_X + x) / scale), ((int) ((WikiHelper.theTop + Element.BASE_Y + y) / scale)), (int) ((width * 2) / scale) + 4, color);
            } else {
                EClientProxy.font.drawSplitString(text, (int) ((WikiHelper.theLeft + Element.BASE_X + x) / scale), ((int) ((WikiHelper.theTop + Element.BASE_Y + y) / scale)), (int) ((width * 2) / scale) + 4, color);
            }

            end();
        } else if (type == DrawType.BOOK) {
            if (wrap >= 50) {
                DesignerHelper.drawSplitScaledString(text, (int) x, (int) y, wrap, color, size);
            } else DesignerHelper.drawSplitScaledString(text, (int) x, (int) y, Math.max(50, (int) ((width) / scale) + 4), color, scale);
        }
    }

    public static boolean isMouseOver(IItemStack stack) {
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

    public static boolean isOverArea(double x2, double y2, int width, int height, float scale) {
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
}
