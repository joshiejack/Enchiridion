package joshie.enchiridion.library;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.data.library.ModdedBooks;
import joshie.enchiridion.data.library.ModdedBooks.ModdedBook;
import joshie.enchiridion.helpers.FileHelper;
import joshie.enchiridion.helpers.GsonHelper;
import joshie.enchiridion.helpers.StackHelper;
import net.minecraft.item.ItemStack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;

public class ModSupport {
    public static HashSet<String> supported = new HashSet();
    private static ModdedBooks books;

    public static void loadDataFromJson(String serverName, String json) {
        if (json == null) {
            setDefaults(serverName);
        } else books = GsonHelper.getModifiedGson().fromJson(json, ModdedBooks.class);
        //Now that we have loaded in the data we should convert it
        EnchiridionAPI.library.resetStacksAllowedInLibrary();
        for (ModdedBook book : books.getList()) {
            try {
                ItemStack stack = StackHelper.getStackFromString(book.getItem());
                if (stack != null) {
                    if (book.getHandler().equals("customwood")) {
                        EnchiridionAPI.library.registerWood(stack, book.shouldMatchDamage(), book.shouldMatchNBT());
                    } else EnchiridionAPI.library.registerBookHandlerForStack(book.getHandler(), stack, book.shouldMatchDamage(), book.shouldMatchNBT());
                }
            } catch (Exception e) {}
        }
    }

    private static void setDefaults(String serverName) {
        books = new ModdedBooks(); //Create the books
        books.add("enchiridion", "enchiridion:book", true, false);
        books.add("writeable", "minecraft:writable_book", false, false);
        books.add("rightclick", "minecraft:written_book", false, false);
        books.add("rightclick", "Thaumcraft:thaumonomicon", false, false);
        books.add("rightclick", "Botania:lexicon", false, false);
        books.add("rightclick", "aura:lexicon", false, false);
        books.add("rightclick", "totemic:totempedia", false, false);
        books.add("rightclick", "openblocks:infoBook", false, false);
        books.add("rightclick", "opencomputers:tool 4", true, false);
        books.add("rightclick", "rftools:rfToolsManualItem", false, false);
        books.add("rightclick", "deepresonance:dr_manual", false, false);
        books.add("computercraft", "computercraft:printout", false, false);
        books.add("switchclick", "ImmersiveEngineering:tool 3", true, false);
        books.add("warpbook", "warpbook:warpbook", false, false);
        books.add("customwood", "minecraft:planks 1", true, false);
        books.add("customwood", "minecraft:planks 5", true, false);
        books.add("customwood", "thaumcraft:plank 0", true, false);
        books.add("customwood", "chisel:planks-dark-oak", false, false);
        books.add("customwood", "chisel:planks-spruce", false, false);
        books.add("customwood", "chisel:livingwood-planks", false, false);
        books.add("customwood", "chisel:livingwood-raw", false, false);
        books.add("customwood", "chisel:thinWood-dark", false, false);
        books.add("customwood", "chisel:thinWood-spruce", false, false);
        books.add("customwood", "biomesoplenty:planks_0 14", true, false);
        books.add("customwood", "Botania:livingwood", false, false);

        try {
            //Write the json
            String json = GsonHelper.getModifiedGson().toJson(books);
            File toSave = FileHelper.getLibraryFile(serverName);
            Writer writer = new OutputStreamWriter(new FileOutputStream(toSave), "UTF-8");
            writer.write(json);
            writer.close();
        } catch (Exception e) {}
    }

    public static ItemStack[] getFreeBooks() {
        return books.getFreeBooks();
    }

    private static HashMap<String, ModdedBooks> cache = new HashMap();

    public static void reset() {
        cache.clear();
    }

    public static int getHashcode(String serverName) {
        if (cache.containsKey(serverName)) {
            return cache.get(serverName).hashCode();
        }

        loadDataFromJson(serverName, FileHelper.getLibraryJson(serverName)); //Load in any existing json files
        cache.put(serverName, books);
        return books.hashCode();
    }
}
