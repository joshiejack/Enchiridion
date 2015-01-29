package joshie.enchiridion.designer.features;

import static java.io.File.separator;
import static joshie.enchiridion.helpers.OpenGLHelper.disable;
import static joshie.enchiridion.helpers.OpenGLHelper.enable;
import static joshie.enchiridion.helpers.OpenGLHelper.end;
import static joshie.enchiridion.helpers.OpenGLHelper.fixColors;
import static joshie.enchiridion.helpers.OpenGLHelper.start;
import static org.lwjgl.opengl.GL11.GL_BLEND;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import joshie.enchiridion.ELogger;
import joshie.enchiridion.helpers.ClientHelper;
import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.WikiPage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;

import com.google.gson.annotations.Expose;

public class FeatureImage extends Feature {
    private DynamicTexture texture;
    private ResourceLocation resource;
    private boolean isDynamic;

    @Expose
    public String path;
    public int img_width;
    public int img_height;

    public FeatureImage setPath(String fileName, String path) {
        WikiPage page = WikiHelper.getPage();
        this.width = 100;
        this.height = 100;
        this.path = fileName;
        loadImage(path);
        return this;
    }

    //Loads the image in to memory
    public void loadImage(String path) {
        if (!path.contains(":")) {
            try {
                BufferedImage img = ImageIO.read(new File(path));
                texture = new DynamicTexture(img);
                resource = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(path, texture);
                isDynamic = true;
                img_width = img.getWidth();
                img_height = img.getHeight();
            } catch (Exception e) {
                ELogger.log(Level.ERROR, "Enchiridion 2 failed to read in the image at the following path: ");
                ELogger.log(Level.ERROR, path + separator + path);
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
                ELogger.log(Level.ERROR, path + separator + resource);
            }

            isDynamic = false;
        }
    }

    @Override
    public void drawFeature() {
        fixColors();
        if (isDynamic) {
            start();
            enable(GL_BLEND);
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
            disable(GL_BLEND);
            end();
        } else if (resource != null) {
            ClientHelper.getMinecraft().getTextureManager().bindTexture(resource);
            gui.drawTexturedModalRect(x, y, 0, 0, img_width, img_height);
        } else if (resource == null) {
            loadImage(path);
        }
    }
}
