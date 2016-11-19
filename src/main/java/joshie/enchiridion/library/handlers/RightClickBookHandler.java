package joshie.enchiridion.library.handlers;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IBookHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import javax.annotation.Nonnull;

public class RightClickBookHandler implements IBookHandler {
    @Override
    public String getName() {
        return "rightclick";
    }

    @Override
    public void handle(@Nonnull ItemStack stack, EntityPlayer player, EnumHand hand, int slotID, boolean isShiftPressed) {
        try {
            ItemStack ret = player.getHeldItem(hand).useItemRightClick(player.world, player, hand).getResult();
            EnchiridionAPI.library.getLibraryInventory(player).setInventorySlotContents(slotID, ret);
        } catch (Exception ignored) {
        }
    }
}