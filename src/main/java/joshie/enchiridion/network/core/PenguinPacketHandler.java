package joshie.enchiridion.network.core;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PenguinPacketHandler implements IMessageHandler<PenguinPacket, IMessage> {
    @Override
    public IMessage onMessage(final PenguinPacket message, final MessageContext ctx) {
        if (ctx.side == Side.CLIENT) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    message.handleQueuedClient(ctx.getClientHandler());
                }
            });
        } else {
            MinecraftServer.getServer().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    message.handleQueuedServer(ctx.getServerHandler());
                }
            });
        }

        return null;
    }
}
