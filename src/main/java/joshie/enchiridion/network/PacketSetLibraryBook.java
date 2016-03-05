package joshie.enchiridion.network;

import io.netty.buffer.ByteBuf;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.lib.network.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketSetLibraryBook extends PenguinPacket {
    private ItemStack stack;
    private int slot;
    
    public PacketSetLibraryBook() {}
    public PacketSetLibraryBook(ItemStack stack, int slot) {
        this.stack = stack;
        this.slot = slot;
    }

    @Override
    public void toBytes(ByteBuf to) {
        ByteBufUtils.writeItemStack(to, stack);
        to.writeInt(slot);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        stack = ByteBufUtils.readItemStack(from);
        slot = from.readInt();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        EnchiridionAPI.library.getLibraryInventory(player).setInventorySlotContents(slot, stack);
    }
}
