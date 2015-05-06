package joshie.enchiridion.helpers;

import java.io.File;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.designer.BookRegistry.BookData;

public class FileHelper {
    public static File getDevAssetsForModPath(File source, String modid, String type) {
        return new File(new File(new File(new File(new File(new File(source, "src"), "main"), "resources"), "assets"), modid), type);
    }

    public static File getSourceFromConfigFolderInDev() {
        return Enchiridion.root.getParentFile().getParentFile().getParentFile();
    }

    public static File getBookSaveDirectory(BookData data) {
        return new File(new File(Enchiridion.root, "books"), data.uniqueName + ".json");
    }
}
