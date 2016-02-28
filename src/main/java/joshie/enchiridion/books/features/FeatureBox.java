package joshie.enchiridion.books.features;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.IFeatureProvider;

public class FeatureBox extends AbstractFeatureWithColor {
	public transient int left;
	public transient int right;
	public transient int top;
	public transient int bottom;
	
	public FeatureBox() {}
	public FeatureBox(String color) {
		this.color = color;
	}
	
	@Override
	public String getName() {
		return "Box: " + Integer.toHexString(colorI);
	}
	
	@Override
	public void update(IFeatureProvider position) {
		super.update(position);
		
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
}
