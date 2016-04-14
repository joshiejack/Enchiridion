package joshie.enchiridion.api.book;

import com.google.gson.JsonObject;
import joshie.enchiridion.api.gui.ISimpleEditorFieldProvider;
import net.minecraft.util.ResourceLocation;

public interface IButtonAction extends ISimpleEditorFieldProvider {
    /** Create a copy of this action **/
    public IButtonAction copy();
	public IButtonAction create();
	public String getName();

    /** Reduce this stuff **/
	public ResourceLocation getResource(boolean isHovered);
    public String getText(boolean isHovered);

    /** Return the tooltip **/
    public String getTooltip();

    /** Setters **/
    public IButtonAction setResourceLocation(boolean isHovered, ResourceLocation resource);
    public IButtonAction setText(boolean isHovered, String text);
    public IButtonAction setTooltip(String tooltip);

    /** Perform the action **/
	public void performAction();

	/** Called to read data for the button **/
	public void readFromJson(JsonObject object);
	
	/** Called to write data for this button **/
	public void writeToJson(JsonObject object);
}
