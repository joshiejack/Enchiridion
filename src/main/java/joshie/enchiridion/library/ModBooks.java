package joshie.enchiridion.library;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

public class ModBooks {
    @Expose
    ArrayList<ModBookData> books = new ArrayList();

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
