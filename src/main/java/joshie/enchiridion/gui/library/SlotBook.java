package joshie.enchiridion.gui.library;

import joshie.enchiridion.ECommonProxy;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IBookHandler;
import joshie.enchiridion.items.ItemEnchiridion;
import joshie.enchiridion.library.LibraryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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

    private ItemStack asLibrary(ItemStack stack) {
        ItemStack clone = stack.copy();
        if (!clone.hasTagCompound()) clone.setTagCompound(new NBTTagCompound());
        clone.getTagCompound().setBoolean(ItemEnchiridion.IS_LIBRARY, true);
        return clone;
    }

    private boolean isLibrary(ItemStack stack) {
        return stack.getItem() == ECommonProxy.book && stack.getItemDamage() == 1;
    }

    private boolean isAppearingAsLibrary(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey(ItemEnchiridion.IS_LIBRARY);
    }

    @Nonnull
    public ItemStack handle(EntityPlayer player, int mouseButton, Slot slot) {
        if (mouseButton == 1) {
            //Right click
            ItemStack stack = slot.getStack();
            IBookHandler handler = EnchiridionAPI.library.getBookHandlerForStack(stack);
            if (handler != null) {
                ItemStack clone = asLibrary(stack);
                player.setHeldItem(hand, clone);
                clone.useItemRightClick(player.world, player, hand);
                LibraryHelper.getLibraryContents(player).setCurrentBook(slot.slotNumber);
            }

            return ItemStack.EMPTY;
        }

        if (slot.slotNumber == LibraryHelper.getLibraryContents(player).getCurrentBook()) {
            LibraryHelper.getLibraryContents(player).setCurrentBook(-1);
            player.setHeldItem(hand, new ItemStack(ECommonProxy.book, 1, 1));
            if (isAppearingAsLibrary(slot.getStack())) {
                slot.getStack().getTagCompound().removeTag(ItemEnchiridion.IS_LIBRARY);
            }
        }

        return DUMMY;
    }
}