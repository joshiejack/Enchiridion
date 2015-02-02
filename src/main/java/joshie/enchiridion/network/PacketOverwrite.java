package joshie.enchiridion.network;

import io.netty.buffer.ByteBuf;
import joshie.enchiridion.library.LibraryHelper;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketOverwrite implements IMessage, IMessageHandler<PacketOverwrite, IMessage> {
    public boolean hasOverwrite;
    public ItemStack stack;
    public ItemStack overwrite;

    public PacketOverwrite() {}

    public PacketOverwrite(ItemStack stack, ItemStack overwrite) {
        this.stack = stack;
        this.overwrite = overwrite;
        this.hasOverwrite = overwrite != null;
    }

    @Override
    public void toBytes(ByteBuf to) {
        to.writeBoolean(hasOverwrite);
        ByteBufUtils.writeItemStack(to, stack);
        if (hasOverwrite) {
            ByteBufUtils.writeItemStack(to, overwrite);
        }
    }

    @Override
    public void fromBytes(ByteBuf from) {
        hasOverwrite = from.readBoolean();
        stack = ByteBufUtils.readItemStack(from);
        if (hasOverwrite) {
            overwrite = ByteBufUtils.readItemStack(from);
        }
    }

    @Override
    public IMessage onMessage(PacketOverwrite message, MessageContext ctx) {
        LibraryHelper.data.addUnlockedBook(ctx.getServerHandler().playerEntity, message.stack, message.overwrite);

        return null;
    }
}