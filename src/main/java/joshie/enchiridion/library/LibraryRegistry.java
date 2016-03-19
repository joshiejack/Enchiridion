package joshie.enchiridion.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IBookHandler;
import joshie.enchiridion.api.library.ILibraryRegistry;
import joshie.enchiridion.gui.library.LibraryRecipe;
import joshie.enchiridion.helpers.ItemListHelper;
import joshie.enchiridion.util.SafeStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class LibraryRegistry implements ILibraryRegistry {
    private HashMap<String, IBookHandler> handlers = new HashMap();
    private HashMap<SafeStack, IBookHandler> bookRegistry = new HashMap();
    public static List<ItemStack> cache = new ArrayList();

    public static List<ItemStack> getBooksAsItemStack() {
        if (cache == null || cache.size() == 0) {
            cache = new ArrayList();
            for (ItemStack stack : ItemListHelper.allItems()) {
                if (EnchiridionAPI.library.getBookHandlerForStack(stack) != null) {
                    cache.add(stack);
                }
            }
        }

        return cache;
    }

    @Override
    public void resetStacksAllowedInLibrary() {
        bookRegistry = new HashMap();
    }

    @Override
    public void registerBookHandler(IBookHandler handler) {
        handlers.put(handler.getName(), handler);
    }

    @Override
    public IInventory getLibraryInventory(EntityPlayer player) {
        return LibraryHelper.getLibraryContents(player);
    }

    @Override
    public void registerBookHandlerForStack(String handlerName, ItemStack itemStack, boolean matchDamage, boolean matchNBT) {
        SafeStack safeStack = SafeStack.newInstance("IGNORE", itemStack, "IGNORE", matchDamage, matchNBT);
        bookRegistry.put(safeStack, handlers.get(handlerName));
    }

    @Override
    public IBookHandler getBookHandlerForStack(ItemStack stack) {
        if (stack == null) return null; //Oi back away!

        for (SafeStack safeStack : SafeStack.allInstances(stack)) {
            IBookHandler handler = bookRegistry.get(safeStack);
            if (handler != null) return handler;
        }

        return null;
    }

    @Override
    public void registerWood(ItemStack stack, boolean matchDamage, boolean matchNBT) {
        LibraryRecipe.validWoods.add(SafeStack.newInstance("IGNORE", stack, "IGNORE", matchDamage, matchNBT));
    }
}
