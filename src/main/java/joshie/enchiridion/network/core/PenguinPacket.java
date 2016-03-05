package joshie.enchiridion.network.core;

import io.netty.buffer.ByteBuf;
import joshie.enchiridion.helpers.MCClientHelper;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class PenguinPacket implements IMessage {
    public abstract void handlePacket(EntityPlayer player);
    
    @Override
    public void toBytes(ByteBuf to) {}

    @Override
    public void fromBytes(ByteBuf from) {}

    public void handleQueuedClient(NetHandlerPlayClient handler) {
        handlePacket(MCClientHelper.getPlayer());
    }

    public void handleQueuedServer(NetHandlerPlayServer serverHandler) {
        handlePacket(serverHandler.playerEntity);
    }
}
