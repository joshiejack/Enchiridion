package joshie.enchiridion.api.book;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IBookHandler {
    /** The name of this handler, used to associate this handler in configuration **/
    public String getName();

    /** Called whenever the book is right clicked in the library
     * This method is only ever called on the client **/
    public void handle(ItemStack stack, World world, EntityPlayer player);
}
