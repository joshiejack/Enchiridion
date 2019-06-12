package joshie.enchiridion.network.packet;

import joshie.enchiridion.lib.GuiIDs;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.function.Supplier;

public class PacketOpenLibrary {

    public PacketOpenLibrary() {
    }

    public static void encode(PacketOpenLibrary packet, PacketBuffer buf) {
    }

    public static PacketOpenLibrary decode(PacketBuffer buf) {
        return new PacketOpenLibrary();
    }

    public static class Handler {
        public static void handle(PacketOpenLibrary message, Supplier<NetworkEvent.Context> ctx) {
            ServerPlayerEntity playerMP = ctx.get().getSender();
            if (playerMP != null && !(playerMP instanceof FakePlayer)) {
                ctx.get().enqueueWork(() -> NetworkHooks.openGui(playerMP, GuiIDs.LIBRARY));
                ctx.get().setPacketHandled(true);
            }
        }
    }
}