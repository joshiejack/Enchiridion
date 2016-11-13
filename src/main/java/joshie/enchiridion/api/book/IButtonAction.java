package joshie.enchiridion.api.book;

import com.google.gson.JsonObject;
import joshie.enchiridion.api.gui.ISimpleEditorFieldProvider;
import net.minecraft.util.ResourceLocation;

public interface IButtonAction extends ISimpleEditorFieldProvider {
    /** Create a copy of this action **/
    IButtonAction copy();
    IButtonAction create();
    String getName();

    /** Perform the action, Return true if it was successful **/
    boolean performAction();

    /** Whether the button this action is attached to, should be visible **/
    boolean isVisible();

    /** Called to read data for the button **/
    void readFromJson(JsonObject object);

    /** Called to write data for this button **/
    void writeToJson(JsonObject object);

    /** @return the resource location to display for this action **/
    ResourceLocation getResource();
}