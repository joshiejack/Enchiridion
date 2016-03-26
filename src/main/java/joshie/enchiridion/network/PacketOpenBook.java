package joshie.enchiridion.network;

import io.netty.buffer.ByteBuf;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.network.core.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketOpenBook extends PenguinPacket {
    String bookid;
    int page;

    public PacketOpenBook() {}
    public PacketOpenBook(String bookid, int page) {
        this.bookid = bookid;
        this.page = page;
    }

    @Override
    public void toBytes(ByteBuf to) {
        ByteBufUtils.writeUTF8String(to, bookid);
        to.writeInt(page);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        bookid = ByteBufUtils.readUTF8String(from);
        page = from.readInt();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        EnchiridionAPI.instance.openBook(player, bookid, page);
    }
}