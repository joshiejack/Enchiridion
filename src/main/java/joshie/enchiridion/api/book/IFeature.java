package joshie.enchiridion.api.book;

import com.google.gson.JsonObject;

import java.util.List;

public interface IFeature {
    /** @return a duplicate of this feature **/
    IFeature copy();
    
    void update(IFeatureProvider position);
    void draw(int mouseX, int mouseY);
    void addTooltip(List<String> tooltip, int mouseX, int mouseY);
    void keyTyped(char character, int key);
    
    /** @return true if this feature should display yellow squares instead of blue
     *  Should also open any edit menus required **/
    boolean getAndSetEditMode();
    
    /* Called when not in edit mode, or shift is clicked, on mouseClick **/
    boolean performClick(int mouseX, int mouseY, int button);
    void performRelease(int mouseX, int mouseY, int button);
    void follow(int mouseX, int mouseY);
    void scroll(boolean down, int amount);
    void onDeselected();
    void readFromJson(JsonObject json);
    void writeToJson(JsonObject json);

    /** @return the name of this feature **/
    String getName();
}