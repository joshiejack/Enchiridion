package enchiridion;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;

import enchiridion.api.StackHelper;

public class BookBinderHelper {
	public static boolean spawn_binder;
	public static final ItemStack[] preload_books = new ItemStack[21];
	
	private static final Set<ItemStack> blacklisted_books = new HashSet();
	private static final Set<ItemStack> accepted_books = new HashSet();
	
    public static final String[] DFT_STRING_REMOVALS = new String[] { };
	public static final String[] DFT_STACK_REMOVALS = new String[] { "minecraft:bookshelf" };
	public static final String[] DFT_STACK_ADDITIONS = new String[] { 
    	"witchery:ingredient 46", "witchery:ingredient 47", "witchery:ingredient 48", "witchery:ingredient 49", "witchery:ingredient 81", 
        "witchery:ingredient 106", "witchery:ingredient 107", "witchery:ingredient 127", "OpenComputers:item 98" };
	public static final String[] DFT_STRING_ADDITIONS = new String[] { "guide", "book", "manual", "pedia", "thaumonomicon", "mudora", "lexicon", "compendium" };
	
	private static boolean matches(ItemStack stack, ItemStack stack2) {
		if(stack.isItemEqual(stack2)) return true;
		else if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE) return stack.getItem() == stack2.getItem();
		else return false;
	}
	
	//Adds a book to the blacklist
	public static boolean blacklistBook(ItemStack stack) {
		if(stack == null || stack.getItem() == null) {
			return false;
		}
		
		if(Config.DEBUG_ENABLED) {
			BookLogHandler.log(Level.INFO, "Blacklisting " + stack.getDisplayName() + " from the book binder");
		}
		
		blacklisted_books.add(stack);
		return true;
	}
	
	//Checks the blacklisted list before adding a book to be accepted in the book binder
	public static boolean registerBook(ItemStack stack) {
		if(stack == null || stack.getItem() == null) {
			return false;
		}
		
		try {
		    stack.getDisplayName();
		} catch (Exception e) { return false; }
		
		for(ItemStack check: blacklisted_books) {
			if(matches(stack, check)) return false;
		}
		
		if(Config.DEBUG_ENABLED) {
			BookLogHandler.log(Level.INFO, "Registered " + stack.getDisplayName() + " to be accepted by the book binder");
		}
		
		accepted_books.add(stack);
		return true;
	}

	//Loops through all of the strings and adds them as wildcard entries
	public static void blacklistBooks(String[] str) {
		Set<String> map = Item.itemRegistry.getKeys();
		for(String s: str) {
			for(String key: map) {
				if(key.toLowerCase().contains(s.toLowerCase())) {
					Item item = (Item) Item.itemRegistry.getObject(key);
					if(item != null) {
						blacklistBook(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE));
					}
				}
			}
		}
	}
	
	//Loops through all of the strings and adds them as wildcard entries
	public static void registerBooks(String[] str) {
		Set<String> map = Item.itemRegistry.getKeys();
		for(String s: str) {
			for(String key: map) {
				
				if(key.toLowerCase().contains(s.toLowerCase())) {
					Item item = (Item) Item.itemRegistry.getObject(key);
					if(item != null) {
						registerBook(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE));
					}
				}
			}
		}
	}
	
	//Adds ItemStacks to the Blacklist
	public static void blacklistBook(String[] items) {
		try {
			for(int i = 0; i < items.length; i++) {
				blacklistBook(StackHelper.getStackFromString(items[i]));
			}
		} catch (Exception e) {
			BookLogHandler.log(Level.WARN, "Invalid Itemstack in blacklisting books list");
			if(Config.DEBUG_ENABLED) {
				e.printStackTrace();
			}
		}
	}
	
	//Adds ItemStacks to the RegisteredBooks
	public static void registerBook(String[] items) {
		try {
			for(int i = 0; i < items.length; i++) {				
				registerBook(StackHelper.getStackFromString(items[i]));
			}
		} catch (Exception e) {
			BookLogHandler.log(Level.WARN, "Invalid Itemstack in register books list");
			if(Config.DEBUG_ENABLED) {
				e.printStackTrace();
			}
		}
	}

	public static void setPreloadedBooks(String[] items) {
		try {
			for(int i = 0; i < items.length; i++) {
				preload_books[i] = StackHelper.getStackFromString(items[i]);
			}
		} catch (Exception e) {
			BookLogHandler.log(Level.WARN, "Invalid Itemstack in preload books list");
			if(Config.DEBUG_ENABLED) {
				e.printStackTrace();
			}
		}
	}

	//Whether the book is in the registry depends on this
	public static boolean isBook(ItemStack stack) {
		for(ItemStack check: accepted_books) {
			if(check.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
				if(check.getItem() == stack.getItem()) return true;
			} else if(check.isItemEqual(stack)) return true;
		}
		
		return stack.getItem() instanceof ItemEnchiridion && stack.getItemDamage() == ItemEnchiridion.GUIDE;
	}
}
