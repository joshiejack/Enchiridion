package joshie.enchiridion.library;

import joshie.enchiridion.network.EPacketHandler;
import joshie.enchiridion.network.PacketRequestLibrarySync;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class LibraryOnConnect {
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        //If our librarydata is null, we should send a request to the server for an update
        if(LibraryDataClient.storage == null) {
            EPacketHandler.sendToServer(new PacketRequestLibrarySync());
        }
    }
}
