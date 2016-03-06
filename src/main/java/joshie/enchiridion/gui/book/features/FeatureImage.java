package joshie.enchiridion.gui.book.features;

import java.io.IOException;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.lib.EInfo;
import net.minecraft.util.ResourceLocation;

public class FeatureImage extends FeatureResource {
    public transient int left;
	public transient int right;
	public transient int top;
	public transient int bottom;
	public transient String name;
	
	public FeatureImage() {}
	public FeatureImage(String path) {
		this.path = path;
	}
	
	@Override
	public FeatureImage copy() {
	    return new FeatureImage(path);
	}
	
	@Override
	public String getName() {
		return name;
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
	    name = path.replace(EInfo.MODID + ":images/", "").replace(".png", "").split("/")[1];
	}
	
	@Override
	protected String getResourcePath() {
		//If the path was created in 1.7, update it to use the new format
		if (!path.contains(":")) {
			path = EInfo.MODID + ":images/" + path;
		}
		
		return path;
	}
	
	@Override
	protected void draw(int xPos, int yPos, double width, double height) {
    	EnchiridionAPI.draw.drawImage(resource, left, top, right, bottom);
    }
	
	@Override
	protected void readImage(String[] split) throws IOException {}
}
