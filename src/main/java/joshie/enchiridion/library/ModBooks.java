package joshie.enchiridion.library;

import java.util.ArrayList;

import joshie.enchiridion.helpers.StackHelper;
import net.minecraft.item.ItemStack;

import com.google.gson.annotations.Expose;

import cpw.mods.fml.common.Loader;

public class ModBooks {
    @Expose
    public ArrayList<ModBookData> books = new ArrayList();

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
        @Expose
        public boolean free;
        @Expose
        public boolean onCrafted;
        @Expose
        public String openGuiClass;
        @Expose
        public String openGuiNBT;
        @Expose
        public boolean pickUp;
        @Expose
        public String overwrite;

        public ItemStack item; //If this isn't null then this book is actively installed

        public ModBookData() {}

        public ModBookData(String mod, String stack, String register) {
            this.mod = mod;
            this.stack = stack;
            this.type = register;
            this.free = true;
            this.onCrafted = false;
            this.openGuiClass = "";
            this.openGuiNBT = "";
            this.pickUp = false;
            this.overwrite = "";
        }

        public ModBookData(String mod, String item, int meta, String register) {
            this(mod, mod + ":" + item + " " + meta, register);
        }

        public ModBookData setOverwrites(String overwrite) {
            this.free = false;
            this.overwrite = overwrite;
            return this;
        }

        public ModBookData setOpenGuiClass(String clazz) {
            this.free = false;
            this.openGuiClass = clazz;
            return this;
        }

        public ModBookData setOpenGuiNBT(String nbt) {
            this.free = false;
            this.openGuiNBT = nbt;
            return this;
        }
    }

    /** sets up the items with their actual item **/
    public ModBooks init() {
        for (ModBookData book : books) {
            if (Loader.isModLoaded(book.mod)) {
                try {
                    book.item = StackHelper.getStackFromString(book.stack);
                } catch (Exception e) {}
            }
        }

        return this;
    }

    /** Register books to the bookhandler registry, Called client side whenever a server
     * sends a new list of books, this is jsut so we know how they should be handled **/
    public void registerBooks() {
        for (ModBookData book : books) {
            if (book.item == null) continue;
            BookHandlerRegistry.registerBook(book.item, book.type);
        }
    }

    /** Default books in the json file **/
    public static ModBooks getModBooks(ModBooks data) {
        data.addBook(new ModBookData("AgriCraft", "journal", 0, "default"));
        data.addBook(new ModBookData("aura", "lexicon", 0, "switch"));
        data.addBook(new ModBookData("AWWayofTime", "itemBloodMagicBook", 0, "switch"));
        data.addBook(new ModBookData("Botania", "Botania:lexicon 0", "network"));
        data.addBook(new ModBookData("Botania", "Botania:lexicon 0 {knowledge.minecraft:1b,knowledge.alfheim:1b}", "network").setOpenGuiClass("vazkii.botania.client.gui.lexicon.BotaniaHijackLexiconIndex").setOpenGuiNBT("knowledge.alfheim").setOverwrites("Botania:lexicon 0"));
        data.addBook(new ModBookData("ChromatiCraft", "chromaticraft_item_help", 0, "default"));
        data.addBook(new ModBookData("factorization", "docbook", 0, "default"));
        data.addBook(new ModBookData("HardcoreQuesting", "quest_book", 0, "network"));
        data.addBook(new ModBookData("Mariculture", "guide", 0, "switch"));
        data.addBook(new ModBookData("Mariculture", "guide", 1, "switch"));
        data.addBook(new ModBookData("Mariculture", "guide", 2, "switch"));
        data.addBook(new ModBookData("Mariculture", "guide", 3, "switch"));
        data.addBook(new ModBookData("Mariculture", "guide", 4, "switch"));
        data.addBook(new ModBookData("OpenBlocks", "infoBook", 0, "network"));
        data.addBook(new ModBookData("Steamcraft", "book", 0, "default"));
        data.addBook(new ModBookData("TConstruct", "manualBook", 0, "default"));
        data.addBook(new ModBookData("TConstruct", "manualBook", 1, "default"));
        data.addBook(new ModBookData("TConstruct", "manualBook", 2, "default"));
        data.addBook(new ModBookData("TConstruct", "manualBook", 3, "default"));
        data.addBook(new ModBookData("Thaumcraft", "ItemThaumonomicon", 0, "network"));
        data.addBook(new ModBookData("witchery", "bookbiomes2", 0, "switch"));
        data.addBook(new ModBookData("witchery", "cauldronbook", 0, "switch"));
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
