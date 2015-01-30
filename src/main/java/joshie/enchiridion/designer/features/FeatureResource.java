package joshie.enchiridion.designer.features;

import static java.io.File.separator;
import static joshie.enchiridion.helpers.OpenGLHelper.fixColors;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import joshie.enchiridion.ELogger;
import joshie.enchiridion.designer.DesignerHelper;
import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.WikiPage;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import org.apache.logging.log4j.Level;

import com.google.gson.annotations.Expose;

public class FeatureResource extends FeatureWithText {
    private ResourceLocation resource;

    @Expose
    public String path;
    public int img_width;
    public int img_height;

    public FeatureResource() {
        width = 100;
        height = 100;
        path = "enchiridion:textures/wiki/enchiridion_logo.png";
    }

    public FeatureResource setPath(String path) {
        WikiPage page = WikiHelper.getPage();
        this.width = 100;
        this.height = 100;
        this.path = path;
        loadImage(path);
        return this;
    }

    //Loads the image in to memory
    public void loadImage(String path) {
        String[] split = path.split(":");
        if(split.length == 2 || split.length == 3 || split.length == 4)
        resource = new ResourceLocation(split[0], split[1]);
        try {
            double splitX = split.length >= 3? Double.parseDouble(split[2]): 1D;
            double splitY = split.length == 4? Double.parseDouble(split[3]): 1D;
            BufferedImage image = ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(resource).getInputStream());
            img_width = (int) (image.getWidth() / splitX);
            img_height = (int) (image.getHeight() / splitY);
        } catch (Exception e) {
            ELogger.log(Level.ERROR, "Enchiridion 2 failed to read in the image at the following resource: ");
            ELogger.log(Level.ERROR, path + separator + resource);
        }

    }

    @Override
    public void drawFeature() {
        fixColors();
        if (resource != null) {
            DesignerHelper.drawResource(resource, left, top, img_width, img_height, (float) width / 250F, (float) height / 250F);
        } else if (resource == null) {
            loadImage(path);
        }

        if (isSelected) {
            DesignerHelper.drawRect(-102, -55, -100, -37, 0xFFFFFFFF);
            DesignerHelper.drawRect(0, -55, 2, -37, 0xFFFFFFFF);
            DesignerHelper.drawRect(-102, -57, 2, -55, 0xFFFFFFFF);
            DesignerHelper.drawRect(-100, -55, 0, -37, 0xFF000000);
            DesignerHelper.drawRect(-100, -37, 0, -39, 0xFFFFFFFF);
            DesignerHelper.drawSplitString(getText(), -95, -50, 250, 0xFFFFFFFF);
        }
    }

    @Override
    public void loadEditor() {
        // DRAW Image Selection

    }

    @Override
    public String getTextField() {
        return path;
    }

    @Override
    public void setTextField(String str) {
        path = str;
        if (str.contains("\n")) {
            path = path.replace("\n", "");
            loadImage(path);
        }
    }
}
