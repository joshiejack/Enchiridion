package joshie.enchiridion.library.handlers;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.book.IBookHandler;
import joshie.enchiridion.data.book.BookRegistry;
import joshie.enchiridion.gui.book.GuiBook;
import joshie.enchiridion.lib.GuiIDs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class EnchiridionBookHandler implements IBookHandler {
    @Override
    public String getName() {
        return "enchiridion";
    }

    @Override
    public void handle(ItemStack stack, EntityPlayer player, int slotID, boolean isShiftPressed) {
        if (player.worldObj.isRemote) {
            GuiBook.INSTANCE.setBook(BookRegistry.INSTANCE.getBook(stack), false);
        }
        
        player.openGui(Enchiridion.instance, GuiIDs.BOOK_FORCE, player.worldObj, 0, 0, 0);
    }
}
