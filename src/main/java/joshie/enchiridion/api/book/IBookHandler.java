package joshie.enchiridion.api.book;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IBookHandler {
    /** The name of this handler, used in configs **/
    public String getName();

    /** Called whenever the book is clicked on in the library gui, default simply calls right click, Called serverside and clientside **/
    public void handle(ItemStack stack, World world, EntityPlayer player);
}
