package joshie.enchiridion.library;

import static cpw.mods.fml.common.Loader.isModLoaded;
import static joshie.enchiridion.api.EnchiridionHelper.bookRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModBooks {
    public static void init() {
        if (isModLoaded("Botania")) {

        }

        if (isModLoaded("HardcoreQuesting")) {
            Item book = GameRegistry.findItem("HardcoreQuesting", "quest_book");
            if (book != null) {
                bookRegistry.registerNetworkSwitch(new ItemStack(book));
            }
        }

        if (isModLoaded("factorization")) {
            Item book = GameRegistry.findItem("factorization", "docbook");
            if (book != null) {
                bookRegistry.registerDefault(new ItemStack(book, 1, 0));
            }
        }

        if (isModLoaded("Mariculture")) {
            Item book = GameRegistry.findItem("Mariculture", "guide");
            if (book != null) {
                bookRegistry.registerSwitch(new ItemStack(book, 1, 0));
                bookRegistry.registerSwitch(new ItemStack(book, 1, 1));
                bookRegistry.registerSwitch(new ItemStack(book, 1, 2));
                bookRegistry.registerSwitch(new ItemStack(book, 1, 3));
                bookRegistry.registerSwitch(new ItemStack(book, 1, 4));
            }
        }

        if (isModLoaded("TConstruct")) {
            Item book = GameRegistry.findItem("TConstruct", "manualBook");
            if (book != null) {
                bookRegistry.registerDefault(new ItemStack(book, 1, 0));
                bookRegistry.registerDefault(new ItemStack(book, 1, 1));
                bookRegistry.registerDefault(new ItemStack(book, 1, 2));
                bookRegistry.registerDefault(new ItemStack(book, 1, 3));
            }
        }

        if (isModLoaded("witchery")) {
            Item book = GameRegistry.findItem("witchery", "ingredient");
            if (book != null) {
                bookRegistry.registerSwitch(new ItemStack(book, 1, 46));
                bookRegistry.registerSwitch(new ItemStack(book, 1, 47));
                bookRegistry.registerSwitch(new ItemStack(book, 1, 48));
                bookRegistry.registerSwitch(new ItemStack(book, 1, 49));
                bookRegistry.registerSwitch(new ItemStack(book, 1, 81));
                bookRegistry.registerSwitch(new ItemStack(book, 1, 106));
                bookRegistry.registerSwitch(new ItemStack(book, 1, 107));
                bookRegistry.registerSwitch(new ItemStack(book, 1, 127));
            }
        }
    }
}
