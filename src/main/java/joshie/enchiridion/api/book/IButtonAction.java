package joshie.enchiridion.api.book;

import com.google.gson.JsonObject;

import net.minecraft.util.ResourceLocation;

public interface IButtonAction {
    /** Create a copy of this action **/
    public IButtonAction copy();
	/** Return the name of all the fields that should be edited **/
	public String[] getFieldNames();
	public IButtonAction create();
	public String getName();
	public ResourceLocation getHovered();
	public ResourceLocation getUnhovered();
	
	public String getHoverText();
	public String getUnhoverText();
	public String getTooltip();
	public void performAction();
	
	/** Called on the first turn this button tries to render **/
	public void initAction();
	
	/** Called to read data for the button **/
	public void readFromJson(JsonObject object);
	
	/** Called to write data for this button **/
	public void writeToJson(JsonObject object);
}
