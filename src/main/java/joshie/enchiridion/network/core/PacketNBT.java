package joshie.enchiridion.network.core;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;

public class PacketNBT {
    public CompoundNBT nbt;

    public PacketNBT() {
    }

    public PacketNBT(NonNullList<ItemStack> inventory) {
        nbt = new CompoundNBT();
        nbt.putInt("length", inventory.size());
        ListNBT itemList = new ListNBT();
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.get(i);
            if (!stack.isEmpty()) {
                CompoundNBT tag = new CompoundNBT();
                tag.putByte("Slot", (byte) i);
                tag.putBoolean("NULLItemStack", false);
                stack.write(tag);
                itemList.add(tag);
            } else {
                CompoundNBT tag = new CompoundNBT();
                tag.putByte("Slot", (byte) i);
                tag.putBoolean("NULLItemStack", true);
                itemList.add(tag);
            }
        }
        nbt.put("Inventory", itemList);
    }

    public static void toBytes(PacketNBT packet, PacketBuffer buf) {
        try {
            new PacketBuffer(buf).writeCompoundTag(packet.nbt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void fromBytes(PacketNBT packet, PacketBuffer buf) {
        try {
            packet.nbt = buf.readCompoundTag();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}