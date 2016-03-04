package joshie.enchiridion.gui.book.features;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.gui.book.GuiSimpleEditor;
import joshie.enchiridion.gui.book.GuiSimpleEditorColor;
import joshie.lib.editables.IColorable;

public class FeatureBox extends FeatureAbstract implements IColorable {
    public String color;
    public transient int colorI;
    
	public transient int left;
	public transient int right;
	public transient int top;
	public transient int bottom;
	
	public FeatureBox() {}
	public FeatureBox(String color) {
		this.color = color;
	}
	
	@Override
	public FeatureBox copy() {
	    return new FeatureBox(color);
	}
	
	@Override
	public String getName() {
		return "Box: " + Integer.toHexString(colorI);
	}
	
	@Override
    public boolean getAndSetEditMode() {
        GuiSimpleEditor.INSTANCE.setEditor(GuiSimpleEditorColor.INSTANCE.setColorable(this));     
        return false;
    }
	
	private void attemptToParseColor() {
	    int previousColor = this.colorI;
	    if (!attemptToParseString(color)) {
	        String doubled = color.replaceAll(".", "$0$0");
	        if (!attemptToParseString(doubled)) {
	            if(!attemptToParseString(doubled.replace("#", ""))) {
	                this.colorI = previousColor;
	            }
	        }
	    }
	}
	
	private boolean attemptToParseString(String string) {
	    try {
	        colorI = (int) Long.parseLong(string, 16);
	        return true;
	    } catch (Exception e) { return false; }
	}
	
	@Override
	public void update(IFeatureProvider position) {
	    attemptToParseColor();
		
		int xPos = position.getX();
		int yPos = position.getY();
		
		left = xPos;
	    right = (int) (xPos + position.getWidth());
	    top = yPos;
	    bottom = (int) (yPos + position.getHeight());
	}

    @Override
    public void draw(int xPos, int yPos, double width, double height, boolean isMouseHovering) {
    	EnchiridionAPI.draw.drawRectangle(left, top, right, bottom, colorI);
    }
    
    @Override
    public String getColorAsHex() {
        return color;
    }
    
    @Override
    public void setColorAsHex(String color) {
        this.color = color;
        attemptToParseColor();
    }
}
