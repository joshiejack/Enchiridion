package joshie.enchiridion.library;

import joshie.enchiridion.api.book.IBookHandler;
import joshie.enchiridion.api.library.ILibraryRegistry;
import joshie.enchiridion.util.SafeStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

public class LibraryRegistry implements ILibraryRegistry {
    private HashMap<String, IBookHandler> handlers = new HashMap<   >();
    private HashMap<SafeStack, IBookHandler> bookHandlerRegistry = new HashMap<>();
    private HashMap<SafeStack, IBookHandler> allowedBookRegistry = new HashMap<>();

    public static final LibraryRegistry INSTANCE = new LibraryRegistry();

    private LibraryRegistry() {
    }

    public void unregisterBookHandlerForStackFromJSON(ItemStack itemStack, boolean matchDamage, boolean matchNBT) {
        SafeStack safeStack = SafeStack.newInstance("IGNORE", itemStack, "IGNORE", matchDamage, matchNBT);
        allowedBookRegistry.remove(safeStack); //Remove the book
    }

    public void registerBookHandlerForStackFromJSON(String handlerName, ItemStack itemStack, boolean matchDamage, boolean matchNBT) {
        SafeStack safeStack = SafeStack.newInstance("IGNORE", itemStack, "IGNORE", matchDamage, matchNBT);
        allowedBookRegistry.put(safeStack, handlers.get(handlerName));
    }

    @Override
    public void resetStacksAllowedInLibrary() {
        allowedBookRegistry = new HashMap<>();
        for (SafeStack stack : bookHandlerRegistry.keySet()) {
            allowedBookRegistry.put(stack, bookHandlerRegistry.get(stack));
        }
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
        bookHandlerRegistry.put(safeStack, handlers.get(handlerName));
    }

    @Override
    public IBookHandler getBookHandlerForStack(ItemStack stack) {
        if (stack == null) return null; //Oi back away!

        for (SafeStack safeStack : SafeStack.allInstances(stack)) {
            IBookHandler handler = allowedBookRegistry.get(safeStack);
            if (handler != null) return handler;
        }

        return null;
    }

    @Override
    public void registerWood(ItemStack stack, boolean matchDamage, boolean matchNBT) {
        LibraryRecipe.validWoods.add(SafeStack.newInstance("IGNORE", stack, "IGNORE", matchDamage, matchNBT));
    }
}