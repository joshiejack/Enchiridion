package joshie.enchiridion.data.library;

import java.util.UUID;

import joshie.lib.helpers.EntityHelper;
import joshie.lib.helpers.UUIDHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class LibraryContents {
    private static final int MAX = 1024;
    private ItemStack[] inventory = new ItemStack[MAX];

    //References to the player and uuid this refers to
    private EntityPlayerMP player; //No Direct calling, it's a cache value
    private UUID uuid; //SHOULD NOT BE CALLED, EXCEPT BY GET AND CREATE PLAYER

    public LibraryContents() {}
    public LibraryContents(EntityPlayerMP player) {
        this.player = player;
        uuid = UUIDHelper.getPlayerUUID(player);
    }

    public EntityPlayerMP getAndCreatePlayer() {
        if (player == null) {
            player = EntityHelper.getPlayerFromUUID(uuid);
        }

        return player;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        uuid = UUID.fromString(nbt.getString("UUID"));
    }

    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setString("UUID", uuid.toString());
    }
}