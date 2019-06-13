package joshie.enchiridion.network;

import joshie.enchiridion.lib.EInfo;
import joshie.enchiridion.network.packet.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(EInfo.MODID, "channel"))
            .clientAcceptedVersions(v -> true)
            .serverAcceptedVersions(v -> true)
            .networkProtocolVersion(() -> "ENCHIRIDION1")
            .simpleChannel();

    public static void registerPackets() {
        CHANNEL.registerMessage(0, PacketSyncLibraryAllowed.class, PacketSyncLibraryAllowed::encode, PacketSyncLibraryAllowed::decode, PacketSyncLibraryAllowed.Handler::handle);
        CHANNEL.registerMessage(1, PacketLibraryCommand.class, PacketLibraryCommand::encode, PacketLibraryCommand::decode, PacketLibraryCommand.Handler::handle);
        CHANNEL.registerMessage(2, PacketSyncMD5.class, PacketSyncMD5::encode, PacketSyncMD5::decode, PacketSyncMD5.Handler::handle);
        CHANNEL.registerMessage(3, PacketSyncFile.class, PacketSyncFile::encode, PacketSyncFile::decode, PacketSyncFile.Handler::handle);
        CHANNEL.registerMessage(4, PacketOpenBook.class, PacketOpenBook::encode, PacketOpenBook::decode, PacketOpenBook.Handler::handle);
        CHANNEL.registerMessage(5, PacketSyncLibraryContents.class, PacketSyncLibraryContents::encode, PacketSyncLibraryContents::decode, PacketSyncLibraryContents.Handler::handle);
        CHANNEL.registerMessage(6, PacketOpenLibrary.class, PacketOpenLibrary::encode, PacketOpenLibrary::decode, PacketOpenLibrary.Handler::handle);
        CHANNEL.registerMessage(7, PacketHandleBook.class, PacketHandleBook::encode, PacketHandleBook::decode, PacketHandleBook.Handler::handle);
        CHANNEL.registerMessage(8, PacketSetLibraryBook.class, PacketSetLibraryBook::encode, PacketSetLibraryBook::decode, PacketSetLibraryBook.Handler::handle);
    }

    public static void sendToClient(Object packet, ServerPlayerEntity playerServer) {
        CHANNEL.sendTo(packet, playerServer.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object packet) {
        CHANNEL.sendToServer(packet);
    }

    public static void sendToEveryone(Object packet, ServerPlayerEntity playerServer) {
        for (PlayerEntity player : playerServer.world.getPlayers()) {
            if (player instanceof ServerPlayerEntity) {
                CHANNEL.sendTo(packet, ((ServerPlayerEntity) player).connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
            }
        }
    }
}