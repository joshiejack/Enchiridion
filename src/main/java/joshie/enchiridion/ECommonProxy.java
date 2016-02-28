package joshie.enchiridion;

import static joshie.enchiridion.Enchiridion.instance;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.books.ItemBook;
import joshie.enchiridion.handlers.EAPIHandler;
import joshie.enchiridion.handlers.GuiHandler;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class ECommonProxy {
    public static Item book;
    
    public void preInit() {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
        book = new ItemBook().setCreativeTab(ECreativeTab.enchiridion).setHasSubtypes(true).setUnlocalizedName("book");
        EnchiridionAPI.instance = new EAPIHandler();
        
        //Prepare the client for shizz
        setupClient();
    }

    public void setupClient() {}

	public void onConstruction() {}
}