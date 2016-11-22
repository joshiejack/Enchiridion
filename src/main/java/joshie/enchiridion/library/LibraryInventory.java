package joshie.enchiridion.library;

import joshie.enchiridion.helpers.UUIDHelper;
import joshie.enchiridion.network.PacketHandler;
import joshie.enchiridion.network.PacketSyncLibraryContents;
import joshie.enchiridion.util.InventoryStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import java.util.UUID;

public class LibraryInventory extends InventoryStorage {
    public static final int NO_SLOT = -1;
    public static final int MAX = 65;
    private EntityPlayer player; //No Direct calling, it's a cache value
    private UUID uuid; //SHOULD NOT BE CALLED, EXCEPT BY GET AND CREATE PLAYER
    private boolean receivedBooks;
    private int currentBook = NO_SLOT;

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

    public int getCurrentBook() {
        return currentBook;
    }

    @Nonnull
    public ItemStack getCurrentBookItem() {
        return getCurrentBook() == NO_SLOT ? ItemStack.EMPTY : getStackInSlot(getCurrentBook());
    }

    public void setCurrentBook(int slot) {
        currentBook = slot;
        markDirty();
    }

    private boolean insertIntoNextFreeSlot(ItemStack stack) {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).isEmpty()) {
                inventory.set(i, stack);
                return true; //If we could insert the book, return true
            }
        }

        return false;
    }

    public void addDefaultBooks() {
        if (!receivedBooks) {
            for (ItemStack stack : ModSupport.getFreeBooks()) {
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
        inventory.clear();
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

    public NonNullList<ItemStack> getInventory() {
        return inventory;
    }

    @Override
    public void markDirty() {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            LibraryHelper.markDirty();
        }
    }

    @Override
    @Nonnull
    public String getName() {
        return "enchiridion.library";
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        currentBook = nbt.getInteger("CurrentBook");
        uuid = UUID.fromString(nbt.getString("UUID")); //Read UUID
        super.readFromNBT(nbt); //Read NBT
        nbt.setBoolean("ReceivedBooks", receivedBooks);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("CurrentBook", currentBook);
        nbt.setString("UUID", uuid.toString()); //Write UUID
        super.writeToNBT(nbt); //Write Items
        receivedBooks = nbt.getBoolean("ReceivedBooks");
    }
}