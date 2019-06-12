package joshie.enchiridion.api.book;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

import javax.annotation.Nonnull;

public interface IBookHandler {
    /** The name of this handler, used to associate this handler in configuration **/
    String getName();

    /** Called whenever the book is right clicked in the library
     *  Called on both sides
     *  @param stack stack the latest item
     *  @param player the player right clicking
     *  @param hand the hand being used
     *  @param slotID the slot that this item is stored in the library
     *  @param isShiftPressed whether the shift key is being held down on this book while right clicking
     *          you can use #EnchiridionAPI.library.getInventory to grab a the iinventory for the library
     *          which will allow you to get the stacks or set the stacks**/
    void handle(@Nonnull ItemStack stack, PlayerEntity player, Hand hand, int slotID, boolean isShiftPressed);
}