package joshie.enchiridion.network;

import joshie.enchiridion.lib.EInfo;
import joshie.lib.network.PenguinNetwork;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
    private static final PenguinNetwork INSTANCE = new PenguinNetwork(EInfo.MODID);

    public static void registerPacket(Class clazz, Side side) {
        INSTANCE.registerPacket(clazz, side);
    }

    public static void sendToClient(IMessage message, EntityPlayerMP player) {
        INSTANCE.sendToClient(message, player);
    }

    public static void sendToServer(IMessage message) {
        INSTANCE.sendToServer(message);
    }
}