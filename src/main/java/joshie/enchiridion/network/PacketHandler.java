package joshie.enchiridion.network;

import joshie.enchiridion.lib.EInfo;
import joshie.lib.network.PenguinNetwork;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
    private static final PenguinNetwork INSTANCE = new PenguinNetwork(EInfo.MODID);
    
    public static void registerPacket(Class clazz) {
        registerPacket(clazz, Side.CLIENT);
        registerPacket(clazz, Side.SERVER);
    }

    public static void registerPacket(Class clazz, Side side) {
        INSTANCE.registerPacket(clazz, side);
    }

    public static void sendToClient(IMessage message, EntityPlayer player) {
        INSTANCE.sendToClient(message, (EntityPlayerMP) player);
    }

    public static void sendToServer(IMessage message) {
        INSTANCE.sendToServer(message);
    }

    public static void sendToEveryone(IMessage message) {
        INSTANCE.sendToEveryone(message);
    }
}