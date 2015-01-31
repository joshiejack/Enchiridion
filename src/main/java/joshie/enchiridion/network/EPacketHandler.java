package joshie.enchiridion.network;

import static joshie.enchiridion.EInfo.MODID;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class EPacketHandler {
    private static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
    private static int id;

    public static void registerPacket(Class clazz, Side side) {
        INSTANCE.registerMessage(clazz, clazz, id++, side);
    }

    public static void sendToClient(IMessage packet, EntityPlayerMP player) {
        INSTANCE.sendTo(packet, player);
    }

    public static void sendToServer(IMessage packet) {
        INSTANCE.sendToServer(packet);
    }
}
