package joshie.enchiridion.network;

import io.netty.buffer.ByteBuf;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IBookHandler;
import joshie.enchiridion.network.core.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class PacketHandleBook extends PenguinPacket {
    private int slot;
    private boolean isShiftPressed;

    public PacketHandleBook() {}
    public PacketHandleBook(int slot, boolean isShiftPressed) {
        this.slot = slot;
        this.isShiftPressed = isShiftPressed;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(slot);
        buf.writeBoolean(isShiftPressed);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        slot = buf.readInt();
        isShiftPressed = buf.readBoolean();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        ItemStack stack = EnchiridionAPI.library.getLibraryInventory(player).getStackInSlot(slot);
        if (stack != null) {
            IBookHandler handler = EnchiridionAPI.library.getBookHandlerForStack(stack);
            if (handler != null) {
                handler.handle(stack, player, slot, isShiftPressed);
            }
        }
    }
}