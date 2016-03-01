package joshie.enchiridion.data.library.handlers;

import java.util.Collection;
import java.util.UUID;

import joshie.enchiridion.data.library.LibraryContents;
import joshie.enchiridion.data.library.LibrarySavedData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class LibraryHandlerServer extends LibraryHandlerAbstract {
    private LibrarySavedData data;
    
    public LibraryHandlerServer(World world) {
        data = (LibrarySavedData) world.loadItemData(LibrarySavedData.class, LibrarySavedData.DATA_NAME);
        if (data == null) {
            data = new LibrarySavedData(LibrarySavedData.DATA_NAME);
            world.setItemData(LibrarySavedData.DATA_NAME, data);
        }
    }
    
    public Collection<LibraryContents> getPlayerData() {
        return data.getPlayerData();
    }
    
    @Override
    public LibraryContents getLibraryContents(EntityPlayer player) {
        return data.getLibraryContents((EntityPlayerMP) player);
    }
    
    @Override
    public LibraryContents getLibraryContents(UUID uuid) {
        return data.getLibraryContents(uuid);
    }
    
    public void markDirty() {
        data.markDirty();
    }
}