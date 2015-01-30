package joshie.enchiridion.designer;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import joshie.enchiridion.ELogger;
import joshie.enchiridion.Enchiridion;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import org.apache.logging.log4j.Level;

public class BookIconAtlas extends TextureAtlasSprite {
    private String name;

    protected BookIconAtlas(String name) {
        super(name);
        this.name = name;
    }

    @Override
    public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
        return true;
    }

    @Override
    public boolean load(IResourceManager manager, ResourceLocation sss) {
        try {
            File iconDir = new File(Enchiridion.root, "books/icons/");
            File icon = new File(iconDir, name);
            BufferedImage img = ImageIO.read(new File(iconDir, name));
            BufferedImage[] abufferedimage = new BufferedImage[1];
            abufferedimage[0] = img;
            super.loadSprite(abufferedimage, null, false);
            ELogger.log(Level.INFO, "Successfully loaded in the book icon @ " + icon);
        } catch (Exception e) {
            ELogger.log(Level.ERROR, "The icon specified could not be found");
            e.printStackTrace();
        }

        return false;
    }
}