package joshie.enchiridion.api;

import java.util.List;

import com.google.gson.JsonObject;

public interface IFeature {
    /** Return a duplicate of this feature **/
    public IFeature copy();
    
	public void update(IFeatureProvider position);
	public void draw(int posX, int posY, double width, double height, boolean isMouseHovering);
    public void addTooltip(List<String> tooltip, int mouseX, int mouseY);
    public void keyTyped(char character, int key);
    
    /** Return true if this feature should display yellow squares instead of blue
     *  Should also open any edit menus required **/
    public boolean getAndSetEditMode();
    
    /** Called when not in edit mode, or shift is clicked, on mouseClick **/
    public void performAction(int mouseX, int mouseY);
    public void follow(int mouseX, int mouseY);
    public void scroll(boolean down);
	public void onDeselected();
	public void readFromJson(JsonObject json);
	public void writeToJson(JsonObject json);
	
	/** Return the name of this feature **/
	public String getName();
}
