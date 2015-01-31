package joshie.enchiridion.network;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

import joshie.enchiridion.helpers.GsonClientHelper;
import joshie.enchiridion.library.LibraryDataClient;
import joshie.enchiridion.library.LibraryStorage;
import joshie.enchiridion.library.handlers.BotaniaBookHandler;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

/** Fired by a server, sending the librarydata to the client**/
public class PacketSendLibrarySync implements IMessage, IMessageHandler<PacketSendLibrarySync, IMessage> {
    String handlerData;
    ArrayList<ItemStack> stacks;

    public PacketSendLibrarySync() {}
    public PacketSendLibrarySync(String handlerData, ArrayList<ItemStack> stacks) {
        this.handlerData = handlerData;
        this.stacks = stacks;
    }

    @Override
    public void toBytes(ByteBuf to) {
        ByteBufUtils.writeUTF8String(to, handlerData);
        to.writeInt(stacks.size());
        for (int i = 0; i < stacks.size(); i++) {
            ByteBufUtils.writeItemStack(to, stacks.get(i));
        }
    }

    @Override
    public void fromBytes(ByteBuf from) {
        handlerData = ByteBufUtils.readUTF8String(from);
        stacks = new ArrayList();
        int amount = from.readInt();
        for (int i = 0; i < amount; i++) {
            stacks.add(ByteBufUtils.readItemStack(from));
        }
    }

    @Override
    public IMessage onMessage(PacketSendLibrarySync message, MessageContext ctx) {
        LibraryDataClient.storage = GsonClientHelper.getGson().fromJson(message.handlerData, LibraryStorage.class);
        LibraryDataClient.storage.setBooks(message.stacks);
        if (Loader.isModLoaded("Botania")) {
            BotaniaBookHandler.updateIsAlfheim();
        }

        return null;
    }
}