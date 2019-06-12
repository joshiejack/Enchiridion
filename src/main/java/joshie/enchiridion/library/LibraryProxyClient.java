package joshie.enchiridion.library;

import joshie.enchiridion.helpers.MCClientHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LibraryProxyClient extends LibraryProxy {
    private LibraryInventory contents;

    public LibraryProxyClient() {
        contents = new LibraryInventory(MCClientHelper.getPlayer());
    }

    @Override
    public LibraryInventory getLibraryInventory(PlayerEntity player) {
        return contents;
    }
}