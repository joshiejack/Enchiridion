package joshie.enchiridion.library.handlers;

import joshie.enchiridion.api.book.IBookHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

import javax.annotation.Nonnull;

public class WrittenBookHandler implements IBookHandler {
    @Override
    public String getName() {
        return "written";
    }

    @Override
    public void handle(@Nonnull ItemStack stack, PlayerEntity player, Hand hand, int slotID, boolean isShiftPressed) {
        if (player.world.isRemote) {
            player.openBook(stack, hand);
        }
    }
}