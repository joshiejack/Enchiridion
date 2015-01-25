package joshie.enchiridion.library;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import joshie.enchiridion.api.IBookHandler;
import joshie.enchiridion.api.ILibraryHelper;
import joshie.enchiridion.helpers.StackHelper;
import joshie.enchiridion.library.handlers.DefaultBookHandler;
import joshie.enchiridion.library.handlers.SwitchBookHandler;
import net.minecraft.item.ItemStack;

public class LibraryRegistry implements ILibraryHelper {
    public static LibraryRegistry INSTANCE = new LibraryRegistry();
    private static HashMap<String, IBookHandler> handlers = new HashMap();
    private static Collection<ItemStack> books = new ArrayList();
    private static IBookHandler default_handler = new DefaultBookHandler();
    private static IBookHandler switch_handler = new SwitchBookHandler();

    @Override
    public void registerDefault(ItemStack stack) {
        register(stack, default_handler);
    }
    
    @Override
    public void registerSwitch(ItemStack stack) {
        register(stack, switch_handler);
    }

    @Override
    public void register(ItemStack stack, IBookHandler handler) {
        String key = StackHelper.getStringFromStack(stack);
        books.add(stack);
        handlers.put(key, handler);
    }
    
    public static Collection<ItemStack> getBooks() {
        return books;
    }

    public static IBookHandler getHandler(ItemStack stack) {
        String key = StackHelper.getStringFromStack(stack);
        IBookHandler handler = handlers.get(key);
        return handler == null? default_handler: handler;
    }
}
