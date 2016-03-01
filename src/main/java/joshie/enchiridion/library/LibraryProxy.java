package joshie.enchiridion.library;

import net.minecraft.entity.player.EntityPlayer;

public abstract class LibraryProxy {
    public abstract LibraryInventory getLibraryInventory(EntityPlayer player);
}