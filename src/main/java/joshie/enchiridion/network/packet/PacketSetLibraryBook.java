package joshie.enchiridion.network.packet;

import joshie.enchiridion.api.EnchiridionAPI;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class PacketSetLibraryBook {
    private ItemStack stack;
    private int slot;

    public PacketSetLibraryBook(@Nonnull ItemStack stack, int slot) {
        this.stack = stack;
        this.slot = slot;
    }

    public static void encode(PacketSetLibraryBook packet, PacketBuffer buf) {
        buf.writeItemStack(packet.stack);
        buf.writeInt(packet.slot);
    }

    public static PacketSetLibraryBook decode(PacketBuffer buf) {
        return new PacketSetLibraryBook(buf.readItemStack(), buf.readInt());
    }

    public static class Handler {
        public static void handle(PacketSetLibraryBook message, Supplier<NetworkEvent.Context> ctx) {
            ServerPlayerEntity playerMP = ctx.get().getSender();
            if (playerMP != null && !(playerMP instanceof FakePlayer)) {
                ctx.get().enqueueWork(() -> EnchiridionAPI.library.getLibraryInventory(playerMP).setInventorySlotContents(message.slot, message.stack));
                ctx.get().setPacketHandled(true);
            }
        }
    }
}