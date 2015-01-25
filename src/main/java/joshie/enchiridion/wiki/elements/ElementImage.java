package joshie.enchiridion.wiki.elements;

import static java.io.File.separator;
import static joshie.enchiridion.helpers.OpenGLHelper.disable;
import static joshie.enchiridion.helpers.OpenGLHelper.enable;
import static joshie.enchiridion.helpers.OpenGLHelper.end;
import static joshie.enchiridion.helpers.OpenGLHelper.scale;
import static joshie.enchiridion.helpers.OpenGLHelper.start;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glScalef;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import joshie.enchiridion.ELogger;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.helpers.ClientHelper;
import joshie.enchiridion.helpers.OpenGLHelper;
import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.WikiPage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;

import com.google.gson.annotations.Expose;
public class ElementImage extends Element {
    private DynamicTexture texture;
    private ResourceLocation resource;
    private boolean isDynamic;
    
    @Expose
    public String path;
    public int img_width;
    public int img_height;
    
    //Loads the image in to memory
	public void loadImage(WikiPage page) {
		if(!path.contains(":")) {
			try {
				BufferedImage img = null;
				if(path.startsWith("root.")) {
					ImageIO.read(new File(Enchiridion.root + separator + "wiki" + separator + path.replace("root.", "")));
				} else {
					img = ImageIO.read(new File(page.getPath() + separator + path));
				}
				
				texture = new DynamicTexture(img);
				resource = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(path, texture);
				isDynamic = true;
				img_width = img.getWidth();
				img_height = img.getHeight();
			} catch (Exception e) {
				ELogger.log(Level.ERROR, "Enchiridion 2 failed to read in the image at the following path: ");
				ELogger.log(Level.ERROR, page.getPath() + separator + path);
			}
		} else {
			String[] split = path.split(":");
			resource = new ResourceLocation(split[0], "textures/wiki" + separator + split[1]);
			try {
				BufferedImage image = ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(resource).getInputStream());
				img_width = (int) (image.getWidth() / 2.5);
				img_height = image.getHeight();
			} catch (Exception e) {
				ELogger.log(Level.ERROR, "Enchiridion 2 failed to read in the image at the following resource: ");
				ELogger.log(Level.ERROR, page.getPath() + separator + resource);
			}
			
			isDynamic = false;
		}
	}
    
    @Override
    public ElementImage setToDefault() {
    	this.width = 100;
        this.height = 100;
    	this.path = "enchiridion:enchiridion_logo.png";
    	loadImage(WikiHelper.getPage());
        return this;
    }
    
    @Override
    public void display(boolean isEditMode) {
    	OpenGLHelper.fixColors();
    	
    	if(isDynamic) {
	    	texture.updateDynamicTexture();
			Tessellator tessellator = Tessellator.instance;
			ClientHelper.getMinecraft().getTextureManager().bindTexture(resource);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(x, y + height, 0, 0.0, 1.0);
			tessellator.addVertexWithUV(x + width, y + height, 0, 1.0, 1.0);
			tessellator.addVertexWithUV(x + width, y, 0, 1.0, 0.0);
			tessellator.addVertexWithUV(x, y, 0, 0.0, 0.0);
			tessellator.draw();
    	} else {    		
    		ClientHelper.getMinecraft().getTextureManager().bindTexture(resource);
    		
    		
    		scaleTexture(BASE_X + left, BASE_Y + top, (float)width / 125F, (float)height / 125F);
    	}
    }
    
    private void scaleTexture(int x, int y, float scaleX, float scaleY) {
    	start();
        enable(GL_BLEND);
        ClientHelper.bindTexture(resource);
        glScalef(scaleX, scaleY, 1.0F);
        WikiHelper.drawTexture(WikiHelper.getScaledX(x, scaleX), WikiHelper.getScaledY(y, scaleY), 0, 0, img_width, img_height);
        disable(GL_BLEND);
        end();
    }
    
    @Override
    public void addEditButtons(List list) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onSelected(int x, int y) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onDeselected() {
        // TODO Auto-generated method stub
        
    }
}
