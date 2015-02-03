package joshie.enchiridion.library.handlers;

import joshie.enchiridion.helpers.ClientHelper;
import joshie.enchiridion.helpers.StackHelper;
import joshie.enchiridion.library.LibraryHelper;
import joshie.enchiridion.library.ModBooks.ModBookData;
import joshie.enchiridion.network.EPacketHandler;
import joshie.enchiridion.network.PacketOverwrite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class BookObtainEvents {
    public static boolean hasBook(ItemStack stack, String key) {
        for (ItemStack book : LibraryHelper.storage.getBooks()) {
            if (book.isItemEqual(stack) && ((book.hasTagCompound() && book.stackTagCompound.hasKey(key)) || key.equals(""))) {
                return true;
            }
        }

        return false;
    }

    @SubscribeEvent
    public void openGui(GuiOpenEvent event) {
        if (event.gui == null || LibraryHelper.modBooks == null) return;
        String clazz = event.gui.getClass().getName();
        for (ModBookData data : LibraryHelper.modBooks.books) {
            if (data.item == null || data.free == true || data.openGuiClass.equals("") || hasBook(data.item, data.openGuiNBT)) continue;
            if (data.openGuiClass.equals(clazz)) {
                ItemStack held = ClientHelper.getPlayer().getCurrentEquippedItem();
                if (data.openGuiNBT.equals("") || (held != null && held.hasTagCompound() && held.stackTagCompound.hasKey(data.openGuiNBT))) {
                    ItemStack overwrites = null;
                    try {
                        if (!data.overwrite.equals("")) overwrites = StackHelper.getStackFromString(data.overwrite);
                    } catch (Exception e) {}

                    if (overwrites != null) {
                        LibraryHelper.storage.overwrite(data.item, overwrites);
                    } else LibraryHelper.storage.add(data.item);

                    EPacketHandler.sendToServer(new PacketOverwrite(data.item, overwrites));
                }
            }
        }
    }

    @SubscribeEvent
    public void onCrafting(ItemCraftedEvent event) {
        ItemStack stack = event.crafting;
        if (stack != null) {
            for (ModBookData data : LibraryHelper.modBooks.books) {
                if (data.item == null || data.free == true || data.onCrafted == false || hasBook(data.item, "")) continue;
                if (data.item.isItemEqual(stack)) {
                    ItemStack overwrites = null;
                    try {
                        if (!data.overwrite.equals("")) overwrites = StackHelper.getStackFromString(data.overwrite);
                    } catch (Exception e) {}

                    if (overwrites != null) {
                        LibraryHelper.storage.overwrite(data.item, overwrites);
                    } else LibraryHelper.storage.add(data.item);

                    EPacketHandler.sendToServer(new PacketOverwrite(data.item, overwrites));
                }
            }
        }
    }

    @SubscribeEvent
    public void onItemPickUp(EntityItemPickupEvent event) {
        if (event.item == null) return;
        ItemStack stack = event.item.getEntityItem();
        for (ModBookData data : LibraryHelper.modBooks.books) {
            if (data.item == null || data.free == true || data.pickUp == false || hasBook(data.item, "")) continue;
            if (data.item.isItemEqual(stack)) {
                ItemStack overwrites = null;
                try {
                    if (!data.overwrite.equals("")) overwrites = StackHelper.getStackFromString(data.overwrite);
                } catch (Exception e) {}

                if (overwrites != null) {
                    LibraryHelper.storage.overwrite(data.item, overwrites);
                } else LibraryHelper.storage.add(data.item);

                EPacketHandler.sendToServer(new PacketOverwrite(data.item, overwrites));
            }
        }
    }
}
