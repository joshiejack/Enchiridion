package joshie.enchiridion.gui;

import joshie.enchiridion.ECommonProxy;
import joshie.enchiridion.api.book.IBook;
import joshie.enchiridion.data.book.BookRegistry;
import joshie.enchiridion.gui.book.GuiBook;
import joshie.enchiridion.gui.book.GuiBookCreate;
import joshie.enchiridion.gui.library.ContainerLibrary;
import joshie.enchiridion.gui.library.GuiLibrary;
import joshie.enchiridion.lib.GuiIDs;
import joshie.enchiridion.library.LibraryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == GuiIDs.LIBRARY) {
            return new ContainerLibrary(player.inventory, LibraryHelper.getServerLibraryContents(player));
        } else return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == GuiIDs.LIBRARY) {
            return new GuiLibrary(player.inventory, LibraryHelper.getClientLibraryContents());
        } else {
            ItemStack held = player.getCurrentEquippedItem();
            if (held != null && held.getItem() == ECommonProxy.book) {
                IBook book = BookRegistry.INSTANCE.getBook(held);
                if (book != null) {
                    return GuiBook.INSTANCE.setBook(book, player.isSneaking());
                } else return GuiBookCreate.INSTANCE.setStack(player.getCurrentEquippedItem());
            }
        }

        return null;
    }
}