package joshie.enchiridion.library.handlers;

import joshie.enchiridion.api.book.IBookHandler;
import joshie.enchiridion.data.book.BookRegistry;
import joshie.enchiridion.gui.book.GuiBook;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

import javax.annotation.Nonnull;

public class EnchiridionBookHandler implements IBookHandler {
    @Override
    public String getName() {
        return "enchiridion";
    }

    @Override
    public void handle(@Nonnull ItemStack stack, ServerPlayerEntity player, Hand hand, int slotID, boolean isShiftPressed) {
        if (player.world.isRemote) {
            GuiBook.INSTANCE.setBook(BookRegistry.INSTANCE.getBook(stack), isShiftPressed);
        }

        player.openBook(stack, hand);
    }
}