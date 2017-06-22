package joshie.enchiridion.library.handlers;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IBookHandler;
import joshie.enchiridion.helpers.HeldHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class TemporarySwitchHandler implements IBookHandler {
    @Override
    public String getName() {
        return "switchclick";
    }

    @Override
    public void handle(ItemStack stack, EntityPlayer player, EnumHand hand, int slotID, boolean isShiftPressed) {
        try {
            ItemStack held = null; //Set the item to null
            EntityEquipmentSlot slot = HeldHelper.getSlotFromHand(hand);
            if (HeldHelper.getStackFromHand(player, hand) != null) held = HeldHelper.getStackFromHand(player, hand); //Store the held item
            player.setItemStackToSlot(slot, stack); //Replace the held item with the item in the book
            player.getHeldItem(hand).useItemRightClick(player.world, player, hand).getResult(); //Grab the result of right clicking
            EnchiridionAPI.library.getLibraryInventory(player).setInventorySlotContents(slotID, stack); //Replace with any changes
            player.setItemStackToSlot(slot, held); //Replace the current item with the item that was previously held
        } catch (Exception e) {}
    }
}