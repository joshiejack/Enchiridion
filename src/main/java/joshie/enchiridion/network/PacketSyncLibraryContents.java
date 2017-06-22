package joshie.enchiridion.network;

import io.netty.buffer.ByteBuf;
import joshie.enchiridion.library.LibraryHelper;
import joshie.enchiridion.library.LibraryInventory;
import joshie.enchiridion.network.core.PacketNBT;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class PacketSyncLibraryContents extends PacketNBT {
    private int currentBook;
    
    public PacketSyncLibraryContents() {}
    public PacketSyncLibraryContents(LibraryInventory contents) {
        super(contents.getInventory());
        currentBook = contents.getCurrentBook();
    }
    
    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeInt(currentBook);
        super.toBytes(buffer);
    }
    
    @Override
    public void fromBytes(ByteBuf buffer) {
        currentBook = buffer.readInt();
        super.fromBytes(buffer);
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        World world = player.world;

        //Reload the info in from the packet that was sent
        int length = nbt.getInteger("length");
        ItemStack[] inventory = new ItemStack[LibraryInventory.MAX];
        NBTTagList tagList = nbt.getTagList("Inventory", 10);
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound tag = tagList.getCompoundTagAt(i);
            byte slot = tag.getByte("Slot");
            if (slot > inventory.length || slot < 0) continue;
            if (tag.getBoolean("NULLItemStack") == true) {
                inventory[slot] = null;
            } else if (slot >= 0 && slot < inventory.length) {
                inventory[slot] = ItemStack.loadItemStackFromNBT(tag);
            }
        }

        //Set the client library
        LibraryHelper.getClientLibraryContents().setCurrentBook(currentBook);
        for (int i = 0; i < inventory.length; i++) {
            LibraryHelper.getClientLibraryContents().setInventorySlotContents(i, inventory[i]);
        }
    }

}