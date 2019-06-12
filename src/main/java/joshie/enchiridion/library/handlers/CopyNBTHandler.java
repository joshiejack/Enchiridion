package joshie.enchiridion.library.handlers;

import joshie.enchiridion.library.LibraryHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;

import javax.annotation.Nonnull;

public class CopyNBTHandler extends TemporarySwitchHandler {
    @Override
    public String getName() {
        return "copynbt";
    }

    @Override
    public void handle(@Nonnull ItemStack stack, PlayerEntity player, Hand hand, int slotID, boolean isShiftPressed) {
        ItemStack library = player.getHeldItem(hand);
        if (library.hasTag()) { //Copy the current configs for the library item to the item itself, to be saved
            library.setTag(library.getTag());
            LibraryHelper.markDirty();
        } else library.setTag(new CompoundNBT());

        super.handle(stack, player, hand, slotID, isShiftPressed);
    }
}