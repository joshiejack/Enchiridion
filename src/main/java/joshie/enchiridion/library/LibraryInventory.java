package joshie.enchiridion.library;

import java.util.UUID;

import joshie.enchiridion.helpers.UUIDHelper;
import joshie.enchiridion.network.PacketHandler;
import joshie.enchiridion.network.PacketSyncLibraryContents;
import joshie.enchiridion.util.InventoryStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class LibraryInventory extends InventoryStorage {
    public static final int MAX = 65;
    private EntityPlayer player; //No Direct calling, it's a cache value
    private UUID uuid; //SHOULD NOT BE CALLED, EXCEPT BY GET AND CREATE PLAYER
    private boolean receivedBooks;

    public LibraryInventory() {
        super(MAX); //Create the inventory
    }

    public LibraryInventory(EntityPlayer player) {
        super(MAX); //Create the inventory

        if (player != null) { //Creation before the server has started
            this.player = player;
            this.uuid = UUIDHelper.getPlayerUUID(player);
        }
    }
    
    private boolean insertIntoNextFreeSlot(ItemStack stack) {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null) {
                inventory[i] = stack;
                return true; //If we could insert the book, return true
            }
        }
        
        return false;
    }
    
    public void addDefaultBooks() {
        if (!receivedBooks) {
            for (ItemStack stack: ModSupport.getFreeBooks()) {
                if (!insertIntoNextFreeSlot(stack)) {
                    break;
                }
            }
            
            receivedBooks = true;
            markDirty();
        }
    }
    
    
    //Only should be called server side
    public void reset() {
        receivedBooks = false;
        markDirty();
    }
    
    //Only should be called server side
    public void clear() {
        inventory = new ItemStack[MAX];
        markDirty();
        EntityPlayer player = getAndCreatePlayer();
        if (player != null) {
            PacketHandler.sendToClient(new PacketSyncLibraryContents(this), getAndCreatePlayer());
        }
    }

    public EntityPlayer getAndCreatePlayer() {
        if (player == null) {
            player = UUIDHelper.getPlayerFromUUID(uuid);
        }

        return player;
    }

    public UUID getUUID() {
        return uuid;
    }

    public ItemStack[] getInventory() {
        return inventory;
    }

    @Override
    public void markDirty() {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            LibraryHelper.markDirty();
        }
    }

    @Override
    public String getName() {
        return "enchiridion.library";
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        uuid = UUID.fromString(nbt.getString("UUID")); //Read UUID
        super.readFromNBT(nbt); //Read NBT
        nbt.setBoolean("ReceivedBooks", receivedBooks);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setString("UUID", uuid.toString()); //Write UUID
        super.writeToNBT(nbt); //Write Items
        receivedBooks = nbt.getBoolean("ReceivedBooks");
    }
}
