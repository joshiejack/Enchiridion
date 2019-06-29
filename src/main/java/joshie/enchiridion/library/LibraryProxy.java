package joshie.enchiridion.library;

import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nullable;

public abstract class LibraryProxy {
    public abstract LibraryInventory getLibraryInventory(@Nullable PlayerEntity player);
}