package joshie.enchiridion.helpers;

import java.io.File;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.books.Book;

public class FileHelper {
    public static File getDevAssetsForModPath(File source, String modid, String type) {
        return new File(new File(new File(new File(new File(new File(source, "src"), "main"), "resources"), "assets"), modid), type);
    }

    public static File getSourceFromConfigFolderInDev() {
        return Enchiridion.root.getParentFile().getParentFile().getParentFile();
    }

    public static File getBookSaveDirectory(Book data) {
        return new File(new File(Enchiridion.root, "books"), data.uniqueName + ".json");
    }
    
    public static File getImageSaveDirectory() {
    	File directory = new File(new File(new File(Enchiridion.root, "books"), "images"), EnchiridionAPI.draw.getBookSaveName());
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IllegalStateException("Couldn't create dir: " + directory);
        }
        
        return directory;
    }
}
