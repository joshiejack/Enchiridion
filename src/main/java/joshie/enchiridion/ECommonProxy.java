package joshie.enchiridion;

import static joshie.enchiridion.Enchiridion.instance;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.data.library.handlers.EnchiridionBookHandler;
import joshie.enchiridion.data.library.handlers.RightClickBookHandler;
import joshie.enchiridion.gui.GuiHandler;
import joshie.enchiridion.items.ItemBook;
import joshie.enchiridion.library.LibraryEvents;
import joshie.enchiridion.library.LibraryRegistry;
import joshie.enchiridion.network.PacketHandleBook;
import joshie.enchiridion.network.PacketHandler;
import joshie.enchiridion.network.PacketSyncLibrary;
import joshie.enchiridion.util.ECreativeTab;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class ECommonProxy {
    public static Item book;
    
    public void preInit() {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
        book = new ItemBook().setCreativeTab(ECreativeTab.enchiridion).setHasSubtypes(true).setUnlocalizedName("book");
        EnchiridionAPI.instance = new EAPIHandler();
        EnchiridionAPI.library = new LibraryRegistry();
        EnchiridionAPI.library.registerBookHandler(new EnchiridionBookHandler());
        EnchiridionAPI.library.registerBookHandler(new RightClickBookHandler());
        EnchiridionAPI.library.registerBookHandlerForStack("enchiridion", new ItemStack(book), false, false);
        
        //Register events
        FMLCommonHandler.instance().bus().register(new LibraryEvents());
        MinecraftForge.EVENT_BUS.register(new LibraryEvents());
        
        //Register packets
        PacketHandler.registerPacket(PacketSyncLibrary.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketHandleBook.class, Side.SERVER);
        
        //Prepare the client for shizz
        setupClient();
    }

    public void setupClient() {}

	public void onConstruction() {}
}