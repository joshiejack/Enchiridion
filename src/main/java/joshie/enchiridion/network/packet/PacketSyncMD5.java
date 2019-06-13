package joshie.enchiridion.network.packet;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.helpers.SyncHelper;
import joshie.enchiridion.network.PacketHandler;
import joshie.enchiridion.network.core.PacketPart;
import joshie.enchiridion.network.core.PacketSyncStringArray;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import org.apache.logging.log4j.Level;

import java.util.HashMap;

import static joshie.enchiridion.network.core.PacketPart.REQUEST_DATA;
import static joshie.enchiridion.network.core.PacketPart.SEND_DATA;

public class PacketSyncMD5 extends PacketSyncStringArray {
    private volatile static String[] tempClient;
    private volatile static HashMap<PlayerEntity, String[]> tempServer = new HashMap<>();

    public PacketSyncMD5(PacketPart part) {
        super(part);
    }

    public PacketSyncMD5(PacketPart part, String text, int index) {
        super(part, text, index);
    }

    public static void encode(PacketSyncMD5 packet, PacketBuffer buf) {
        toBytes(packet, buf);
    }

    public static PacketSyncMD5 decode(PacketBuffer buf) {
        PacketSyncMD5 packet = new PacketSyncMD5(PacketPart.valueOf(buf.readString(32767)), buf.readString(32767), buf.readInt());
        fromBytes(packet, buf);
        return packet;
    }

    @Override
    public void receivedStringLength(ServerPlayerEntity player) {
        //Stop the system if no images are allowed
        if (player.world.isRemote && EConfig.SETTINGS.allowDataAndImagesFromServers.get()) { //Receive the md5 list from the server and build it
            tempClient = new String[integer]; //Build up the string value from the name
            PacketHandler.sendToServer(new PacketSyncMD5(REQUEST_DATA));
            if (EConfig.SETTINGS.debugMode)
                Enchiridion.log(Level.INFO, "Client received the length of the hash string");
        } else if (EConfig.SETTINGS.syncDataAndImagesToClients.get()) {
            String[] temp = new String[integer];
            tempServer.put(player, temp);
            PacketHandler.sendToClient(new PacketSyncMD5(REQUEST_DATA), player);
            if (EConfig.SETTINGS.debugMode)
                Enchiridion.log(Level.INFO, "Server received the length of the missing hash string");
        }
    }

    @Override
    public void receivedDataRequest(ServerPlayerEntity player) {
        if (player.world.isRemote && EConfig.SETTINGS.allowDataAndImagesFromServers.get()) {
            if (EConfig.SETTINGS.debugMode) Enchiridion.log(Level.INFO, "Client received this request for data");
            for (int i = 0; i < SyncHelper.md5requests.length; i++) {
                PacketHandler.sendToServer(new PacketSyncMD5(SEND_DATA, SyncHelper.md5requests[i], i));
            }
        } else if (EConfig.SETTINGS.syncDataAndImagesToClients.get()) {
            if (EConfig.SETTINGS.debugMode) Enchiridion.log(Level.INFO, "Server received this request for data");
            for (int i = 0; i < SyncHelper.servermd5.length; i++) {
                PacketHandler.sendToClient(new PacketSyncMD5(SEND_DATA, SyncHelper.servermd5[i], i), player);
            }
        }
    }

    @Override
    public void receivedData(ServerPlayerEntity player) {
        if (player.world.isRemote && EConfig.SETTINGS.allowDataAndImagesFromServers.get()) {
            if (tempClient.length > integer) {
                tempClient[integer] = text;
                //Now check if any parts are null
                for (String s : tempClient) {
                    if (s == null) {
                        return;
                    }
                }

                //Received all the data
                StringBuilder builder = new StringBuilder();
                for (String s : tempClient) {
                    builder.append(s);
                }


                if (EConfig.SETTINGS.debugMode)
                    Enchiridion.log(Level.INFO, "Client received all data and is this is for integer " + integer);
                if (!SyncHelper.doMD5Match(tempClient)) {
                    if (EConfig.SETTINGS.debugMode)
                        Enchiridion.log(Level.INFO, "Some hash is missing, sending the missing hashes");
                    PacketHandler.sendToServer(new PacketSyncMD5(PacketPart.SEND_SIZE, "", SyncHelper.md5requests.length));
                }
            }
        } else if (tempServer.get(player) != null && tempServer.get(player).length > integer) {
            String[] temp = tempServer.get(player);
            temp[integer] = text;
            for (String s : temp) {
                if (s == null) return; //Do no continue
            }

            //Received all the data
            StringBuilder builder = new StringBuilder();
            for (String s : temp) {
                builder.append(s);
            }

            //Now that we have all the data on the server we can get the SyncHelper to send them all to the client
            if (EConfig.SETTINGS.debugMode)
                Enchiridion.log(Level.INFO, "Missing hashes finished loading, sending the files");
            SyncHelper.sendFilesToClient(player, temp);
        }
    }
}