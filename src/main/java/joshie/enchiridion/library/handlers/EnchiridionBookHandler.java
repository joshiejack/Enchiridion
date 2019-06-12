package joshie.enchiridion.library.handlers;

import joshie.enchiridion.api.book.IBookHandler;
import joshie.enchiridion.data.book.BookRegistry;
import joshie.enchiridion.gui.book.GuiBook;
import joshie.enchiridion.lib.GuiIDs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

import javax.annotation.Nonnull;

public class EnchiridionBookHandler implements IBookHandler {
    @Override
    public String getName() {
        return "enchiridion";
    }

    @Override
    public void handle(@Nonnull ItemStack stack, PlayerEntity player, Hand hand, int slotID, boolean isShiftPressed) {
        if (player.world.isRemote) {
            GuiBook.INSTANCE.setBook(BookRegistry.INSTANCE.getBook(stack), isShiftPressed);
        }

        player.openGui(GuiIDs.BOOK_FORCE);
    }
}