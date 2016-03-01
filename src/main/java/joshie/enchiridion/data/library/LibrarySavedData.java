package joshie.enchiridion.data.library;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import joshie.lib.helpers.EntityHelper;
import joshie.lib.helpers.UUIDHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.UsernameCache;

public class LibrarySavedData extends WorldSavedData {
    public static final String DATA_NAME = "Enchiridion-Library";
    private HashMap<UUID, LibraryContents> players = new HashMap();

    public LibrarySavedData(String string) {
        super(string);
    }

    public Collection<LibraryContents> getPlayerData() {
        return players.values();
    }

    public LibraryContents getLibraryContents(EntityPlayerMP player) {
        UUID uuid = UUIDHelper.getPlayerUUID(player);
        if (players.containsKey(uuid)) {
            return players.get(uuid);
        } else {
            //If this UUID was not found, Search the username cache for this players username
            String name = player.getGameProfile().getName();
            for (Map.Entry<UUID, String> entry : UsernameCache.getMap().entrySet()) {
                if (entry.getValue().equals(name)) {
                    uuid = entry.getKey();
                    break;
                }
            }

            if (players.containsKey(uuid)) {
                return players.get(uuid);
            } else {
                LibraryContents data = new LibraryContents(player);
                players.put(uuid, data);

                markDirty();
                return players.get(uuid);
            }
        }
    }

    /** CAN AND WILL RETURN NULL, IF THE UUID COULD NOT BE FOUND **/
    public LibraryContents getLibraryContents(UUID uuid) {
        if (players.containsKey(uuid)) {
            return players.get(uuid);
        } else {
            EntityPlayer player = EntityHelper.getPlayerFromUUID(uuid);
            if (player == null) return null;
            else return getLibraryContents((EntityPlayerMP) player);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList tag_list_players = nbt.getTagList("LibraryContents", 10);
        for (int i = 0; i < tag_list_players.tagCount(); i++) {
            NBTTagCompound tag = tag_list_players.getCompoundTagAt(i);
            LibraryContents data = new LibraryContents();
            boolean success = false;
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
    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagList tag_list_players = new NBTTagList();
        for (Map.Entry<UUID, LibraryContents> entry : players.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                NBTTagCompound tag = new NBTTagCompound();
                entry.getValue().writeToNBT(tag);
                tag_list_players.appendTag(tag);
            }
        }

        nbt.setTag("LibraryContents", tag_list_players);
    }
}