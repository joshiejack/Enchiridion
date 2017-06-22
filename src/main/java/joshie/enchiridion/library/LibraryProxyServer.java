package joshie.enchiridion.library;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import java.util.Collection;

public class LibraryProxyServer extends LibraryProxy {
    private LibrarySavedData data;
    
    public LibraryProxyServer(World world) {
        data = (LibrarySavedData) world.loadData(LibrarySavedData.class, LibrarySavedData.DATA_NAME);
        if (data == null) {
            data = new LibrarySavedData(LibrarySavedData.DATA_NAME);
            world.setData(LibrarySavedData.DATA_NAME, data);
        }
    }
    
    @Override
    public LibraryInventory getLibraryInventory(EntityPlayer player) {
        return data.getLibraryContents((EntityPlayerMP) player);
    }
    
    public Collection<LibraryInventory> getAllInventories() {
        return data.getPlayerData();
    }
    
    public void markDirty() {
        data.markDirty();
    }
}