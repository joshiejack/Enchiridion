package joshie.enchiridion.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface IDrawHelper {

	public void drawSplitScaledString(String text, int xPos, int yPos, int wrap, int color, float scale);
	public void drawRectangle(int left, int top, int right, int bottom, int colorI);
	public void drawBorderedRectangle(int left, int top, int right, int bottom, int colorI, int colorB);
	public void drawStack(ItemStack stack, int left, int top, float size);
	public void drawResource(ResourceLocation resource, int left, int top, int width, int height, float scaleX, float scaleY);
	public void drawImage(ResourceLocation resource, int left, int top, int right, int bottom);
	
	/** For use with RecipeHandlers **/
	public void setRenderData(int xPos, int yPos, double width, double height, float size); //Called internally to update the internal sizes
	public void drawTexturedRectangle(double x, double y, int u, int v, int w, int h, float scale);
	public void drawTexturedReversedRectangle(double x, double y, int u, int v, int w, int h, float scale);
	public void drawIItemStack(IItemStack stack);
	public boolean isMouseOverIItemStack(IItemStack stack);    
	public boolean isMouseOverArea(double x, double y, int width, int height, float scale);
}
