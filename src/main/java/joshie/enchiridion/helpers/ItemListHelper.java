package joshie.enchiridion.helpers;

import joshie.enchiridion.Enchiridion;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Level;

import java.util.stream.Collectors;

public class ItemListHelper {
    private static NonNullList<ItemStack> items = NonNullList.withSize(0, ItemStack.EMPTY);
    private static NonNullList<ItemStack> allItems = NonNullList.withSize(0, ItemStack.EMPTY);

    public static NonNullList<ItemStack> init() {
        items = NonNullList.create();
        allItems = NonNullList.create();

        for (Item item : ForgeRegistries.ITEMS) {
            if (item == null) {
                continue;
            }
            try {
                item.fillItemGroup(ItemGroup.SEARCH, items);
            } catch (Exception e) {
                Enchiridion.log(Level.ERROR, "Enchiridion had an issue when trying to load the item: " + item.getClass());
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
        try {
            allItems.addAll(Minecraft.getInstance().player.inventory.mainInventory.stream().filter(stack -> !stack.isEmpty()).filter(stack -> !allItems().contains(stack)).collect(Collectors.toList()));
        } catch (Exception ignored) {
        }
    }
}