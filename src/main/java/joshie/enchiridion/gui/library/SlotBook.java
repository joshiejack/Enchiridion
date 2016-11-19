package joshie.enchiridion.gui.library;

import joshie.enchiridion.ECommonProxy;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IBookHandler;
import joshie.enchiridion.helpers.MCClientHelper;
import joshie.enchiridion.library.LibraryHelper;
import joshie.enchiridion.network.PacketHandleBook;
import joshie.enchiridion.network.PacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import javax.annotation.Nonnull;

public class SlotBook extends Slot {
    private static final ItemStack DUMMY = new ItemStack(Items.BOOK);
    private EnumHand hand;

    public SlotBook(IInventory inventory, EnumHand hand, int index, int xPosition, int yPosition) {
        super(inventory, index, xPosition, yPosition);
        this.hand = hand;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        //FORBID LIBRARIES
        return !(stack.getItem() == ECommonProxy.book && stack.getItemDamage() == 1) && EnchiridionAPI.library.getBookHandlerForStack(stack) != null;
    }

    @Nonnull
    public ItemStack handle(EntityPlayer player, int mouseButton, Slot slot) {
        if (mouseButton == 1) {
            ItemStack stack = slot.getStack();
            IBookHandler handler = EnchiridionAPI.library.getBookHandlerForStack(stack);
            if (handler != null) {
                if (player.world.isRemote) {
                    boolean isShiftPressed = MCClientHelper.isShiftPressed();
                    PacketHandler.sendToServer(new PacketHandleBook(slot.slotNumber, hand, isShiftPressed));
                    handler.handle(stack, player, hand, slot.slotNumber, isShiftPressed);
                    LibraryHelper.getClientLibraryContents().setCurrentBook(slot.slotNumber);
                }
                return ItemStack.EMPTY;
            }
        }

        return DUMMY;
    }
}