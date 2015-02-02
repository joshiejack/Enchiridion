package joshie.enchiridion.network;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

import joshie.enchiridion.library.LibraryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketLibraryCommand implements IMessage, IMessageHandler<PacketLibraryCommand, IMessage> {
    public PacketLibraryCommand() {}

    @Override
    public void toBytes(ByteBuf to) {}

    @Override
    public void fromBytes(ByteBuf from) {}

    @Override
    public IMessage onMessage(PacketLibraryCommand message, MessageContext ctx) {
        //Command Received
        LibraryHelper.data.reloadBooks();
        for(World world: MinecraftServer.getServer().worldServers) {
            for(EntityPlayer player: (ArrayList<EntityPlayer>)world.playerEntities) {
                if(player instanceof EntityPlayerMP) {
                    LibraryHelper.data.updateClient((EntityPlayerMP) player);
                }
            }
        }

        return null;
    }
}
