package joshie.enchiridion.library;

import java.util.ArrayList;
import java.util.HashMap;

import joshie.enchiridion.api.IBookHandler;
import joshie.enchiridion.helpers.StackHelper;
import joshie.enchiridion.library.handlers.DefaultBookHandler;
import joshie.enchiridion.library.handlers.NetworkSwitchHandler;
import joshie.enchiridion.library.handlers.SwitchBookHandler;
import net.minecraft.item.ItemStack;

public class BookHandlerRegistry {
    //Only ever one instance of this
    private static final ArrayList<IBookHandler> bookHandlers = new ArrayList();
    private static final HashMap<String, IBookHandler> handlers = new HashMap();

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
    
    public static IBookHandler getHandlerForStack(ItemStack stack) {
        ItemStack removed_nbt = stack.copy();
        removed_nbt.stackTagCompound = null; //Remove the NBT
        String key = StackHelper.getStringFromStack(removed_nbt); //Grab the key
        IBookHandler bookHandler = handlers.get(key);
        return bookHandler != null? bookHandler: bookHandlers.get(0);
    }
    
    public static void registerBook(ItemStack stack, String handler) {
        ItemStack removed_nbt = stack.copy();
        removed_nbt.stackTagCompound = null; //Remove the NBT
        String key = StackHelper.getStringFromStack(removed_nbt); //Grab the key
        IBookHandler bookHandler = getHandlerFromString(handler);
        handlers.put(key, bookHandler);
    }

    public static IBookHandler getHandlerFromString(String key) {
        IBookHandler theHandler = null;
        for (IBookHandler handler : bookHandlers) {
            if (handler.getName().equals(key)) {
                theHandler = handler;
            }
        }

        return theHandler == null ? bookHandlers.get(0) : theHandler;
    }
}
