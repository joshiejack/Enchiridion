package joshie.enchiridion.network.packet;

import joshie.enchiridion.library.LibraryHelper;
import joshie.enchiridion.library.LibraryInventory;
import joshie.enchiridion.library.ModSupport;
import joshie.enchiridion.network.PacketHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

import static joshie.enchiridion.network.core.PacketPart.REQUEST_SIZE;

public class PacketLibraryCommand {
    private String command;

    public PacketLibraryCommand(String string) {
        command = string;
    }

    public static void encode(PacketLibraryCommand packet, PacketBuffer buf) {
        buf.writeString(packet.command);
    }

    public static PacketLibraryCommand decode(PacketBuffer buf) {
        return new PacketLibraryCommand(buf.readString(32767));
    }

    public static class Handler {
        public static void handle(PacketLibraryCommand message, Supplier<NetworkEvent.Context> ctx) {
            ServerPlayerEntity playerMP = ctx.get().getSender();
            if (playerMP != null && !(playerMP instanceof FakePlayer)) {
                //Refresh command resets the modded books allowed
                //Reset command resets whether players have received books or not
                //Clear command clears the inventory of all the players libraries
                switch (message.command) {
                    case "refresh":
                        if (!playerMP.world.isRemote) {
                            ModSupport.reset(); //Reset the modded data, then tell clients to reset it and request data
                            PacketHandler.sendToEveryone(new PacketLibraryCommand("refresh"), playerMP);
                        } else {
                            ModSupport.reset(); //Reset the modded data, then request the info from the server
                            PacketHandler.sendToServer(new PacketSyncLibraryAllowed(REQUEST_SIZE));
                        }
                        break;
                    case "reset":
                        if (!playerMP.world.isRemote) {
                            LibraryHelper.getAllInventories().forEach(LibraryInventory::reset);
                        }
                        break;
                    case "clear":
                        if (!playerMP.world.isRemote) {
                            LibraryHelper.getAllInventories().forEach(LibraryInventory::clear);
                        }
                        break;
                }
                ctx.get().setPacketHandled(true);
            }
        }
    }
}