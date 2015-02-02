package joshie.enchiridion.network;

import io.netty.buffer.ByteBuf;
import joshie.enchiridion.helpers.ClientHelper;
import joshie.enchiridion.library.LibraryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketNetworkSwitch implements IMessage, IMessageHandler<PacketNetworkSwitch, IMessage> {
    boolean isSenderClient;
    ItemStack stack;

    public PacketNetworkSwitch() {}

    public PacketNetworkSwitch(ItemStack stack, boolean isSenderClient) {
        this.stack = stack;
        this.isSenderClient = isSenderClient;
    }

    @Override
    public void toBytes(ByteBuf to) {
        to.writeBoolean(isSenderClient);
        ByteBufUtils.writeItemStack(to, stack);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        isSenderClient = from.readBoolean();
        stack = ByteBufUtils.readItemStack(from);
    }

    @Override
    public IMessage onMessage(PacketNetworkSwitch message, MessageContext ctx) {
        //If the sender was the client, handler the server side element, and send a packet back to the client with the new item
        if (message.isSenderClient) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            ItemStack previous = player.getCurrentEquippedItem();
            if (previous != null) previous = previous.copy();
            player.setCurrentItemOrArmor(0, message.stack);
            ItemStack ret = message.stack.getItem().onItemRightClick(message.stack, player.worldObj, player);
            player.setCurrentItemOrArmor(0, previous);
            EPacketHandler.sendToClient(new PacketNetworkSwitch(ret, false), player);
            LibraryHelper.data.addUnlockedBook(player, ret, message.stack);
        } else {
            EntityPlayer player = ClientHelper.getPlayer();
            ItemStack previous = player.getCurrentEquippedItem();
            if (previous != null) previous = previous.copy();
            player.setCurrentItemOrArmor(0, message.stack);
            message.stack.getItem().onItemRightClick(message.stack, player.worldObj, player);
            player.setCurrentItemOrArmor(0, previous);
            LibraryHelper.storage.overwrite(message.stack, message.stack);
        }

        return null;
    }
}
