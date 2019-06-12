package joshie.enchiridion.library;

import joshie.enchiridion.helpers.UUIDHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.storage.WorldSavedData;

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

    public LibraryInventory getLibraryContents(ServerPlayerEntity player) {
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
            PlayerEntity player = UUIDHelper.getPlayerFromUUID(uuid);
            if (player == null) return null;
            else return getLibraryContents((ServerPlayerEntity) player);
        }
    }

    @Override
    public void read(@Nonnull CompoundNBT nbt) {
        ListNBT tag_list_players = nbt.getList("LibraryInventory", 10);
        for (int i = 0; i < tag_list_players.size(); i++) {
            CompoundNBT tag = tag_list_players.getCompound(i);
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
    public CompoundNBT write(@Nonnull CompoundNBT nbt) {
        ListNBT tag_list_players = new ListNBT();
        players.entrySet().stream().filter(entry -> entry.getKey() != null && entry.getValue() != null).forEach(entry -> {
            CompoundNBT tag = new CompoundNBT();
            entry.getValue().writeToNBT(tag);
            tag_list_players.add(tag);
        });
        nbt.put("LibraryInventory", tag_list_players);
        return nbt;
    }
}