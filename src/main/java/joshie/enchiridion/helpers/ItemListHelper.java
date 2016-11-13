package joshie.enchiridion.helpers;

import joshie.enchiridion.Enchiridion;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;

public class ItemListHelper {
    private static ArrayList<ItemStack> items = null;
    private static ArrayList<ItemStack> allItems = null;

    public static List<ItemStack> init() {
        items = new ArrayList<>();
        allItems = new ArrayList<>();

        for (Item item : Item.REGISTRY) {
            if (item == null) {
                continue;
            }

            if (item.getCreativeTabs().length > 0) {
                for (CreativeTabs tab : item.getCreativeTabs()) {
                    try {
                        item.getSubItems(item, tab, items);
                    } catch (Exception e) {
                        Enchiridion.log(Level.ERROR, "Enchiridion had an issue when trying to load the item: " + item.getClass());
                    }
                }
            }
        }
        allItems.addAll(items);
        return items;
    }

    public static List<ItemStack> items() {
        return items != null ? items : init();
    }

    public static List<ItemStack> allItems() {
        return allItems != null ? allItems : init();
    }

    public static void addInventory() {
        for (ItemStack stack : Minecraft.getMinecraft().thePlayer.inventory.mainInventory) {
            if (stack != null) {
                if (!allItems().contains(stack)) {
                    allItems.add(stack);
                }
            }
        }
    }
}