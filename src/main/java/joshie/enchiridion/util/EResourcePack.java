package joshie.enchiridion.util;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import joshie.enchiridion.EConfig;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.helpers.FileHelper;
import joshie.enchiridion.lib.EInfo;
import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.resources.data.IMetadataSectionSerializer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;

public class EResourcePack implements IResourcePack {
    public static final EResourcePack INSTANCE = new EResourcePack();

    private EResourcePack() {
    }

    private static final Set<String> DOMAINS = ImmutableSet.of(EInfo.MODID);

    private File getFileLocationFromResource(ResourceLocation location) {
        String path = location.getPath();
        String name = path.startsWith("templates") ? path.substring(10) : path.startsWith("images") ? path.substring(7) : path.startsWith("textures") ? path.substring(14) : path.substring(12);
        File directory = path.startsWith("templates") ? FileHelper.getTemplatesDirectory() : path.startsWith("images") ? FileHelper.getImagesDirectory() : FileHelper.getIconsDirectory();
        return new File(directory, name);
    }

    @Override
    @Nonnull
    public InputStream getRootResourceStream(@Nonnull String fileName) throws IOException {
        return FileUtils.openInputStream(getFileLocationFromResource(new ResourceLocation(fileName)));
    }

    @Override
    @Nonnull
    public InputStream getResourceStream(@Nonnull ResourcePackType type, @Nonnull ResourceLocation location) throws IOException {
        if (location.getPath().equals("textures/wiki/enchiridion_logo.png")) //special case the logo
            return EResourcePack.class.getResourceAsStream("/assets/enchiridion/textures/books/enchiridion_logo.png");
        return FileUtils.openInputStream(getFileLocationFromResource(location));
    }

    @Override
    @Nonnull
    public Collection<ResourceLocation> getAllResourceLocations(@Nonnull ResourcePackType type, @Nonnull String path, int maxDepth, @Nonnull Predicate<String> filter) {
        return Sets.newHashSet();
    }

    @Override
    public boolean resourceExists(@Nonnull ResourcePackType type, @Nonnull ResourceLocation location) {
        if (!isValidLocation(location)) return false;
        String path = location.getPath();
        if (path.startsWith("models") || path.startsWith("textures") || path.startsWith("images") || path.startsWith("templates")) {
            File file = getFileLocationFromResource(location);
            if (EConfig.SETTINGS.debugMode && !path.equals("textures/wiki/enchiridion_logo.png"))
                Enchiridion.log(Level.INFO, "Checking for file at: " + file);
            if (file.exists() || path.equals("textures/wiki/enchiridion_logo.png")) {
                if (EConfig.SETTINGS.debugMode) Enchiridion.log(Level.INFO, "Passed file exists check");
                return true;
            }
        }
        return false;
    }

    //If the texture folder is the one enchiridion uses itself, use the normal loading
    public boolean isValidLocation(ResourceLocation location) {
        String path = location.getPath();
        return !path.startsWith("textures/books") && !path.contains("enchiridion.json") && !path.contains("library.json");
    }

    @Override
    @Nonnull
    public Set<String> getResourceNamespaces(@Nonnull ResourcePackType type) {
        return DOMAINS;
    }

    @Override
    @Nullable
    public <T> T getMetadata(@Nonnull IMetadataSectionSerializer<T> deserializer) {
        return null;
    }

    /*@Override
    @Nonnull
    public BufferedImage getPackImage() throws IOException {
        return TextureUtil.readBufferedImage(EResourcePack.class.getResourceAsStream("/assets/enchiridion/textures/books/enchiridion_logo.png"));
    }*/

    @Override
    @Nonnull
    public String getName() {
        return EInfo.MODID;
    }

    @Override
    public void close() {
    }
}