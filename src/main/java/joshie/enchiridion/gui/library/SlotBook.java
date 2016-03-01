package joshie.enchiridion.gui.library;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IBookHandler;
import joshie.enchiridion.network.PacketHandleBook;
import joshie.enchiridion.network.PacketHandler;
import joshie.lib.gui.SlotSpecial;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBook extends SlotSpecial {
    private static final ItemStack dummy = new ItemStack(Items.book);

    public SlotBook(IInventory inventory, int index, int xPosition, int yPosition) {
        super(inventory, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return EnchiridionAPI.library.getBookHandlerForStack(stack) != null;
    }

    @Override
    public ItemStack handle(EntityPlayer player, int mouseButton, Slot slot) {
        if (mouseButton == 1) {
            ItemStack stack = slot.getStack();
            IBookHandler handler = EnchiridionAPI.library.getBookHandlerForStack(stack);
            if (handler != null) {
                if (player.worldObj.isRemote) PacketHandler.sendToServer(new PacketHandleBook(slot.slotNumber));
                handler.handle(stack, player, slot.slotNumber);
                return null;
            }
        }

        return dummy;
    }
}
