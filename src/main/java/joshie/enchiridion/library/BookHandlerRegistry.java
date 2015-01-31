package joshie.enchiridion.library;

import java.util.ArrayList;

import joshie.enchiridion.api.IBookHandler;
import joshie.enchiridion.helpers.StackHelper;
import joshie.enchiridion.library.handlers.DefaultBookHandler;
import joshie.enchiridion.library.handlers.NetworkSwitchHandler;
import joshie.enchiridion.library.handlers.SwitchBookHandler;
import net.minecraft.item.ItemStack;

public class BookHandlerRegistry {
    //Only ever one instance of this
    private static final ArrayList<IBookHandler> bookHandlers = new ArrayList();

    //Called to setup the default bookhandlers
    public static void initRegistry() {
        registerHandler(new DefaultBookHandler());
        registerHandler(new SwitchBookHandler());
        registerHandler(new NetworkSwitchHandler());
    }

    /** Register a new book handler **/
    public static void registerHandler(IBookHandler handler) {
        for (IBookHandler h : bookHandlers) {
            if (h.getName().equals(handler.getName())) {
                return;
            }
        }

        bookHandlers.add(handler);
    }

    /** Returns the approparite bookhandler for this itemstack **/
    public static IBookHandler getHandler(ItemStack stack) {
        ItemStack removed_nbt = stack.copy();
        removed_nbt.stackTagCompound = null;
        //Remove the nbt to get the correct handling for this book type
        String key = StackHelper.getStringFromStack(removed_nbt);
        IBookHandler theHandler = null;
        for (IBookHandler handler : bookHandlers) {
            if (handler.getName().equals(LibraryDataClient.storage.getHandler(key))) {
                theHandler = handler;
            }
        }

        return theHandler == null ? bookHandlers.get(0) : theHandler;
    }
}
