package joshie.enchiridion.network;

import joshie.enchiridion.helpers.FileHelper;
import joshie.enchiridion.helpers.MCServerHelper;
import joshie.enchiridion.helpers.SplitHelper;
import joshie.enchiridion.library.ModSupport;
import joshie.enchiridion.network.core.PacketPart;
import joshie.enchiridion.network.core.PacketSyncStringArray;
import net.minecraft.entity.player.EntityPlayer;

import static joshie.enchiridion.network.core.PacketPart.*;

public class PacketSyncLibraryAllowed extends PacketSyncStringArray {
    private static String[] client;
    private static String serverNameClient;

    public PacketSyncLibraryAllowed() {
    }

    public PacketSyncLibraryAllowed(PacketPart part) {
        super(part);
    }

    public PacketSyncLibraryAllowed(PacketPart part, String text, int index) {
        super(part, text, index);
    }

    @Override
    public void receivedHashcode(EntityPlayer player) {
        int clientHash = ModSupport.getHashcode(text);
        if (integer != clientHash) {
            PacketHandler.sendToServer(new PacketSyncLibraryAllowed(REQUEST_SIZE));
        }
    }

    @Override
    public void receivedLengthRequest(EntityPlayer player) {
        String json = FileHelper.getLibraryJson(MCServerHelper.getHostName());
        int length = SplitHelper.splitStringEvery(json, 5000).length;
        String serverName = MCServerHelper.getHostName();
        PacketHandler.sendToClient(new PacketSyncLibraryAllowed(SEND_SIZE, serverName, length), player);
    }

    @Override
    public void receivedStringLength(EntityPlayer player) {
        client = new String[integer]; //Build up the string value from the name
        serverNameClient = text; //Receive the server name
        PacketHandler.sendToServer(new PacketSyncLibraryAllowed(REQUEST_DATA));
    }

    @Override
    public void receivedDataRequest(EntityPlayer player) {
        //Grab the data and send it
        String json = FileHelper.getLibraryJson(MCServerHelper.getHostName());
        String[] server = SplitHelper.splitStringEvery(json, 5000);
        for (int i = 0; i < server.length; i++) {
            PacketHandler.sendToClient(new PacketSyncLibraryAllowed(SEND_DATA, server[i], i), player);
        }
    }

    @Override
    public void receivedData(EntityPlayer player) {
        if (client.length > integer) {
            client[integer] = text;
            //Now check if any parts are null
            boolean all = true;
            for (String s : client) {
                if (s == null) all = false;
            }

            //Received all the data
            if (all) {
                StringBuilder builder = new StringBuilder();
                for (String s : client) {
                    builder.append(s);
                }

                ModSupport.loadDataFromJson(serverNameClient, builder.toString()); //Load the data here
            }
        }
    }
}