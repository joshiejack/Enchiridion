package joshie.enchiridion.network;

import io.netty.buffer.ByteBuf;
import joshie.enchiridion.helpers.GsonServerHelper;
import joshie.enchiridion.library.LibraryLoadEvent;
import joshie.enchiridion.library.LibraryStorage;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

/** Fired by a client request their librarydata **/
public class PacketRequestLibrarySync implements IMessage, IMessageHandler<PacketRequestLibrarySync, IMessage> {
    public PacketRequestLibrarySync() {}

    @Override
    public void toBytes(ByteBuf to) {
        return;
    }

    @Override
    public void fromBytes(ByteBuf from) {
        return;
    }

    @Override
    public IMessage onMessage(PacketRequestLibrarySync message, MessageContext ctx) {
        LibraryStorage storage = LibraryLoadEvent.data.getStorageFor(ctx.getServerHandler().playerEntity);
        EPacketHandler.sendToClient(new PacketSendLibrarySync(GsonServerHelper.getGson().toJson(storage)), ctx.getServerHandler().playerEntity);

        return null;
    }
}