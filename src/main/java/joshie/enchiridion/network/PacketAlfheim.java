package joshie.enchiridion.network;

import io.netty.buffer.ByteBuf;
import joshie.enchiridion.library.LibraryLoadEvent;
import joshie.enchiridion.library.mods.BotaniaCommon;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketAlfheim implements IMessage, IMessageHandler<PacketAlfheim, IMessage> {
    public PacketAlfheim() {}

    @Override
    public void toBytes(ByteBuf to) {
        return;
    }

    @Override
    public void fromBytes(ByteBuf from) {
        return;
    }

    @Override
    public IMessage onMessage(PacketAlfheim message, MessageContext ctx) {
        LibraryLoadEvent.data.overwrite(ctx.getServerHandler().playerEntity, BotaniaCommon.alfheim.copy());

        return null;
    }
}