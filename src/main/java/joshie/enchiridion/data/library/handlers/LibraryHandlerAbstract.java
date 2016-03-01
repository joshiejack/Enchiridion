package joshie.enchiridion.data.library.handlers;

import java.util.UUID;

import joshie.enchiridion.data.library.LibraryContents;
import net.minecraft.entity.player.EntityPlayer;

public abstract class LibraryHandlerAbstract {
    public abstract LibraryContents getLibraryContents(EntityPlayer player);
    public abstract LibraryContents getLibraryContents(UUID uuid);
}