package joshie.enchiridion.network.core;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;

public abstract class PacketNBT extends PenguinPacket {
    public NBTTagCompound nbt;

    public PacketNBT() {
    }

    public PacketNBT(NonNullList<ItemStack> inventory) {
        nbt = new NBTTagCompound();
        nbt.setInteger("length", inventory.size());
        NBTTagList itemList = new NBTTagList();
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.get(i);
            if (!stack.isEmpty()) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte) i);
                tag.setBoolean("NULLItemStack", false);
                stack.writeToNBT(tag);
                itemList.appendTag(tag);
            } else {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte) i);
                tag.setBoolean("NULLItemStack", true);
                itemList.appendTag(tag);
            }
        }
        nbt.setTag("Inventory", itemList);
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        try {
            new PacketBuffer(buffer).writeCompoundTag(nbt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        try {
            nbt = new PacketBuffer(buffer).readCompoundTag();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}