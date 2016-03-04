package joshie.enchiridion.network;

import static joshie.lib.util.PacketPart.REQUEST_SIZE;

import joshie.enchiridion.library.ModSupport;
import joshie.lib.network.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;

public class PacketReloadLibrary extends PenguinPacket {
    public PacketReloadLibrary() {}

    @Override
    public void handlePacket(EntityPlayer player) {
        if (!player.worldObj.isRemote) {
            ModSupport.reset(); //Reset the modded data, then tell clients to reset it and request data
            PacketHandler.sendToEveryone(new PacketReloadLibrary());
        } else {
            ModSupport.reset(); //Reset the modded data, then request the info from the server
            PacketHandler.sendToServer(new PacketSyncLibraryAllowed(REQUEST_SIZE));
        }
    }
}