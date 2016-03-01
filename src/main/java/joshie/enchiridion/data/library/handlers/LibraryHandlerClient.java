package joshie.enchiridion.data.library.handlers;

import java.util.UUID;

import joshie.enchiridion.data.library.LibraryContents;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LibraryHandlerClient extends LibraryHandlerAbstract {
    private LibraryContents contents;
    
    public LibraryHandlerClient() {
        contents = new LibraryContents();
    }
    
    @Override
    public LibraryContents getLibraryContents(EntityPlayer player) {
        return contents;
    }

    @Override
    public LibraryContents getLibraryContents(UUID uuid) {
        return contents;
    }
}