package joshie.enchiridion.api.book;

import com.google.gson.JsonObject;
import joshie.enchiridion.api.gui.ISimpleEditorFieldProvider;
import net.minecraft.util.ResourceLocation;

public interface IButtonAction extends ISimpleEditorFieldProvider {
    /** Create a copy of this action **/
    public IButtonAction copy();
    public IButtonAction create();
    public String getName();

    /** Perform the action **/
    public void performAction();

    /** Whether the button this action is attached to, should be visible **/
    public boolean isVisible();

    /** Called to read data for the button **/
    public void readFromJson(JsonObject object);

    /** Called to write data for this button **/
    public void writeToJson(JsonObject object);

    /** Return the resource location to display for this action **/
    public ResourceLocation getResource();
}
