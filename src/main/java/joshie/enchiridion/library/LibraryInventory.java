package joshie.enchiridion.library;

import joshie.enchiridion.helpers.UUIDHelper;
import joshie.enchiridion.network.PacketHandler;
import joshie.enchiridion.network.packet.PacketSyncLibraryContents;
import joshie.enchiridion.util.InventoryStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.thread.EffectiveSide;

import javax.annotation.Nonnull;
import java.util.UUID;

public class LibraryInventory extends InventoryStorage {
    public static final int MAX = 65;
    private PlayerEntity player; //No Direct calling, it's a cache value
    private UUID uuid; //SHOULD NOT BE CALLED, EXCEPT BY GET AND CREATE PLAYER
    private boolean receivedBooks;
    private int currentBook;

    public LibraryInventory() {
        super(MAX); //Create the inventory
    }

    public LibraryInventory(PlayerEntity player) {
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
        return getStackInSlot(getCurrentBook());
    }

    public void setCurrentBook(int slot) {
        currentBook = slot;
        markDirty();
    }

    private boolean insertIntoNextFreeSlot(@Nonnull ItemStack stack) {
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
        PlayerEntity player = getAndCreatePlayer();
        if (player != null) {
            PacketHandler.sendToClient(new PacketSyncLibraryContents(this), (ServerPlayerEntity) getAndCreatePlayer());
        }
    }

    public PlayerEntity getAndCreatePlayer() {
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
        if (EffectiveSide.get() == LogicalSide.SERVER) {
            LibraryHelper.markDirty();
        }
    }

    @Override
    public void readFromNBT(CompoundNBT nbt) {
        currentBook = nbt.getInt("CurrentBook");
        uuid = UUID.fromString(nbt.getString("UUID")); //Read UUID
        super.readFromNBT(nbt); //Read NBT
        nbt.putBoolean("ReceivedBooks", receivedBooks);
    }

    @Override
    public void writeToNBT(CompoundNBT nbt) {
        nbt.putInt("CurrentBook", currentBook);
        nbt.putString("UUID", uuid.toString()); //Write UUID
        super.writeToNBT(nbt); //Write Items
        receivedBooks = nbt.getBoolean("ReceivedBooks");
    }
}