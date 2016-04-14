package joshie.enchiridion.api.book;

import com.google.gson.JsonObject;

import java.util.List;

public interface IFeature {
    /** Return a duplicate of this feature **/
    public IFeature copy();
    
    public void update(IFeatureProvider position);
    public void draw(int mouseX, int mouseY);
    public void addTooltip(List<String> tooltip, int mouseX, int mouseY);
    public void keyTyped(char character, int key);
    
    /** Return true if this feature should display yellow squares instead of blue
     *  Should also open any edit menus required **/
    public boolean getAndSetEditMode();
    
    /** Called when not in edit mode, or shift is clicked, on mouseClick **/
    public void performClick(int mouseX, int mouseY);
    public void performRelease(int mouseX, int mouseY);
    public void follow(int mouseX, int mouseY);
    public void scroll(boolean down, int amount);
    public void onDeselected();
    public void readFromJson(JsonObject json);
    public void writeToJson(JsonObject json);

    /** Return the name of this feature **/
    public String getName();
}
