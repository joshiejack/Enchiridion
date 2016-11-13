package joshie.enchiridion.api.gui;

import joshie.enchiridion.api.recipe.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface IDrawHelper {

    void drawSplitScaledString(String text, int xPos, int yPos, int wrap, int color, float scale);
    void drawRectangle(int left, int top, int right, int bottom, int colorI);
    void drawBorderedRectangle(int left, int top, int right, int bottom, int colorI, int colorB);
    void drawStack(ItemStack stack, int left, int top, float size);
    void drawResource(ResourceLocation resource, int left, int top, int width, int height, float scaleX, float scaleY);
    void drawImage(ResourceLocation resource, int left, int top, int right, int bottom);
    void drawLine(int left, int top, int right, int bottom, int thickness, int color);

    /** For use with RecipeHandlers **/
    void setRenderData(int xPos, int yPos, double width, double height, float size); //Called internally to update the internal sizes
    void drawTexturedRectangle(double x, double y, int u, int v, int w, int h, float scale);
    void drawTexturedReversedRectangle(double x, double y, int u, int v, int w, int h, float scale);
    void drawIItemStack(IItemStack stack);
    boolean isMouseOverIItemStack(IItemStack stack);
    boolean isMouseOverArea(double x, double y, int width, int height, float scale);
}