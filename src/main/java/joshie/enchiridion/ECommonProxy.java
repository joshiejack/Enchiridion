package joshie.enchiridion;

import static joshie.enchiridion.Enchiridion.instance;
import joshie.enchiridion.api.EnchiridionHelper;
import joshie.enchiridion.designer.BookRegistry;
import joshie.enchiridion.designer.ItemBook;
import joshie.enchiridion.designer.BookRegistry.BookData;
import joshie.enchiridion.library.LibraryRegistry;
import joshie.enchiridion.library.ModBooks;
import joshie.enchiridion.library.handlers.BotaniaBookHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class ECommonProxy {
    public static Item book;
    
    public void preInit() {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new EGuiHandler());
        EnchiridionHelper.bookRegistry = LibraryRegistry.INSTANCE;
        
        if(EConfig.ENABLE_BOOKS) {
            book = new ItemBook().setCreativeTab(CreativeTabs.tabMisc).setHasSubtypes(true).setUnlocalizedName("book");
            GameRegistry.registerItem(book, "book");
            
            //Register a dummy book
            BookRegistry.register(new BookData("enchiridion.introbook", "Introduction Book", null, 0xFFFFFF));
        }
    }
    
    public void init() {
        LibraryRegistry.INSTANCE.initRegistry();
        ModBooks.init();
        
        if(Loader.isModLoaded("Botania")) {
            MinecraftForge.EVENT_BUS.register(new BotaniaBookHandler());
        }
    }
    
    public void postInit() {
        LibraryRegistry.INSTANCE.load();
        setupClient();
    }

    public void setupClient() {}
}
