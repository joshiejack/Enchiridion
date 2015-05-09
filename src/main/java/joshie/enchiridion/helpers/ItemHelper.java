package joshie.enchiridion.helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import joshie.enchiridion.ELogger;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.apache.logging.log4j.Level;

public class ItemHelper {
    private static final ArrayList<ItemStack> items = new ArrayList();
    private static final ArrayList<ItemStack> allItems = new ArrayList();

    public static void init() {
        Iterator iterator = Item.itemRegistry.iterator();
        while (iterator.hasNext()) {
            Item item = (Item) iterator.next();

            if (item == null) {
                continue;
            }

            if (item.getCreativeTabs() != null && item.getCreativeTabs().length > 0) {
                for (CreativeTabs tab : item.getCreativeTabs()) {
                    try {
                        item.getSubItems(item, tab, items);
                    } catch (Exception e) {
                        ELogger.log(Level.ERROR, "Enchiridion had an issue when trying to load the item: " + item.getClass());
                    }
                }
            }
        }
        
        allItems.addAll(items);
    }
    
    public static List<ItemStack> items() {
        return items;
    }

    public static List<ItemStack> allItems() {
        return allItems;
    }

    public static void addInventory() {
        for (ItemStack stack : ClientHelper.getPlayer().inventory.mainInventory) {
            if (stack != null) {
                if (!allItems.contains(stack)) {
                    allItems.add(stack);
                }
            }
        }
    }
}
