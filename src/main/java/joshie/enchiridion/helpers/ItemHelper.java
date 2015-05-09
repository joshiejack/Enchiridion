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
    private static ArrayList<ItemStack> items = null;
    private static ArrayList<ItemStack> allItems = null;

    public static List<ItemStack> init() {
        items = new ArrayList();
        allItems = new ArrayList();
        
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
        return items;
    }
    
    public static List<ItemStack> items() {
        return items != null? items: init();
    }

    public static List<ItemStack> allItems() {
        return allItems != null? allItems: init();
    }

    public static void addInventory() {
        for (ItemStack stack : ClientHelper.getPlayer().inventory.mainInventory) {
            if (stack != null) {
                if (!allItems().contains(stack)) {
                    allItems.add(stack);
                }
            }
        }
    }
}
