package joshie.enchiridion.gui.library;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.lib.gui.ContainerCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerLibrary extends ContainerCore {
    public IInventory library;

    public ContainerLibrary(InventoryPlayer playerInventory, IInventory library) {
        //Set the library
        this.library = library;
        
        //Left hand side slots
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                addSlotToContainer(new SlotBook(library, j + (i * 3),  -51 + (j * 18), 22 + (i * 23)));
            }
        }
        
        //Right hand side slots
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                addSlotToContainer(new SlotBook(library, 15 + j + (i * 3),  175 + (j * 18), 22 + (i * 23)));
            }
        }
        
        //Centre Slots
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                addSlotToContainer(new SlotBook(library, 30 + j + (i * 7),  26 + (j * 18), -1 + (i * 23)));
            }
        }
        
       // addSlotToContainer(new SlotBook(library, 0, 8, 15)); //Add one book slot
        bindPlayerInventory(playerInventory, 30);
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
        int size = library.getSizeInventory();
        int low = size + 27;
        int high = low + 9;
        ItemStack itemstack = null;
        Slot slot = (Slot) inventorySlots.get(slotID);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemstack = stack.copy();

            if (slotID < size) {
                if (!mergeItemStack(stack, size, high, true)) return null;
                slot.onSlotChange(stack, itemstack);
            } else if (slotID >= size) {
                if (EnchiridionAPI.library.getBookHandlerForStack(stack) != null) {
                    if (!mergeItemStack(stack, 0, 65, false)) return null; //Slots 0-64 for Books
                } else if (slotID >= size && slotID < low) {
                    if (!mergeItemStack(stack, low, high, false)) return null;
                } else if (slotID >= low && slotID < high && !mergeItemStack(stack, high, low, false)) return null;
            } else if (!mergeItemStack(stack, size, high, false)) return null;

            if (stack.stackSize == 0) {
                slot.putStack((ItemStack) null);
            } else {
                slot.onSlotChanged();
            }

            if (stack.stackSize == itemstack.stackSize) return null;

            slot.onPickupFromSlot(player, stack);
        }

        return itemstack;
    }
}
