package joshie.enchiridion.designer.features;

import static java.io.File.separator;
import static joshie.enchiridion.helpers.OpenGLHelper.fixColors;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import joshie.enchiridion.ELogger;
import joshie.enchiridion.designer.DesignerHelper;
import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.WikiPage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import org.apache.logging.log4j.Level;

import com.google.gson.annotations.Expose;

public class FeatureImage extends Feature {
    private DynamicTexture texture;
    private ResourceLocation resource;
    private boolean isDynamic;

    @Expose
    public String path;
    public int img_width;
    public int img_height;

    public FeatureImage() {
        width = 100;
        height = 100;
        path = "enchiridion:enchiridion_logo.png";
    }

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
            DesignerHelper.drawImage(texture, resource, left, top, right, bottom);
        } else if (resource != null) {
            DesignerHelper.drawResource(resource, left, top, img_width, img_height);
        } else if (resource == null) {
            loadImage(path);
        }
    }
}
