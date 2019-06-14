package joshie.enchiridion.network.packet;

import joshie.enchiridion.library.LibraryHelper;
import joshie.enchiridion.library.LibraryInventory;
import joshie.enchiridion.network.core.PacketNBT;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncLibraryContents extends PacketNBT {
    private int currentBook;

    public PacketSyncLibraryContents(int currentBook) {
        this.currentBook = currentBook;
    }

    public PacketSyncLibraryContents(LibraryInventory contents) {
        super(contents.getInventory());
        this.currentBook = contents.getCurrentBook();
    }

    public static void encode(PacketSyncLibraryContents packet, PacketBuffer buf) {
        buf.writeInt(packet.currentBook);
        toBytes(packet, buf);
    }

    public static PacketSyncLibraryContents decode(PacketBuffer buf) {
        PacketSyncLibraryContents packet = new PacketSyncLibraryContents(buf.readInt());
        fromBytes(packet, buf);
        return packet;
    }

    public static class Handler {
        public static void handle(PacketSyncLibraryContents message, Supplier<NetworkEvent.Context> ctx) {
            //Reload the info in from the packet that was sent
            NonNullList<ItemStack> inventory = NonNullList.withSize(LibraryInventory.MAX, ItemStack.EMPTY);
            ListNBT tagList = message.nbt.getList("Inventory", 10);
            for (int i = 0; i < tagList.size(); i++) {
                CompoundNBT tag = tagList.getCompound(i);
                byte slot = tag.getByte("Slot");
                if (slot > inventory.size() || slot < 0) continue;
                if (tag.getBoolean("NULLItemStack")) {
                    inventory.set(slot, ItemStack.EMPTY);
                } else if (slot >= 0 && slot < inventory.size()) {
                    inventory.set(slot, ItemStack.read(tag));
                }
            }

            //Set the client library
            LibraryHelper.getClientLibraryContents().setCurrentBook(message.currentBook);
            for (int i = 0; i < inventory.size(); i++) {
                LibraryHelper.getClientLibraryContents().setInventorySlotContents(i, inventory.get(i));
            }
            ctx.get().setPacketHandled(true);
        }
    }
}