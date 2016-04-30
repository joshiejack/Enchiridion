package joshie.enchiridion.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.ITextComponent;

public abstract class InventoryStorage implements IInventory {
    protected ItemStack[] inventory;

    public InventoryStorage(int size) {
        inventory = new ItemStack[size];
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation(getName(), new Object[0]);
    }

    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return inventory[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (inventory[index] != null) {
            if (inventory[index].stackSize <= count) {
                ItemStack itemstack = inventory[index];
                inventory[index] = null;
                markDirty();
                return itemstack;
            }
            ItemStack itemstack1 = inventory[index].splitStack(count);
            if (inventory[index].stackSize == 0) {
                inventory[index] = null;
            }
            markDirty();
            return itemstack1;
        } else return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (inventory[index] != null) {
            ItemStack itemstack = inventory[index];
            inventory[index] = null;
            return itemstack;
        } else return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        inventory[index] = stack;

        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }

        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {}

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {}

    @Override
    public void closeInventory(EntityPlayer player) {}

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {}

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < inventory.length; ++i) {
            inventory[i] = null;
        }
    }

    public void readFromNBT(NBTTagCompound nbt) {
        //Save Inventory
        NBTTagList nbttaglist = nbt.getTagList("Items", 10);
        inventory = new ItemStack[getSizeInventory()];
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot") & 255;

            if (j >= 0 && j < inventory.length) {
                inventory[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
            }
        }
    }

    public void writeToNBT(NBTTagCompound nbt) {
        //Load Inventory
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < inventory.length; ++i) {
            if (inventory[i] != null) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte) i);
                inventory[i].writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }

        nbt.setTag("Items", nbttaglist);
    }

}
