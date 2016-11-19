package joshie.enchiridion.helpers;

import joshie.enchiridion.Enchiridion;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.apache.logging.log4j.Level;

public class ItemListHelper {
    private static NonNullList<ItemStack> items = NonNullList.withSize(0, ItemStack.EMPTY);
    private static NonNullList<ItemStack> allItems = NonNullList.withSize(0, ItemStack.EMPTY);

    public static NonNullList<ItemStack> init() {
        items = NonNullList.create();
        allItems = NonNullList.create();

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

    public static NonNullList<ItemStack> items() {
        return !items.isEmpty() ? items : init();
    }

    public static NonNullList<ItemStack> allItems() {
        return !allItems.isEmpty() ? allItems : init();
    }

    public static void addInventory() {
        for (ItemStack stack : Minecraft.getMinecraft().player.inventory.mainInventory) {
            if (!stack.isEmpty()) {
                if (!allItems().contains(stack)) {
                    allItems.add(stack);
                }
            }
        }
    }
}