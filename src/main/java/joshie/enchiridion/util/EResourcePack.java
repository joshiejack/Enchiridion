package joshie.enchiridion.util;

import com.google.common.collect.ImmutableSet;
import joshie.enchiridion.EConfig;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.helpers.FileHelper;
import joshie.enchiridion.lib.EInfo;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class EResourcePack implements IResourcePack {
    public static final EResourcePack INSTANCE = new EResourcePack();

    private EResourcePack() {
    }

    private static final Set<String> DOMAINS = ImmutableSet.of(EInfo.MODID);

    private File getFileLocationFromResource(ResourceLocation location) {
        String path = location.getResourcePath();
        String name = path.startsWith("templates") ? path.substring(10) : path.startsWith("images") ? path.substring(7) : path.startsWith("textures") ? path.substring(14) : path.substring(12);
        File directory = path.startsWith("templates") ? FileHelper.getTemplatesDirectory() : path.startsWith("images") ? FileHelper.getImagesDirectory() : FileHelper.getIconsDirectory();
        return new File(directory, name);
    }

    @Override
    @Nonnull
    public InputStream getInputStream(@Nonnull ResourceLocation location) throws IOException {
        if (location.getResourcePath().equals("textures/wiki/enchiridion_logo.png")) //special case the logo
            return EResourcePack.class.getResourceAsStream("/assets/enchiridion/textures/books/enchiridion_logo.png");
        return FileUtils.openInputStream(getFileLocationFromResource(location));
    }

    //If the texture folder is the one enchiridion uses itself, use the normal loading
    public boolean isValidLocation(ResourceLocation location) {
        String path = location.getResourcePath();
        return !path.startsWith("textures/books") && !path.contains("enchiridion.json") && !path.contains("library.json");
    }

    @Override
    public boolean resourceExists(@Nonnull ResourceLocation location) {
        if (!isValidLocation(location)) return false;
        String path = location.getResourcePath();
        if (path.startsWith("models") || path.startsWith("textures") || path.startsWith("images") || path.startsWith("templates")) {
            File file = getFileLocationFromResource(location);
            if (EConfig.debugMode && !path.equals("textures/wiki/enchiridion_logo.png"))
                Enchiridion.log(Level.INFO, "Checking for file at: " + file);
            if (file.exists() || path.equals("textures/wiki/enchiridion_logo.png")) {
                if (EConfig.debugMode) Enchiridion.log(Level.INFO, "Passed file exists check");
                return true;
            }
        }

        return false;
    }

    @Override
    @Nonnull
    public Set<String> getResourceDomains() {
        return DOMAINS;
    }

    @Override
    @Nonnull
    public <T extends IMetadataSection> T getPackMetadata(@Nonnull MetadataSerializer serializer, @Nonnull String string) throws IOException {
        return null;
    }

    @Override
    @Nonnull
    public BufferedImage getPackImage() throws IOException {
        return TextureUtil.readBufferedImage(EResourcePack.class.getResourceAsStream("/assets/enchiridion/textures/books/enchiridion_logo.png"));
    }

    @Override
    @Nonnull
    public String getPackName() {
        return EInfo.MODID;
    }
}