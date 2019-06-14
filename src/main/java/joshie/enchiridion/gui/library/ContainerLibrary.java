package joshie.enchiridion.gui.library;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.helpers.HeldHelper;
import joshie.enchiridion.lib.EGuis;
import joshie.enchiridion.library.LibraryHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;

import javax.annotation.Nonnull;

public class ContainerLibrary extends Container { //TODO Fix. Broken in Forge atm.
    public IInventory library;

    public ContainerLibrary(int windowID, PlayerInventory playerInventory, PacketBuffer extraData) {
        this(windowID, playerInventory, LibraryHelper.getServerLibraryContents(playerInventory.player), HeldHelper.getHandFromOrdinal(extraData.readInt()));
    }

    public ContainerLibrary(int windowID, PlayerInventory playerInventory, IInventory library, Hand hand) {
        super(EGuis.LIBRARY_CONTAINER, windowID);
        this.library = library;

        //Left hand side slots
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                addSlot(new SlotBook(library, hand, j + (i * 3), -51 + (j * 18), 22 + (i * 23)));
            }
        }

        //Right hand side slots
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                addSlot(new SlotBook(library, hand, 15 + j + (i * 3), 175 + (j * 18), 22 + (i * 23)));
            }
        }

        //Centre Slots
        for (int i = 0; i < 5; i++) {
            int y = i == 0 ? 1 : 0;
            for (int j = 0; j < 7; j++) {
                addSlot(new SlotBook(library, hand, 30 + j + (i * 7), 26 + (j * 18), -1 + (i * 23) - y));
            }
        }

        // addSlotToContainer(new SlotBook(library, 0, 8, 15)); //Add one book slot
        bindPlayerInventory(playerInventory, 30);
    }

    protected void bindPlayerInventory(PlayerInventory inventory, int yOffset) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18 + yOffset));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(inventory, i, 8 + i * 18, 142 + yOffset));
        }
    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity player) {
        return true;
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(PlayerEntity player, int slotID) {
        int size = library.getSizeInventory();
        int low = size + 27;
        int high = low + 9;
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(slotID);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemstack = stack.copy();

            if (slotID < size) {
                if (!mergeItemStack(stack, size, high, true)) return ItemStack.EMPTY;
                slot.onSlotChange(stack, itemstack);
            } else if (slotID >= size) {
                if (EnchiridionAPI.library.getBookHandlerForStack(stack) != null) {
                    if (!mergeItemStack(stack, 0, 65, false)) return ItemStack.EMPTY; //Slots 0-64 for Books
                } else if (slotID >= size && slotID < low) {
                    if (!mergeItemStack(stack, low, high, false)) return ItemStack.EMPTY;
                } else if (slotID >= low && slotID < high && !mergeItemStack(stack, high, low, false))
                    return ItemStack.EMPTY;
            } else if (!mergeItemStack(stack, size, high, false)) return ItemStack.EMPTY;

            if (stack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (stack.getCount() == itemstack.getCount()) return ItemStack.EMPTY;

            slot.onTake(player, stack);
        }
        return itemstack;
    }

    @Override
    @Nonnull
    public ItemStack slotClick(int slotID, int mouseButton, ClickType type, PlayerEntity player) {
        Slot slot = slotID < 0 || slotID > inventorySlots.size() ? null : inventorySlots.get(slotID);
        if (!(player instanceof ServerPlayerEntity)) return ItemStack.EMPTY;
        return slot instanceof SlotBook && ((SlotBook) slot).handle((ServerPlayerEntity) player, mouseButton, slot).isEmpty() ? ItemStack.EMPTY : super.slotClick(slotID, mouseButton, type, player);
    }
}