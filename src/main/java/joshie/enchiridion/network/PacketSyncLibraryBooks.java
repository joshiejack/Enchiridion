package joshie.enchiridion.network;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

import joshie.enchiridion.helpers.GsonServerHelper;
import joshie.enchiridion.library.LibraryHelper;
import joshie.enchiridion.library.LibraryStorage;
import joshie.enchiridion.library.ModBooks;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncLibraryBooks implements IMessage, IMessageHandler<PacketSyncLibraryBooks, IMessage> {
    public LibraryStorage storage;
    public ModBooks modBooks;
    
    public PacketSyncLibraryBooks() {}
    public PacketSyncLibraryBooks(LibraryStorage storage, ModBooks modBooks) {
        this.storage = storage;
        this.modBooks = modBooks;
    }

    @Override
    public void toBytes(ByteBuf to) {
        //Write the itemstack list to the bytebuf
        int size = storage.getBooks().size();
        to.writeInt(size);
        for(int i = 0; i < size; i++) {
            ByteBufUtils.writeItemStack(to, storage.getBooks().get(i));
        }
        
        //Write the modbooks to the buffer
        String booksJson = GsonServerHelper.getGson().toJson(modBooks);
        ByteBufUtils.writeUTF8String(to, booksJson);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        //Fetch how many stacks we have
        int size = from.readInt();
        ArrayList<ItemStack> books = new ArrayList();
        for(int i = 0; i < size; i++) {
            books.add(ByteBufUtils.readItemStack(from));
        }
        
        storage = new LibraryStorage(books);
        
        String booksJson = ByteBufUtils.readUTF8String(from);
        modBooks = GsonServerHelper.getGson().fromJson(booksJson, ModBooks.class);
    }

    @Override
    public IMessage onMessage(PacketSyncLibraryBooks message, MessageContext ctx) {
        //We are receiving this client side
        LibraryHelper.storage = message.storage;
        LibraryHelper.modBooks = message.modBooks.init();
        LibraryHelper.modBooks.registerBooks();
        
        return null;
    }
}