package joshie.enchiridion.library.handlers;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IBookHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class TemporarySwitchHandler implements IBookHandler {
    @Override
    public String getName() {
        return "switchclick";
    }

    @Override
    public void handle(ItemStack stack, EntityPlayer player, int slotID, boolean isShiftPressed) {
        try {
            ItemStack held = null; //Set the item to null
            if (player.getCurrentEquippedItem() != null) held = player.getCurrentEquippedItem().copy(); //Store the held item
            player.setCurrentItemOrArmor(0, stack.copy()); //Replace the held item with the item in the book
            ItemStack ret = player.getCurrentEquippedItem().useItemRightClick(player.worldObj, player); //Grab the result of right clicking
            EnchiridionAPI.library.getLibraryInventory(player).setInventorySlotContents(slotID, ret); //Replace with any changes
            player.setCurrentItemOrArmor(0, held); //Replace the current item with the item that was previously held
        } catch (Exception e) {}
    }
}
