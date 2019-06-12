package joshie.enchiridion.network.packet;

import joshie.enchiridion.gui.library.ContainerLibrary;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
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
                ctx.get().enqueueWork(() -> NetworkHooks.openGui(playerMP, new INamedContainerProvider() {
                    @Override
                    @Nonnull
                    public ITextComponent getDisplayName() {
                        return new StringTextComponent("Library");
                    }

                    @Override
                    public Container createMenu(int id, @Nonnull PlayerInventory playerInventory, @Nonnull PlayerEntity player) {
                        return new ContainerLibrary(id, playerInventory, player.getActiveHand());
                    }
                }));
                ctx.get().setPacketHandled(true);
            }
        }
    }
}