package joshie.enchiridion.helpers;

import java.io.File;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IBook;
import joshie.enchiridion.lib.EInfo;

public class FileHelper {
    public static File getConfigFile() {
        return new File(Enchiridion.root, EInfo.MODID + ".cfg");
    }
    
    public static File getDevAssetsForModPath(File source, String modid, String type) {
        return new File(new File(new File(new File(new File(new File(source, "src"), "main"), "resources"), "assets"), modid), type);
    }

    public static File getBooksDirectory() {
        File directory = new File(Enchiridion.root, "books");
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IllegalStateException("Couldn't create dir: " + directory);
        }
        
        return directory;
    }
    
    public static File getIconsDirectory() {
        File directory = new File(getBooksDirectory(), "icons");
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IllegalStateException("Couldn't create dir: " + directory);
        }
        
        return directory;
    }
    
    public static File getIconsJSONForBook(IBook book) {
        return new File(getIconsDirectory(), book.getSaveName() + ".json");
    }
    
    public static File getSaveJSONForBook(IBook book) {
        return new File(getBooksDirectory(), book.getSaveName() + ".json");
    }
    
    public static File getImagesDirectory() {
        File directory = new File(getBooksDirectory(), "images");
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IllegalStateException("Couldn't create dir: " + directory);
        }
        
        return directory;
    }
    
    public static File getImageSaveDirectory() {
    	File directory = new File(getImagesDirectory(), EnchiridionAPI.book.getBook().getSaveName());
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IllegalStateException("Couldn't create dir: " + directory);
        }
        
        return directory;
    }
}
