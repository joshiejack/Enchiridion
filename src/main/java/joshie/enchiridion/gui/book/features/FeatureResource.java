package joshie.enchiridion.gui.book.features;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IFeatureProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class FeatureResource extends FeatureAbstract {
	public String path;
		
	protected transient ResourceLocation resource;
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
    public void update(IFeatureProvider position) { //Preload the resource
        super.update(position);
        attempted = loadResource();
    }
    
    public void setResource(ResourceLocation resource) {
        this.resource = new ResourceLocation(resource.getResourceDomain(), resource.getResourcePath());
        this.path = resource.toString();
    }
    
    public ResourceLocation getResource() {
       return this.resource;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
    	if (resource != null) {
    		draw(position.getLeft(), position.getTop(), position.getWidth(), position.getHeight());
    	} else if (!attempted) attempted = loadResource();
    	
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
            Enchiridion.log(Level.ERROR, "Enchiridion 2 failed to read in the image at the following resource: ");
            Enchiridion.log(Level.ERROR, path);
        	e.printStackTrace();
        }
        
        return true;
    }
}
