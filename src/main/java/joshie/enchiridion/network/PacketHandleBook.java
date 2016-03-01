package joshie.enchiridion.network;

import io.netty.buffer.ByteBuf;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IBookHandler;
import joshie.lib.network.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class PacketHandleBook extends PenguinPacket {
    private int slot;

    public PacketHandleBook() {}
    public PacketHandleBook(int slot) {
        this.slot = slot;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(slot);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        slot = buf.readInt();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        ItemStack stack = EnchiridionAPI.library.getLibraryInventory(player).getStackInSlot(slot);
        if (stack != null) {
            IBookHandler handler = EnchiridionAPI.library.getBookHandlerForStack(stack);
            if (handler != null) {
                handler.handle(stack, player, slot);
            }
        }
    }
}