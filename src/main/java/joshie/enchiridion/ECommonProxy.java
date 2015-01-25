package joshie.enchiridion;

import static joshie.enchiridion.Enchiridion.instance;
import joshie.enchiridion.api.EnchiridionHelper;
import joshie.enchiridion.library.LibraryRegistry;
import joshie.enchiridion.library.ModBooks;
import joshie.enchiridion.library.handlers.BotaniaBookHandler;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.NetworkRegistry;

public class ECommonProxy {
    public void preInit() {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new EGuiHandler());
        EnchiridionHelper.bookRegistry = LibraryRegistry.INSTANCE;
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
