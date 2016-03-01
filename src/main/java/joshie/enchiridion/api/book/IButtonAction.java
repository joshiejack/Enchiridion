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
	
	public String getTooltip();
	public void performAction();
	public void readFromJson(JsonObject object);
	public void writeToJson(JsonObject object);
}
