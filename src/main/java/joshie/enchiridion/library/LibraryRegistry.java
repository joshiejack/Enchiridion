package joshie.enchiridion.library;

import java.util.HashMap;

import joshie.enchiridion.api.book.IBookHandler;
import joshie.enchiridion.api.library.ILibraryRegistry;
import joshie.lib.util.SafeStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class LibraryRegistry implements ILibraryRegistry {
    private HashMap<String, IBookHandler> handlers = new HashMap();
    private HashMap<SafeStack, IBookHandler> bookRegistry = new HashMap();
    
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
}
