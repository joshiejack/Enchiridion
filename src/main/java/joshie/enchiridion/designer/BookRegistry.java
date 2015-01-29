package joshie.enchiridion.designer;

import static java.io.File.separator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import joshie.enchiridion.ELogger;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.wiki.WikiHelper;
import net.minecraft.item.ItemStack;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;

import com.google.gson.annotations.Expose;

public class BookRegistry {
    public static class BookData {
        @Expose
        public ArrayList<DesignerCanvas> book;
        @Expose
        public HashMap<String, String> displayNames;
        @Expose
        public String uniqueName;
        @Expose
        public List information;
        @Expose
        public int color;
        @Expose
        public boolean bookBackground = true;

        public BookData(String unique, String en_US, List info, int color) {
            this.uniqueName = unique;
            this.displayNames = new HashMap();
            this.displayNames.put("en_US", en_US);
            this.information = info;
            this.color = color;
            this.book = new ArrayList();
        }
    }

    public static void init() {
        File directory = new File(Enchiridion.root + separator + "books");
        if(!directory.exists() && !directory.mkdirs()) {
            throw new IllegalStateException("Couldn't create dir: " + directory);
        }
        
        Collection<File> files = FileUtils.listFiles(directory, new String[] { "json" }, true);
        for (File file : files) {
            //Read all the json books from this directory
            try {
                BookRegistry.register(WikiHelper.getGson().fromJson(FileUtils.readFileToString(file), BookData.class));
            } catch (Exception e) {
                ELogger.log(Level.ERROR, "Failed to load book @ : " + file.toString());
            }
        }
    }

    private static final HashMap<String, BookData> books = new HashMap();
    private static int lastID = 1;

    public static String getID(ItemStack stack) {
        if (stack == null || !stack.hasTagCompound()) return null;
        String identifier = stack.stackTagCompound.getString("identifier");
        return identifier;
    }

    public static BookData getData(ItemStack stack) {
        String identifier = getID(stack);
        if (identifier == null) return null;
        return getData(identifier);
    }

    public static BookData getData(String unique) {
        return books.get(unique);
    }

    public static void register(BookData data) {
        books.put(data.uniqueName, data);
    }

    public static Set<String> getIDs() {
        return books.keySet();
    }
}
