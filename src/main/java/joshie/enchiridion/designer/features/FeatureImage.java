package joshie.enchiridion.designer.features;

import static joshie.enchiridion.helpers.OpenGLHelper.fixColors;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import joshie.enchiridion.EConfig;
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
        if (!EConfig.DEFAULT_DIR.equals("")) {
            this.path = "mod@" + EConfig.DEFAULT_DIR + "@" + path;
        }

        loadImage(path);
        return this;
    }

    //Loads the image in to memory
    public void loadImage(String path) {
        if (!path.contains(":")) {
            try {
                BufferedImage img = null;
                if (path.startsWith("mod@")) {
                    String[] split = path.split("@");
                    String image = "/assets/" + split[1] + "/books/images/" + split[2];
                    img = ImageIO.read(Enchiridion.class.getResourceAsStream(image));
                } else img = ImageIO.read(new File(Enchiridion.root, "books/images/" + path));

                texture = new DynamicTexture(img);
                resource = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(path, texture);
            } catch (Exception e) {
                ELogger.log(Level.ERROR, "Enchiridion 2 failed to read in the image at the following path: ");
                ELogger.log(Level.ERROR, path);
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
