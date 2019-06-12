package joshie.enchiridion.library;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;

import java.util.Collection;

public class LibraryProxyServer extends LibraryProxy {
    private LibrarySavedData data;

    public LibraryProxyServer(ServerWorld world) {
        DimensionSavedDataManager storage = world.getSavedData();
        data = storage.get(() -> new LibrarySavedData(LibrarySavedData.DATA_NAME), LibrarySavedData.DATA_NAME);
        if (data == null) {
            data = new LibrarySavedData(LibrarySavedData.DATA_NAME);
            storage.set(data);
        }
    }

    @Override
    public LibraryInventory getLibraryInventory(PlayerEntity player) {
        return data.getLibraryContents((ServerPlayerEntity) player);
    }

    public Collection<LibraryInventory> getAllInventories() {
        return data.getPlayerData();
    }

    public void markDirty() {
        data.markDirty();
    }
}