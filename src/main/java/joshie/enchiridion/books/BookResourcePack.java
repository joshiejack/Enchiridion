package joshie.enchiridion.books;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;

import com.google.common.collect.ImmutableSet;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.ELogger;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.lib.EInfo;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;

public class BookResourcePack implements IResourcePack {
	public static final BookResourcePack INSTANCE = new BookResourcePack();
	
	private BookResourcePack(){}
	private static final Set<String> domains = ImmutableSet.<String>of(EInfo.MODID);
	
	private File getFile(ResourceLocation location) {
		String path = location.getResourcePath();
		String name = path.startsWith("images")? path.substring(7): path.startsWith("textures")? path.substring(14): path.substring(12);
		String folder = path.startsWith("images") ? "images" : "icons";
		File directory = new File(new File(Enchiridion.root, "books"), folder);
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IllegalStateException("Couldn't create dir: " + directory);
        }
        
        return new File(directory, name);
	}
		
	@Override
	public InputStream getInputStream(ResourceLocation location) throws IOException {
		if (location.getResourcePath().equals("textures/wiki/enchiridion_logo.png")) //special case the logo
			return BookResourcePack.class.getResourceAsStream("/assets/enchiridion/textures/books/enchiridion_logo.png");
		return FileUtils.openInputStream(getFile(location));
	}
	
	//If the texture folder is the one enchiridion uses itself, use the normal loading
	public boolean isValidLocation(ResourceLocation location) {
	    String path = location.getResourcePath();
	    if (path.startsWith("textures/books")) return false;
	    if (path.startsWith("images/enchiridion")) return false;
	    if (path.contains("enchiridion.json")) return false;
	    return true;
	}

	@Override
	public boolean resourceExists(ResourceLocation location) {
		if (!isValidLocation(location)) return false;
		String path = location.getResourcePath();
		if (path.startsWith("models") || path.startsWith("textures") || path.startsWith("images")) {
			File file = getFile(location);
			if (EConfig.debugMode && !path.equals("textures/wiki/enchiridion_logo.png")) ELogger.log(Level.INFO, "Checking for file at: " + file);
			if (file.exists() || path.equals("textures/wiki/enchiridion_logo.png")) {
				if (EConfig.debugMode) ELogger.log(Level.INFO, "Passed file exists check");
				return true;
			}
		}
		
		return false;
	}

	@Override
	public Set<String> getResourceDomains() {
		return domains;
	}

	@Override
	public <T extends IMetadataSection> T getPackMetadata(IMetadataSerializer p_135058_1_, String p_135058_2_) throws IOException {
		return null;
	}

	@Override
	public BufferedImage getPackImage() throws IOException {
		return TextureUtil.readBufferedImage(BookResourcePack.class.getResourceAsStream("/assets/enchiridion/textures/books/enchiridion_logo.png"));
	}

	@Override
	public String getPackName() {
		return "enchiridion";
	}
}
