package joshie.enchiridion.api.library;

import joshie.enchiridion.api.book.IBookHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface ILibraryRegistry {
    /** Called when entering a new world **/
    void resetStacksAllowedInLibrary();

    /** Register a method for handling the opening of books
     *  This method should be called on the client and the server **/
    void registerBookHandler(IBookHandler handler);

    /** Register this book for being handled
     *  Ensure you call this after preInit, and register book handlers in preInit **/
    void registerBookHandlerForStack(String handlerName, @Nonnull ItemStack stack, boolean matchNBT);

    /** Gets the book handler instance for this stack **/
    IBookHandler getBookHandlerForStack(@Nonnull ItemStack stack);

    /** @return Returns the IInventory for this players library **/
    IInventory getLibraryInventory(PlayerEntity player);

    /** Register wood as being able to be used to craft the library **/
    void registerWood(@Nonnull ItemStack stack, boolean matchNBT);
}