package joshie.enchiridion;

import static joshie.enchiridion.Enchiridion.instance;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.data.library.LibraryEvents;
import joshie.enchiridion.handlers.EAPIHandler;
import joshie.enchiridion.handlers.GuiHandler;
import joshie.enchiridion.items.ItemBook;
import joshie.enchiridion.util.ECreativeTab;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class ECommonProxy {
    public static Item book;
    
    public void preInit() {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
        book = new ItemBook().setCreativeTab(ECreativeTab.enchiridion).setHasSubtypes(true).setUnlocalizedName("book");
        EnchiridionAPI.instance = new EAPIHandler();
        
        //Register events
        MinecraftForge.EVENT_BUS.register(new LibraryEvents());
        
        //Prepare the client for shizz
        setupClient();
    }

    public void setupClient() {}

	public void onConstruction() {}
}