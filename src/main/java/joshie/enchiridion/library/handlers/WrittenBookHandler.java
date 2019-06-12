package joshie.enchiridion.library.handlers;

import joshie.enchiridion.api.book.IBookHandler;
import joshie.enchiridion.lib.GuiIDs;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

public class WrittenBookHandler implements IBookHandler {
    @Override
    public String getName() {
        return "written";
    }

    @Override
    public void handle(@Nonnull ItemStack stack, ServerPlayerEntity player, Hand hand, int slotID, boolean isShiftPressed) {
        //NetworkHooks.openGui(GuiIDs.WRITTEN, slotID); //TODO
    }
}