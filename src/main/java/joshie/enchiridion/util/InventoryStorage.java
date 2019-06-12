package joshie.enchiridion.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

public abstract class InventoryStorage implements IInventory {
    protected NonNullList<ItemStack> inventory;

    public InventoryStorage(int size) {
        inventory = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    @Override
    public int getSizeInventory() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int index) {
        return inventory.get(index);
    }

    @Override
    @Nonnull
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = ItemStackHelper.getAndSplit(inventory, index, count);

        if (!stack.isEmpty()) {
            this.markDirty();
        }
        return stack;
    }

    @Override
    @Nonnull
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(inventory, index);
    }

    @Override
    public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
        inventory.set(index, stack);

        if (!stack.isEmpty() && stack.getCount() > getInventoryStackLimit()) {
            stack.setCount(getInventoryStackLimit());
        }
        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull PlayerEntity player) {
        return true;
    }

    @Override
    public void openInventory(@Nonnull PlayerEntity player) {
    }

    @Override
    public void closeInventory(@Nonnull PlayerEntity player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    public void readFromNBT(CompoundNBT nbt) {
        //Save Inventory
        inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);

        ItemStackHelper.loadAllItems(nbt, inventory);
    }

    public void writeToNBT(CompoundNBT nbt) {
        //Load Inventory
        ItemStackHelper.saveAllItems(nbt, inventory);
    }
}