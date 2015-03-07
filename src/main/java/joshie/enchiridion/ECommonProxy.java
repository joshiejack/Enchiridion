package joshie.enchiridion;

import java.lang.reflect.Field;

import joshie.enchiridion.designer.ItemBook;
import joshie.enchiridion.library.LibraryOnConnect;
import joshie.enchiridion.network.EPacketHandler;
import joshie.enchiridion.network.PacketLibraryCommand;
import joshie.enchiridion.network.PacketNetworkSwitch;
import joshie.enchiridion.network.PacketOverwrite;
import joshie.enchiridion.network.PacketSyncLibraryBooks;
import joshie.enchiridion.network.PacketSyncNewBook;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

public class ECommonProxy {
    public static Item book;

    public void preInit() {
        if (EConfig.ENABLE_WIKI) {
            /** Register network packets for handling the network switch book handler **/
            EPacketHandler.registerPacket(PacketNetworkSwitch.class, Side.SERVER);
            EPacketHandler.registerPacket(PacketNetworkSwitch.class, Side.CLIENT);
            /** Send a list of library books to the client when they login **/
            EPacketHandler.registerPacket(PacketSyncLibraryBooks.class, Side.CLIENT);
            /** Send a command to refresh the library **/
            EPacketHandler.registerPacket(PacketLibraryCommand.class, Side.SERVER);
            EPacketHandler.registerPacket(PacketOverwrite.class, Side.SERVER);
        }

        /** If we have books enabled **/
        if (EConfig.ENABLE_BOOKS) {
            EPacketHandler.registerPacket(PacketSyncNewBook.class, Side.SERVER);

            /** Create the book item and register it **/
            book = new ItemBook().setCreativeTab(CreativeTabs.tabMisc).setHasSubtypes(true).setUnlocalizedName("book");
            GameRegistry.registerItem(book, "book");

            /** Add books to the enchiridion1 creative tab, otherwise just leave in misc **/
            if (Loader.isModLoaded("Enchiridion")) {
                try {
                    Class clazz = Class.forName("enchiridion.CreativeTab");
                    Field f = clazz.getDeclaredField("books");
                    book.setCreativeTab((CreativeTabs) f.get(null));
                } catch (Exception e) {}
            }
        }

        if (EConfig.ENABLE_WIKI) {
            /** Register the handler for connecting to a world **/
            FMLCommonHandler.instance().bus().register(new LibraryOnConnect());
        }

        /** PreInit Everything Client Side **/
        preClient();
    }

    public void initClient() {}

    public void postClient() {}

    public void preClient() {}
}
