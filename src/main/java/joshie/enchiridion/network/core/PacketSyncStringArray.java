package joshie.enchiridion.network.core;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import static joshie.enchiridion.network.core.PacketPart.*;

public abstract class PacketSyncStringArray extends PenguinPacket {
    protected PacketPart part;
    protected String text = "";
    protected int integer = -1;

    public PacketSyncStringArray() {
    }

    public PacketSyncStringArray(PacketPart part) {
        this.part = part;
    }

    public PacketSyncStringArray(PacketPart part, String text, int index) {
        this.part = part;
        this.text = text;
        this.integer = index;
    }

    @Override
    public void toBytes(ByteBuf to) {
        to.writeByte(part.ordinal());
        if (part.sends()) {
            to.writeInt(integer);
            ByteBufUtils.writeUTF8String(to, text);
        }
    }

    @Override
    public void fromBytes(ByteBuf from) {
        part = PacketPart.values()[from.readByte()];
        if (part.sends()) {
            integer = from.readInt();
            text = ByteBufUtils.readUTF8String(from);
        }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        if (part == SEND_HASH) receivedHashcode(player);
        else if (part == REQUEST_SIZE) receivedLengthRequest(player);
        else if (part == SEND_SIZE) receivedStringLength(player);
        else if (part == REQUEST_DATA) receivedDataRequest(player);
        else if (part == SEND_DATA) receivedData(player);
    }

    /**
     * Client should do nothing or send a packet to request data
     **/
    public abstract void receivedHashcode(EntityPlayer player);

    /**
     * Server should now send the string length
     **/
    public abstract void receivedLengthRequest(EntityPlayer player);

    /**
     * Client should now send a received size packet
     **/
    public abstract void receivedStringLength(EntityPlayer player);

    /**
     * Server should now send the data for the string
     **/
    public abstract void receivedDataRequest(EntityPlayer player);

    /**
     * Client should try to build a list of the data after received all the data
     **/
    public abstract void receivedData(EntityPlayer player);
}