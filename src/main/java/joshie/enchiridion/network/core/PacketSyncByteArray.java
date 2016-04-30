package joshie.enchiridion.network.core;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

import static joshie.enchiridion.network.core.PacketPart.*;

public abstract class PacketSyncByteArray extends PenguinPacket {
    protected PacketPart part;
    protected byte[] bites;
    
    public PacketSyncByteArray(){}
    
    public PacketSyncByteArray(PacketPart part) {
        this.part = part;
    }
    
    public PacketSyncByteArray(PacketPart part, byte[] bites) {
        this.part = part;
        this.bites = bites;
    }
    
    @Override
    public void toBytes(ByteBuf to) {
        to.writeByte(part.ordinal());
        if (part.sends()) {
            if (bites != null && bites.length > 0) {
                to.writeInt(bites.length);
                for (byte b: bites) {
                    to.writeByte(b);
                }
            } else to.writeInt(0);
        }
    }

    @Override
    public void fromBytes(ByteBuf from) {
        part = PacketPart.values()[from.readByte()];
        if (part.sends()) {
            int length = from.readInt();
            if (length != 0) {
                bites = new byte[length];
                for (int i = 0; i < length; i++) {
                    bites[i] = from.readByte();
                }
            }
        }
    }
    
    @Override
    public void handlePacket(EntityPlayer player) {
        boolean isClient = player.worldObj.isRemote;
        if (part == SEND_HASH) receivedHashcode(player);
        else if (part == REQUEST_SIZE) receivedLengthRequest(player);
        else if (part == SEND_SIZE) receivedStringLength(player);
        else if (part == REQUEST_DATA) receivedDataRequest(player);
        else if (part == SEND_DATA) receivedData(player);
    }
    
    /** Client should do nothing or send a packet to request data **/
    public abstract void receivedHashcode(EntityPlayer player);
    
    /** Server should now send the byte length **/
    public abstract void receivedLengthRequest(EntityPlayer player);
    
    /** Client should now send a received size packet **/
    public abstract void receivedStringLength(EntityPlayer player);
    
    /** Server should now send the data for the string **/
    public abstract void receivedDataRequest(EntityPlayer player);
    
    /** Client should try to build a list of the data after received all the data **/
    public abstract void receivedData(EntityPlayer player);
}
