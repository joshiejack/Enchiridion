package joshie.enchiridion.library.handlers;

import io.netty.buffer.ByteBuf;
import joshie.enchiridion.EPacketHandler;
import joshie.enchiridion.api.IBookHandler;
import joshie.enchiridion.helpers.ClientHelper;
import joshie.enchiridion.library.LibraryRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

public class NetworkSwitchHandler implements IBookHandler {
    public NetworkSwitchHandler() {
        EPacketHandler.registerPacket(NetworkSwitchPacket.class, Side.SERVER);
        EPacketHandler.registerPacket(NetworkSwitchPacket.class, Side.CLIENT);
    }

    @Override
    public void handle(ItemStack stack, World world, EntityPlayer player) {
        EPacketHandler.sendToServer(new NetworkSwitchPacket(stack, true));
    }

    public static class NetworkSwitchPacket implements IMessage, IMessageHandler<NetworkSwitchPacket, IMessage> {
        boolean isSenderClient;
        ItemStack stack;

        public NetworkSwitchPacket() {}

        public NetworkSwitchPacket(ItemStack stack, boolean isSenderClient) {
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
        public IMessage onMessage(NetworkSwitchPacket message, MessageContext ctx) {
            //If the sender was the client, handler the server side element, and send a packet back to the client with the new item
            if (message.isSenderClient) {
                EntityPlayerMP player = ctx.getServerHandler().playerEntity;
                ItemStack previous = player.getCurrentEquippedItem();
                if (previous != null) previous = previous.copy();
                player.setCurrentItemOrArmor(0, message.stack);
                ItemStack ret = message.stack.getItem().onItemRightClick(message.stack, player.worldObj, player);
                player.setCurrentItemOrArmor(0, previous);
                EPacketHandler.sendToClient(new NetworkSwitchPacket(ret, false), player);
            } else {
                LibraryRegistry.overwrite(message.stack);
                EntityPlayer player = ClientHelper.getPlayer();
                ItemStack previous = player.getCurrentEquippedItem();
                if (previous != null) previous = previous.copy();
                player.setCurrentItemOrArmor(0, message.stack);
                message.stack.getItem().onItemRightClick(message.stack, player.worldObj, player);
                player.setCurrentItemOrArmor(0, previous);
            }

            return null;
        }
    }
}
