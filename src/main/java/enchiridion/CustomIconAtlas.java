package enchiridion;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.Level;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.BaseMetadataSectionSerializer;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import enchiridion.api.GuideHandler;
import enchiridion.api.pages.PageImage;
import enchiridion.api.pages.PageImage.LinkedTexture;

public class CustomIconAtlas extends TextureAtlasSprite {	
	private String zip;
	private String name;
	protected CustomIconAtlas(String zip, String name) {
		super(name);
		this.name = name;
		this.zip = zip;
	}

	@Override
	public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location)  {
		return true;
    } 
		
	@Override
	public boolean load(IResourceManager manager, ResourceLocation sss) {
		try {
			if(!GuideHandler.DEBUG_ENABLED) {
				ZipFile zipfile = new ZipFile(new File(Enchiridion.root + File.separator + zip + ".zip"));
				ZipEntry zipentry = zipfile.getEntry(name + ".png");
				BufferedImage img = ImageIO.read(zipfile.getInputStream(zipentry));
				zipfile.close();
				BufferedImage[] abufferedimage = new BufferedImage[1];
				abufferedimage[0] = img;
				super.loadSprite(abufferedimage, null, false);
			} else {
				try {
					File debugFolder = new File(Enchiridion.root + File.separator + "debug");
					BufferedImage img = ImageIO.read(new File(debugFolder + name + ".png"));
					BufferedImage[] abufferedimage = new BufferedImage[1];
					abufferedimage[0] = img;
					super.loadSprite(abufferedimage, null, false);
				} catch (ReportedException e) {
					BookLogHandler.log(Level.ERROR, "YOU ARE MISSING A REQUIRED ICON IN DEBUG MODE");
				}
			}
		} catch (Exception e) { e.printStackTrace(); }
        return false;
    } 
}
