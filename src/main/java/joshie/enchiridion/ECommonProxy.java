package joshie.enchiridion;

import java.lang.reflect.Field;

import joshie.enchiridion.designer.ItemBook;
import joshie.enchiridion.library.LibraryLoadEvent;
import joshie.enchiridion.library.mods.BotaniaCommon;
import joshie.enchiridion.network.EPacketHandler;
import joshie.enchiridion.network.PacketNetworkSwitch;
import joshie.enchiridion.network.PacketRequestLibrarySync;
import joshie.enchiridion.network.PacketSendLibrarySync;
import joshie.enchiridion.network.PacketSyncNewBook;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

public class ECommonProxy {
    public static Item book;

    public void preInit() {
        /** Register network packets for handling the network switch book handler **/
        EPacketHandler.registerPacket(PacketNetworkSwitch.class, Side.SERVER);
        EPacketHandler.registerPacket(PacketNetworkSwitch.class, Side.CLIENT);
        /** Register packets for sending and receiving nbttaglist **/
        EPacketHandler.registerPacket(PacketSendLibrarySync.class, Side.CLIENT);
        EPacketHandler.registerPacket(PacketRequestLibrarySync.class, Side.SERVER);

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

        /** Register the world load handler **/
        MinecraftForge.EVENT_BUS.register(new LibraryLoadEvent());

        /** PreInit Everything Client Side **/
        preClient();
    }

    public void initClient() {
        if (Loader.isModLoaded("Botania")) {
            BotaniaCommon.INSTANCE.init();
        }
    }

    public void postClient() {}
    public void preClient() {}
}
