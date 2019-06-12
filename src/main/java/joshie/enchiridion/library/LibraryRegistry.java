package joshie.enchiridion.library;

import joshie.enchiridion.api.book.IBookHandler;
import joshie.enchiridion.api.library.ILibraryRegistry;
import joshie.enchiridion.util.SafeStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class LibraryRegistry implements ILibraryRegistry {
    private HashMap<String, IBookHandler> handlers = new HashMap<>();
    private HashMap<SafeStack, IBookHandler> bookHandlerRegistry = new HashMap<>();
    private HashMap<SafeStack, IBookHandler> allowedBookRegistry = new HashMap<>();

    public static final LibraryRegistry INSTANCE = new LibraryRegistry();

    private LibraryRegistry() {
    }

    public void unregisterBookHandlerForStackFromJSON(@Nonnull ItemStack stack, boolean matchNBT) {
        SafeStack safeStack = SafeStack.newInstance("IGNORE", stack, matchNBT);
        allowedBookRegistry.remove(safeStack); //Remove the book
    }

    public void registerBookHandlerForStackFromJSON(String handlerName, @Nonnull ItemStack stack, boolean matchNBT) {
        SafeStack safeStack = SafeStack.newInstance("IGNORE", stack, matchNBT);
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
    public IInventory getLibraryInventory(PlayerEntity player) {
        return LibraryHelper.getLibraryContents(player);
    }

    @Override
    public void registerBookHandlerForStack(String handlerName, @Nonnull ItemStack stack, boolean matchNBT) {
        SafeStack safeStack = SafeStack.newInstance("IGNORE", stack, matchNBT);
        bookHandlerRegistry.put(safeStack, handlers.get(handlerName));
    }

    @Override
    public IBookHandler getBookHandlerForStack(@Nonnull ItemStack stack) {
        if (stack.isEmpty()) return null; //Oi back away!

        for (SafeStack safeStack : SafeStack.allInstances(stack)) {
            IBookHandler handler = allowedBookRegistry.get(safeStack);
            if (handler != null) return handler;
        }

        return null;
    }

    @Override
    public void registerWood(@Nonnull ItemStack stack, boolean matchNBT) {
        LibraryRecipe.VALID_WOODS.add(SafeStack.newInstance("IGNORE", stack, matchNBT));
    }
}