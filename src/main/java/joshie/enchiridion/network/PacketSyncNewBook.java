package joshie.enchiridion.network;

import io.netty.buffer.ByteBuf;
import joshie.enchiridion.designer.BookRegistry;
import joshie.enchiridion.designer.BookRegistry.BookData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncNewBook implements IMessage, IMessageHandler<PacketSyncNewBook, IMessage> {
    String tag;

    public PacketSyncNewBook() {}
    public PacketSyncNewBook(String tag) {
        this.tag = tag;
    }

    @Override
    public void toBytes(ByteBuf to) {
        ByteBufUtils.writeUTF8String(to, tag);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        tag = ByteBufUtils.readUTF8String(from);
    }

    @Override
    public IMessage onMessage(PacketSyncNewBook message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        player.getCurrentEquippedItem().setTagCompound(new NBTTagCompound());
        player.getCurrentEquippedItem().stackTagCompound.setString("identifier", message.tag);
        BookRegistry.register(new BookData(message.tag));

        return null;
    }
}