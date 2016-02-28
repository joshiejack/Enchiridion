package joshie.enchiridion.api;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface IBookHelper {
	public boolean isEditMode();
	public void setPage(IPage page);
	
	public void drawSplitScaledString(String text, int xPos, int yPos, int wrap, int color, float scale);
	public void drawRectangle(int left, int top, int right, int bottom, int colorI);
	public void drawBorderedRectangle(int left, int top, int right, int bottom, int colorI, int colorB);
	public void drawStack(ItemStack stack, int left, int top, float size);
	public void drawResource(ResourceLocation resource, int left, int top, int width, int height, float scaleX, float scaleY);
	public void drawImage(ResourceLocation resource, int left, int top, int right, int bottom);
	public IPage getPage();
	public List<IPage> getBookPages();
	public String getBookSaveName();
	public String getBookID();
	public IFeatureProvider getSelectedFeature();
	public void setSelected(IFeatureProvider provider);
}
