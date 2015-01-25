package joshie.enchiridion.library;

import java.util.ArrayList;
import java.util.HashMap;

import joshie.enchiridion.api.IBookHandler;
import joshie.enchiridion.api.ILibraryHelper;
import joshie.enchiridion.helpers.StackHelper;
import joshie.enchiridion.library.handlers.DefaultBookHandler;
import joshie.enchiridion.library.handlers.NetworkSwitchHandler;
import joshie.enchiridion.library.handlers.SwitchBookHandler;
import net.minecraft.item.ItemStack;

public class LibraryRegistry implements ILibraryHelper {
    public static LibraryRegistry INSTANCE = new LibraryRegistry();
    private static HashMap<String, IBookHandler> handlers = new HashMap();
    private static ArrayList<ItemStack> books = new ArrayList();
    public static IBookHandler default_handler;
    public static IBookHandler switch_handler;
    public static IBookHandler network_handler;

    public void initRegistry() {
        default_handler = new DefaultBookHandler();
        switch_handler = new SwitchBookHandler();
        network_handler = new NetworkSwitchHandler();
    }

    @Override
    public void registerDefault(ItemStack stack) {
        register(stack, default_handler);
    }
    
    @Override
    public void registerSwitch(ItemStack stack) {
        register(stack, switch_handler);
    }
    
    @Override
    public void registerNetworkSwitch(ItemStack stack) {
        register(stack, network_handler);
    }

    @Override
    public void register(ItemStack stack, IBookHandler handler) {
        String key = StackHelper.getStringFromStack(stack);
        books.add(stack);
        handlers.put(key, handler);
    }
    
    public static ArrayList<ItemStack> getBooks() {
        return books;
    }

    public static IBookHandler getHandler(ItemStack stack) {
        String key = StackHelper.getStringFromStack(stack);
        IBookHandler handler = handlers.get(key);
        return handler == null? default_handler: handler;
    }

    public static void overwrite(ItemStack stack) {
        int index = 0;
        for(int i = 0; i < books.size(); i++) {
            if(stack.isItemEqual(books.get(i))) {
                index = i;
                break;
            }
        }
        
        books.set(index, stack);
    }
}
