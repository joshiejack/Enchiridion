package joshie.enchiridion.api.library;

import joshie.enchiridion.api.book.IBookHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface ILibraryRegistry {
    /** Called when entering a new world **/
    public void resetStacksAllowedInLibrary();
    
    /** Register a method for handling the opening of books
     *  This method should be called on the client and the server **/
    public void registerBookHandler(IBookHandler handler);
    
    /** Register this book for being handled
     *  Ensure you call this after preinit, and register book handlers in preinit **/
    public void registerBookHandlerForStack(String handlerName, ItemStack stack, boolean matchDamage, boolean matchNBT);
    
    /** Gets the book handler instance for this stack **/
    public IBookHandler getBookHandlerForStack(ItemStack stack);
    
    /** Returns the IInventory for this players libraryy **/
    public IInventory getLibraryInventory(EntityPlayer player);
}
