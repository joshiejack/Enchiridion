package joshie.enchiridion.books.features;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.Level;

import joshie.enchiridion.ELogger;
import joshie.enchiridion.api.EnchiridionAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class FeatureResource extends AbstractFeature {
	public String path;
		
	public transient ResourceLocation resource;
    public transient int img_width;
    public transient int img_height;
    public transient boolean attempted;
    
    @Override
    public FeatureResource copy() {
        FeatureResource resource = new FeatureResource();
        resource.path = path;
        return resource;
    }

    @Override
    public void draw(int xPos, int yPos, double width, double height, boolean isMouseHovering) {
    	if (resource != null) {
    		draw(xPos, yPos, width, height);
    	} else if (!attempted) {
    		attempted = loadResource();
    	}
    }
    
    protected void draw(int xPos, int yPos, double width, double height) {
    	EnchiridionAPI.draw.drawResource(resource, xPos, yPos, img_width, img_height, (float) width / 250F, (float) height / 250F);
    }
    
    protected String getResourcePath() {
    	return path;
    }
    
    protected void readImage(String[] split) throws IOException {
    	double splitX = split.length >= 3 ? Double.parseDouble(split[2]) : 1D;
        double splitY = split.length == 4 ? Double.parseDouble(split[3]) : 1D;
        BufferedImage image = ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(resource).getInputStream());
        img_width = (int) (image.getWidth() / splitX);
        img_height = (int) (image.getHeight() / splitY);
    }
    
    private boolean loadResource() {
    	String path = getResourcePath();   
    	if (path == null) return false;
    	String[] split = path.split(":");
        if (split.length == 2 || split.length == 3 || split.length == 4) resource = new ResourceLocation(split[0], split[1]);
        try {
        	readImage(split);
        } catch (Exception e) {
            ELogger.log(Level.ERROR, "Enchiridion 2 failed to read in the image at the following resource: ");
            ELogger.log(Level.ERROR, path);
        	e.printStackTrace();
        }
        
        return true;
    }
}
