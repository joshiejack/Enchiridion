package joshie.enchiridion.library;

import joshie.enchiridion.helpers.UUIDHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class LibrarySavedData extends WorldSavedData {
    public static final String DATA_NAME = "Enchiridion-Library";
    private HashMap<UUID, LibraryInventory> players = new HashMap<>();

    public LibrarySavedData(String string) {
        super(string);
    }

    public Collection<LibraryInventory> getPlayerData() {
        return players.values();
    }

    public LibraryInventory getLibraryContents(EntityPlayerMP player) {
        UUID uuid = UUIDHelper.getPlayerUUID(player);
        if (players.containsKey(uuid)) {
            return players.get(uuid);
        } else {
            if (players.containsKey(uuid)) {
                return players.get(uuid);
            } else {
                LibraryInventory data = new LibraryInventory(player);
                players.put(uuid, data);

                markDirty();
                return players.get(uuid);
            }
        }
    }

    /**
     * CAN AND WILL RETURN NULL, IF THE UUID COULD NOT BE FOUND
     **/
    public LibraryInventory getLibraryContents(UUID uuid) {
        if (players.containsKey(uuid)) {
            return players.get(uuid);
        } else {
            EntityPlayer player = UUIDHelper.getPlayerFromUUID(uuid);
            if (player == null) return null;
            else return getLibraryContents((EntityPlayerMP) player);
        }
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound nbt) {
        NBTTagList tag_list_players = nbt.getTagList("LibraryInventory", 10);
        for (int i = 0; i < tag_list_players.tagCount(); i++) {
            NBTTagCompound tag = tag_list_players.getCompoundTagAt(i);
            LibraryInventory data = new LibraryInventory();
            boolean success;
            try {
                data.readFromNBT(tag);
                success = true;
            } catch (Exception e) {
                success = false;
            }
            //Only add non failed loads
            if (success) {
                players.put(data.getUUID(), data);
            }
        }
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound nbt) {
        NBTTagList tag_list_players = new NBTTagList();
        players.entrySet().stream().filter(entry -> entry.getKey() != null && entry.getValue() != null).forEach(entry -> {
            NBTTagCompound tag = new NBTTagCompound();
            entry.getValue().writeToNBT(tag);
            tag_list_players.appendTag(tag);
        });
        nbt.setTag("LibraryInventory", tag_list_players);
        return nbt;
    }
}