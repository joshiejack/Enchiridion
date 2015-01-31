package joshie.enchiridion.designer.features;

import static java.io.File.separator;
import static joshie.enchiridion.helpers.OpenGLHelper.fixColors;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import joshie.enchiridion.ELogger;
import joshie.enchiridion.Enchiridion;
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

    @Expose
    public String path;

    public FeatureImage() {
        width = 100;
        height = 100;
    }

    public FeatureImage(Feature feature) {
        super(feature);
    }

    public FeatureImage setPath(String path) {
        WikiPage page = WikiHelper.getPage();
        this.width = 100;
        this.height = 100;
        this.path = path;
        loadImage(path);
        return this;
    }

    //Loads the image in to memory
    public void loadImage(String path) {
        if (!path.contains(":")) {
            try {
                BufferedImage img = ImageIO.read(new File(Enchiridion.root, "books/images/" + path));
                texture = new DynamicTexture(img);
                resource = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(path, texture);
            } catch (Exception e) {
                ELogger.log(Level.ERROR, "Enchiridion 2 failed to read in the image at the following path: ");
                ELogger.log(Level.ERROR, path + separator + path);
            }
        }
    }

    @Override
    public void drawFeature() {
        fixColors();
        if (texture != null && resource != null) {
            DesignerHelper.drawImage(texture, resource, left, top, right, bottom);
        } else if (resource == null) {
            loadImage(path);
        }
    }
}
