package joshie.enchiridion.network.core;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

import static joshie.enchiridion.network.core.PacketPart.*;

public class PacketSyncStringArray implements IPacketArray {
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

    public static void toBytes(PacketSyncStringArray packet, PacketBuffer buf) {
        buf.writeByte(packet.part.ordinal());
        if (packet.part.sends()) {
            buf.writeInt(packet.integer);
            buf.writeString(packet.text);
        }
    }

    public static void fromBytes(PacketSyncStringArray packet, PacketBuffer buf) {
        packet.part = PacketPart.values()[buf.readByte()];
        if (packet.part.sends()) {
            packet.integer = buf.readInt();
            packet.text = buf.readString(32767);
        }
    }

    public static class Handler {
        public static void handle(PacketSyncStringArray message, Supplier<NetworkEvent.Context> ctx) {
            ServerPlayerEntity playerMP = ctx.get().getSender();
            if (playerMP != null && !(playerMP instanceof FakePlayer)) {
                if (message.part == SEND_HASH) message.receivedHashcode(playerMP);
                else if (message.part == REQUEST_SIZE) message.receivedLengthRequest(playerMP);
                else if (message.part == SEND_SIZE) message.receivedStringLength(playerMP);
                else if (message.part == REQUEST_DATA) message.receivedDataRequest(playerMP);
                else if (message.part == SEND_DATA) message.receivedData(playerMP);
            }
            ctx.get().setPacketHandled(true);
        }
    }
}