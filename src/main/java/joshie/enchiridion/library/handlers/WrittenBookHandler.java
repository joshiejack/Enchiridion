package joshie.enchiridion.library.handlers;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.book.IBookHandler;
import joshie.enchiridion.lib.GuiIDs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import javax.annotation.Nonnull;

public class WrittenBookHandler implements IBookHandler {
    @Override
    public String getName() {
        return "written";
    }

    @Override
    public void handle(@Nonnull ItemStack stack, EntityPlayer player, EnumHand hand, int slotID, boolean isShiftPressed) {
        player.openGui(Enchiridion.instance, GuiIDs.WRITTEN, player.world, slotID, 0, 0);
    }
}