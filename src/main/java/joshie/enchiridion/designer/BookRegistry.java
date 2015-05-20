package joshie.enchiridion.designer;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import joshie.enchiridion.ELogger;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.helpers.ClientHelper;
import joshie.enchiridion.helpers.FileHelper;
import joshie.enchiridion.helpers.GsonClientHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.Language;
import net.minecraft.item.ItemStack;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;

import com.google.gson.annotations.Expose;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class BookRegistry {
    public static class BookData {
        @Expose
        public ArrayList<DesignerCanvas> book;
        @Expose
        public String uniqueName;
        @Expose
        public List information;
        @Expose
        public int color;
        @Expose
        public boolean showBackground = true;
        @Expose
        public boolean showArrows = true;
        @Expose
        public boolean showNumber = true;
        @Expose
        public String iconPass1 = "";
        @Expose
        public String iconPass2 = "";
        @Expose
        public boolean iconColorisePass1;
        @Expose
        public boolean iconColorisePass2;
        @Expose
        public int iconColorPass1;
        @Expose
        public int iconColorPass2;
        @Expose
        public String language = "en_US";
        @Expose
        public String displayName;
        @Expose
        public boolean displayInCreative = true;

        public BookData() {}

        public BookData(String unique, String display, List info, int color) {
            this.uniqueName = unique;
            this.displayName = display;
            this.information = info;
            this.color = color;
            this.book = new ArrayList();
            this.book.add(new DesignerCanvas());
            this.language = ClientHelper.getLang();
        }

        public BookData(String unique) {
            this(unique, unique, null, 0xFFFFFF);
        }

        public BookData(BookData data) {
            this.book = new ArrayList(data.book);
            this.uniqueName = data.uniqueName;
            this.color = data.color;
            this.showBackground = data.showBackground;
            this.showArrows = data.showArrows;
            this.showNumber = data.showNumber;
            this.iconPass1 = data.iconPass1;
            this.iconPass2 = data.iconPass2;
            this.iconColorisePass1 = data.iconColorisePass1;
            this.iconColorisePass2 = data.iconColorisePass2;
            this.iconColorPass1 = data.iconColorPass1;
            this.iconColorPass2 = data.iconColorPass2;
            this.language = ClientHelper.getLang();
            this.displayName = data.displayName;
            this.displayInCreative = data.displayInCreative;
            if (data.information != null) {
                this.information = new ArrayList(data.information);
            }
        }
    }

    //Loads in all the books from the config directory
    public static void init() {
        File directory = new File(Enchiridion.root, "books");
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IllegalStateException("Couldn't create dir: " + directory);
        }

        Collection<File> files = FileUtils.listFiles(directory, new String[] { "json" }, true);
        for (File file : files) {
            //Read all the json books from this directory
            try {
                BookRegistry.register(GsonClientHelper.getGson().fromJson(FileUtils.readFileToString(file), BookData.class));
            } catch (Exception e) {
                BookRegistry.register(new BookData(file.getName().replace(".json", "")));
            }
        }
    }

    public static void registerModInDev(String modid, File source) {
        File path = FileHelper.getDevAssetsForModPath(source.getParentFile(), modid, "books");
        if (!path.exists()) {
            path.mkdir();
        }
        
        Collection<File> files = FileUtils.listFiles(path, new String[] { "json" }, true);
        for (File file : files) {
            try {
                String json = FileUtils.readFileToString(file);
                BookData data = BookRegistry.register(GsonClientHelper.getGson().fromJson(json, BookData.class));
                ELogger.log(Level.INFO, "Successfully loaded in the book with the unique identifier: " + data.uniqueName + " for the language: " + data.language);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void registerModInJar(String modid, File jar) {
        try {
            ZipFile zipfile = new ZipFile(jar);
            Enumeration enumeration = zipfile.entries();
            while (enumeration.hasMoreElements()) {
                ZipEntry zipentry = (ZipEntry) enumeration.nextElement();
                String fileName = zipentry.getName();
                Path path1 = Paths.get(fileName);
                Path path2 = Paths.get("assets", modid, "books");

                if (path1.startsWith(path2)) {
                    try {
                        String json = IOUtils.toString(zipfile.getInputStream(zipentry));
                        BookData data = BookRegistry.register(GsonClientHelper.getGson().fromJson(json, BookData.class));
                        ELogger.log(Level.INFO, "Successfully loaded in the book with the unique identifier: " + data.uniqueName + " for the language: " + data.language);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            zipfile.close();
        } catch (Exception e) {}
    }

    private static final HashMap<ItemStack, String> stackToIdentifier = new HashMap();
    private static final HashMap<String, HashMap<String, BookData>> books = new HashMap();
    private static int lastID = 1;

    private static BookData getDataInFirstLanguage(String identifier) {
        Iterator it = Minecraft.getMinecraft().getLanguageManager().getLanguages().iterator();
        while (it.hasNext()) {
            String code = ((Language) it.next()).getLanguageCode();
            HashMap<String, BookData> data = books.get(code);
            if (data != null) {
                if (data.get(identifier) != null) return data.get(identifier);
            }
        }

        return null;
    }

    private static void markAllLanguagesAsNoCreative(String identifier) {
        Iterator it = Minecraft.getMinecraft().getLanguageManager().getLanguages().iterator();
        while (it.hasNext()) {
            String code = ((Language) it.next()).getLanguageCode();
            HashMap<String, BookData> data = books.get(code);
            if (data != null) {
                if (data.get(identifier) != null) {
                    data.get(identifier).displayInCreative = false;
                }
            }
        }
    }

    public static void registerItemStack(String identifier, ItemStack stack) {
        if (stack == null) ELogger.log(Level.WARN, "A book with the identifier " + identifier + " could not be registered as the stack was null");
        else if (getDataInFirstLanguage(identifier) == null) {
            ELogger.log(Level.WARN, "A book with the identifier " + identifier + " could not be found in any language");
        } else {
            stackToIdentifier.put(stack, identifier);
            markAllLanguagesAsNoCreative(identifier);
            ELogger.log(Level.WARN, "Successfully registered " + stack.getDisplayName() + " as a book that will open the identifier " + identifier);
        }
    }

    public static String getID(ItemStack stack) {
        if (stack == null || !stack.hasTagCompound()) return null;
        String identifier = stack.stackTagCompound.getString("identifier");
        return identifier;
    }

    public static boolean opensGui(ItemStack stack) {
        for (ItemStack check : stackToIdentifier.keySet()) {
            if (check.isItemEqual(stack)) {
                return true;
            }
        }

        return false;
    }

    public static BookData getData(ItemStack stack) {
        String identifier = getID(stack);
        if (identifier == null) {
            for (ItemStack check : stackToIdentifier.keySet()) {
                if (check.isItemEqual(stack)) {
                    identifier = stackToIdentifier.get(check);
                    break;
                }
            }
        }

        return getData(identifier);
    }

    public static BookData getData(String unique) {
        String language = ClientHelper.getLang();       
        HashMap<String, BookData> data = books.get(language);
        BookData book = null;
        if (data != null) book = data.get(unique);
        if (book == null) book = getDataInFirstLanguage(unique);
        return book;
    }

    public static BookData register(BookData data) {
        String language = data.language;
        if (language == null || language.equals("")) language = ClientHelper.getLang();
        HashMap<String, BookData> map = books.get(language);
        if (map == null) map = new HashMap();
        if (map.get(data.uniqueName) != null) return map.get(data.uniqueName);
        data = new BookData(data);
        map.put(data.uniqueName, data);
        books.put(language, map);
        return data;
    }

    public static Set<String> getIDs() {
        HashSet<String> data = new HashSet();
        for (HashMap<String, BookData> map : books.values()) {
            data.addAll(map.keySet());
        }

        return data;
    }
}
