package joshie.enchiridion.network;

import io.netty.buffer.ByteBuf;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.items.ItemEnchiridion;
import joshie.enchiridion.library.LibraryHelper;
import joshie.enchiridion.network.core.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;

public class PacketHandleBook extends PenguinPacket {
    private int slot;
    private boolean isShiftPressed;
    private EnumHand hand;

    public PacketHandleBook() {
    }

    public PacketHandleBook(int slot, EnumHand hand, boolean isShiftPressed) {
        this.slot = slot;
        this.hand = hand;
        this.isShiftPressed = isShiftPressed;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(slot);
        buf.writeInt(hand.ordinal());
        buf.writeBoolean(isShiftPressed);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        slot = buf.readInt();
        hand = EnumHand.values()[buf.readInt()];
        isShiftPressed = buf.readBoolean();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        ItemStack stack = EnchiridionAPI.library.getLibraryInventory(player).getStackInSlot(slot);
        if (!stack.isEmpty()) {
            ItemStack clone = stack.copy();
            if (!clone.hasTagCompound()) clone.setTagCompound(new NBTTagCompound());
            clone.getTagCompound().setBoolean(ItemEnchiridion.IS_LIBRARY, true);
            player.setHeldItem(hand, clone); //Copy the book out
            ItemStack held = player.getHeldItem(hand);
            LibraryHelper.getClientLibraryContents().setInventorySlotContents(slot, held);
        }
    }
}