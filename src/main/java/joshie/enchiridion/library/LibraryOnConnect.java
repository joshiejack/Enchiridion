package joshie.enchiridion.library;

import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class LibraryOnConnect {
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        //if the player is server sided send the storage data to the client
        if (event.player instanceof EntityPlayerMP) {
            LibraryHelper.data.updateClient((EntityPlayerMP) event.player);
        }
    }
}
