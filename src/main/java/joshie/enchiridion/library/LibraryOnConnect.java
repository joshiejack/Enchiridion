package joshie.enchiridion.library;

import joshie.enchiridion.helpers.GsonServerHelper;
import joshie.enchiridion.network.EPacketHandler;
import joshie.enchiridion.network.PacketSendLibrarySync;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class LibraryOnConnect {
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        //if the player is server sided send the storage data to the client
        if (event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.player;
            LibraryStorage storage = LibraryLoadEvent.data.getStorageFor(player);
            EPacketHandler.sendToClient(new PacketSendLibrarySync(GsonServerHelper.getGson().toJson(storage), storage.getBooks()), player);
        }
    }
}
