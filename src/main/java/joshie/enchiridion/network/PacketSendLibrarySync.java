package joshie.enchiridion.network;

import io.netty.buffer.ByteBuf;
import joshie.enchiridion.helpers.GsonClientHelper;
import joshie.enchiridion.library.LibraryDataClient;
import joshie.enchiridion.library.LibraryStorage;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

/** Fired by a server, sending the librarydata to the client**/
public class PacketSendLibrarySync implements IMessage, IMessageHandler<PacketSendLibrarySync, IMessage> {
    String libraryJson;

    public PacketSendLibrarySync() {}

    public PacketSendLibrarySync(String libraryJson) {}

    @Override
    public void toBytes(ByteBuf to) {
        ByteBufUtils.writeUTF8String(to, libraryJson);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        libraryJson = ByteBufUtils.readUTF8String(from);
    }

    @Override
    public IMessage onMessage(PacketSendLibrarySync message, MessageContext ctx) {
        LibraryDataClient.storage = GsonClientHelper.getGson().fromJson(message.libraryJson, LibraryStorage.class);
        return null;
    }
}