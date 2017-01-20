package joshie.enchiridion.library;

import amerifrance.guideapi.api.GuideAPI;
import amerifrance.guideapi.api.impl.Book;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.data.library.ModdedBooks;
import joshie.enchiridion.data.library.ModdedBooks.ModdedBook;
import joshie.enchiridion.helpers.FileHelper;
import joshie.enchiridion.helpers.GsonHelper;
import joshie.enchiridion.helpers.StackHelper;
import joshie.enchiridion.lib.EInfo;
import net.minecraft.item.ItemStack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;

public class ModSupport {
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
                if (!stack.isEmpty()) {
                    if (book.getHandler().equals("customwood")) {
                        EnchiridionAPI.library.registerWood(stack, book.shouldMatchDamage(), book.shouldMatchNBT());
                    } else {
                        LibraryRegistry.INSTANCE.registerBookHandlerForStackFromJSON(book.getHandler(), stack, book.shouldMatchDamage(), book.shouldMatchNBT());
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }

    private static void setDefaults(String serverName) {
        books = new ModdedBooks(); //Create the books
        books.add("enchiridion", "enchiridion:book", true, false);
        books.add("writeable", "minecraft:writable_book", false, false);
        books.add("written", "minecraft:written_book", false, false);
        books.add("switchclick", "actuallyadditions:itemBooklet", false, false);
        books.add("switchclick", "deepresonance:dr_manual", false, false);
        books.add("switchclick", "environmentaltech:digital_guide", false, false);
        books.add("switchclick", "pokecube:pokedex", false, false);
        books.add("switchclick", "rftools:rftools_manual", false, false);
        books.add("switchclick", "rftoolscontrol:rftoolscontrol_manual", false, false);
        books.add("switchclick", "rftoolsdim:rftoolsdim_manual", false, false);
        books.add("switchclick", "theoneprobe:probenote", false, false);
        if (EInfo.IS_GUIDEAPI_LOADED) {
            for (Book book : GuideAPI.BOOKS) {
                books.add("copynbt", "guideapi:" + book.getRegistryName().toString().replace(":", "-"), false, false);
            }
        }
        books.add("customwood", "minecraft:planks 1", true, false);
        books.add("customwood", "minecraft:planks 5", true, false);
        books.add("customwood", "biomesoplenty:planks_0 14", true, false);

        //Not updated to 1.11 yet, so might not work
        books.add("switchclick", "aura:lexicon", false, false);
        books.add("switchclick", "botania:lexicon", false, false);
        books.add("switchclick", "extrautils2:Book", false, false);
        books.add("switchclick", "harvestfestival:book", false, false);
        books.add("switchclick", "harvestfestival:cookbook", false, false);
        books.add("switchclick", "openblocks:infoBook", false, false);
        books.add("switchclick", "opencomputers:tool 4", true, false);
        books.add("switchclick", "simpleachievements:achievement_book", false, false);
        books.add("switchclick", "tconstruct:book", false, false);
        books.add("switchclick", "thaumcraft:thaumonomicon", false, false);
        books.add("switchclick", "totemic:totempedia", false, false);
        books.add("switchclick", "villagebox:village_book", false, false);
        books.add("computercraft", "computercraft:printout", false, false);
        books.add("switchclick", "immersiveengineering:tool 3", true, false);
        books.add("warpbook", "warpbook:warpbook", false, false);
        books.add("customwood", "thaumcraft:plank 0", true, false);
        books.add("customwood", "chisel:planks-dark-oak", false, false);
        books.add("customwood", "chisel:planks-spruce", false, false);
        books.add("customwood", "chisel:livingwood-planks", false, false);
        books.add("customwood", "chisel:livingwood-raw", false, false);
        books.add("customwood", "chisel:thinWood-dark", false, false);
        books.add("customwood", "chisel:thinWood-spruce", false, false);
        books.add("customwood", "botania:livingwood", false, false);

        try {
            //Write the json
            String json = GsonHelper.getModifiedGson().toJson(books);
            File toSave = FileHelper.getLibraryFile(serverName);
            Writer writer = new OutputStreamWriter(new FileOutputStream(toSave), "UTF-8");
            writer.write(json);
            writer.close();
        } catch (Exception ignored) {
        }
    }

    public static ItemStack[] getFreeBooks() {
        return books.getFreeBooks();
    }

    private static final HashMap<String, ModdedBooks> CACHE = new HashMap<>();

    public static void reset() {
        CACHE.clear();
    }

    public static int getHashcode(String serverName) {
        if (CACHE.containsKey(serverName)) {
            return CACHE.get(serverName).hashCode();
        }

        loadDataFromJson(serverName, FileHelper.getLibraryJson(serverName)); //Load in any existing json files
        CACHE.put(serverName, books);
        return books.hashCode();
    }
}