package joshie.enchiridion.library;

import net.minecraft.entity.player.PlayerEntity;

public abstract class LibraryProxy {
    public abstract LibraryInventory getLibraryInventory(PlayerEntity player);
}