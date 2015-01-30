package joshie.enchiridion.library;

import static joshie.enchiridion.api.EnchiridionHelper.bookRegistry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.helpers.StackHelper;
import joshie.enchiridion.wiki.WikiHelper;
import net.minecraft.item.ItemStack;

import org.apache.commons.io.FileUtils;

import com.google.gson.annotations.Expose;

import cpw.mods.fml.common.Loader;

public class ModBooks {
    @Expose
    private ArrayList<ModBookData> books = new ArrayList();

    public ModBooks addBook(ModBookData book) {
        books.add(book);
        return this;
    }

    public static class ModBookData {
        @Expose
        public String mod;
        @Expose
        public String stack;
        @Expose
        public String type;

        public ModBookData() {}
        public ModBookData(String mod, String item, int meta, String register) {
            this.mod = mod;
            this.stack = mod + ":" + item + " " + meta;
            this.type = register;
        }

        public ModBookData(String mod, ItemStack stack, String register) {
            this.mod = mod;
            this.stack = StackHelper.getStringFromStack(stack);
            this.type = register;
        }
    }

    public static void init() {
        try {
            ModBooks data = null;
            File default_file = new File(Enchiridion.root, "library/default.json");
            if (!default_file.exists()) {
                data = getModBooks(new ModBooks());

                File parent = default_file.getParentFile();
                if (!parent.exists() && !parent.mkdirs()) {
                    throw new IllegalStateException("Couldn't create dir: " + parent);
                }

                Writer writer = new OutputStreamWriter(new FileOutputStream(default_file), "UTF-8");
                writer.write(WikiHelper.getGson().toJson(data));
                writer.close();
            } else {
                String json = FileUtils.readFileToString(default_file);
                data = WikiHelper.getGson().fromJson(json, ModBooks.class);
            }

            //Now that we have the book data let's go through and register them
            for (ModBookData book : data.books) {
                if (Loader.isModLoaded(book.mod)) {
                    ItemStack item = StackHelper.getStackFromString(book.stack);
                    if (item != null && item.getItem() != null) {
                        if (book.type.equals("default")) {
                            bookRegistry.registerDefault(item);
                        } else if (book.type.equals("network")) {
                            bookRegistry.registerNetworkSwitch(item);
                        } else if (book.type.equals("switch")) {
                            bookRegistry.registerSwitch(item);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Default books in the json file **/
    public static ModBooks getModBooks(ModBooks data) {
        data.addBook(new ModBookData("AWWayofTime", "itemBloodMagicBook", 0, "default"));
        data.addBook(new ModBookData("Botania", "lexicon", 0, "switch"));
        data.addBook(new ModBookData("HardcoreQuesting", "quest_book", 0, "network"));
        data.addBook(new ModBookData("factorization", "docbook", 0, "default"));
        data.addBook(new ModBookData("Mariculture", "guide", 0, "switch"));
        data.addBook(new ModBookData("Mariculture", "guide", 1, "switch"));
        data.addBook(new ModBookData("Mariculture", "guide", 2, "switch"));
        data.addBook(new ModBookData("Mariculture", "guide", 3, "switch"));
        data.addBook(new ModBookData("Mariculture", "guide", 4, "switch"));
        data.addBook(new ModBookData("OpenBlocks", "infoBook", 0, "default"));
        data.addBook(new ModBookData("TConstruct", "manualBook", 0, "default"));
        data.addBook(new ModBookData("TConstruct", "manualBook", 1, "default"));
        data.addBook(new ModBookData("TConstruct", "manualBook", 2, "default"));
        data.addBook(new ModBookData("TConstruct", "manualBook", 3, "default"));
        data.addBook(new ModBookData("Thaumcraft", "ItemThaumonomicon", 0, "default"));
        data.addBook(new ModBookData("witchery", "ingredient", 46, "switch"));
        data.addBook(new ModBookData("witchery", "ingredient", 47, "switch"));
        data.addBook(new ModBookData("witchery", "ingredient", 48, "switch"));
        data.addBook(new ModBookData("witchery", "ingredient", 49, "switch"));
        data.addBook(new ModBookData("witchery", "ingredient", 81, "switch"));
        data.addBook(new ModBookData("witchery", "ingredient", 106, "switch"));
        data.addBook(new ModBookData("witchery", "ingredient", 107, "switch"));
        data.addBook(new ModBookData("witchery", "ingredient", 127, "switch"));
        return data;
    }
}
