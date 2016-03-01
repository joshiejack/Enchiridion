package joshie.enchiridion.data.library;

import java.util.HashMap;

import joshie.enchiridion.api.book.IBookHandler;

public class BookHandlerRegistry {
    public static final BookHandlerRegistry INSTANCE = new BookHandlerRegistry();
    private HashMap<String, IBookHandler> handlers = new HashMap();
    
    private BookHandlerRegistry() {}
    
    public void registerBooHandlder(IBookHandler handler) {
        handlers.put(handler.getName(), handler);
    }
}
