package joshie.enchiridion.helpers;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemHelper {
    public static final ArrayList<ItemStack> items = new ArrayList();
    
    public static void init() {
        Iterator iterator = Item.itemRegistry.iterator();
        while (iterator.hasNext()) {
            Item item = (Item) iterator.next();

            if (item == null) {
                continue;
            }

            for (CreativeTabs tab : item.getCreativeTabs()) {
                item.getSubItems(item, tab, items);
            }
        }
    }
    
    public static void addInventory() {
        for (ItemStack stack : ClientHelper.getPlayer().inventory.mainInventory) {
            if (stack != null) {
                if(!items.contains(stack)) {
                    items.add(stack);
                }
            }
        }
    }
}
