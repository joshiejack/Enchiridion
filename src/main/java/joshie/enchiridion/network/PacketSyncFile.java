package joshie.enchiridion.network;

import io.netty.buffer.ByteBuf;
import joshie.enchiridion.helpers.FileHelper;
import joshie.enchiridion.helpers.SyncHelper;
import joshie.enchiridion.network.core.PacketPart;
import joshie.enchiridion.network.core.PacketSyncByteArray;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.File;
import java.io.FileOutputStream;

import static joshie.enchiridion.network.core.PacketPart.REQUEST_DATA;
import static joshie.enchiridion.network.core.PacketPart.SEND_DATA;

public class PacketSyncFile extends PacketSyncByteArray {
    public static class ByteWrapper {
        public byte[][] bites;
    }

    private String directory;
    private int length;

    public PacketSyncFile() {
    }

    public PacketSyncFile(String directory, PacketPart type) {
        super(type);
        this.directory = directory;
    }

    public PacketSyncFile(String directory, PacketPart type, int length) {
        this.directory = directory;
        this.part = type;
        this.length = length;
    }

    public PacketSyncFile(String directory, PacketPart type, int index, byte[] bites) {
        this.directory = directory;
        this.part = type;
        this.length = index;
        this.bites = bites;
    }

    @Override
    public void toBytes(ByteBuf to) {
        ByteBufUtils.writeUTF8String(to, directory);
        to.writeInt(length);
        super.toBytes(to);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        directory = ByteBufUtils.readUTF8String(from);
        length = from.readInt();
        super.fromBytes(from);
    }

    @Override
    public void receivedHashcode(EntityPlayer player) {
    }

    @Override
    public void receivedLengthRequest(EntityPlayer player) {
    }

    @Override
    public void receivedStringLength(EntityPlayer player) {
        byte[][] bites = new byte[length][];
        SyncHelper.bytesClient.put(directory, bites);
        PacketHandler.sendToServer(new PacketSyncFile(directory, REQUEST_DATA));
    }

    @Override
    public void receivedDataRequest(EntityPlayer player) {
        byte[][] bites = SyncHelper.bytesServer.get(directory);
        for (int index = 0; index < bites.length; index++) {
            PacketHandler.sendToClient(new PacketSyncFile(directory, SEND_DATA, index, bites[index]), player);
        }
    }

    @Override
    public void receivedData(EntityPlayer player) {
        byte[][] bites = SyncHelper.bytesClient.get(directory);
        bites[length] = this.bites;

        //If we are missing any bytes run away
        for (byte[] b : bites) {
            if (b == null) return;
        }


        try {
            //Combined all exist byte arrays
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            for (byte[] b : bites) stream.write(b);
            byte[] combined = stream.toByteArray();
            stream.close();

            File file = new File(FileHelper.getBooksDirectory(), directory);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(combined);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}