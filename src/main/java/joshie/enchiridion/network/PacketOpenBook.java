package joshie.enchiridion.network;

import io.netty.buffer.ByteBuf;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.network.core.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketOpenBook extends PenguinPacket {
    private String bookID;
    private int page;

    public PacketOpenBook() {
    }

    public PacketOpenBook(String bookID, int page) {
        this.bookID = bookID;
        this.page = page;
    }

    @Override
    public void toBytes(ByteBuf to) {
        ByteBufUtils.writeUTF8String(to, bookID);
        to.writeInt(page);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        bookID = ByteBufUtils.readUTF8String(from);
        page = from.readInt();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        EnchiridionAPI.instance.openBook(player, bookID, page);
    }
}