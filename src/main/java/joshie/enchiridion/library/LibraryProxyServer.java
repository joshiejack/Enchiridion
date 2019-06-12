package joshie.enchiridion.library;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.ServerWorld;

import java.util.Collection;

public class LibraryProxyServer extends LibraryProxy {
    private LibrarySavedData data;

    public LibraryProxyServer(ServerWorld world) {
        data = (LibrarySavedData) world.loadData(LibrarySavedData.class, LibrarySavedData.DATA_NAME);
        if (data == null) {
            data = new LibrarySavedData(LibrarySavedData.DATA_NAME);
            world.setData(LibrarySavedData.DATA_NAME, data);
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