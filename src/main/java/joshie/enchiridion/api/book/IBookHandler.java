package joshie.enchiridion.api.book;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IBookHandler {
    /** The name of this handler, used to associate this handler in configuration **/
    public String getName();

    /** Called whenever the book is right clicked in the library
     *  Called on both sides
     *  @stack the latest item
     *  @player the player right clicking
     *  @slotID the slot that this item is stored in the library
     *          you can use #EnchiridionAPI.library.getInventory to grab a the iinventory for the library
     *          which will allow you to get the stacks or set the stacks**/
    public void handle(ItemStack stack, EntityPlayer player, int slotID);
}
