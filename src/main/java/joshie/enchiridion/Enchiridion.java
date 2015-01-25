package joshie.enchiridion;

import static java.io.File.separator;
import static joshie.enchiridion.EInfo.JAVAPATH;
import static joshie.enchiridion.EInfo.MODID;
import static joshie.enchiridion.EInfo.MODNAME;
import static joshie.enchiridion.EInfo.MODPATH;
import static joshie.enchiridion.EInfo.VERSION;

import java.io.File;

import joshie.enchiridion.library.LibraryRegistry;
import joshie.enchiridion.library.ModBooks;
import joshie.enchiridion.wiki.WikiRegistry;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.FMLEventChannel;

@Mod(modid = MODID, name = MODNAME, version = VERSION)
public class Enchiridion {
    @SidedProxy(clientSide = JAVAPATH + "EClientProxy", serverSide = JAVAPATH + "ECommonProxy")
    public static ECommonProxy proxy;

    @Instance(MODID)
    public static Enchiridion instance;
    public static File root;
    
    public static FMLEventChannel channel;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        root = new File(event.getModConfigurationDirectory() + separator + MODPATH);
        EConfig.init(new Configuration(new File(root + File.separator + "enchiridion2.cfg")));
        proxy.preInit();
        
        //Search through all the mods for relevant pages
        WikiRegistry.instance().registerMods();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
        ModBooks.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }

    @EventHandler
    public void onReceiveIntercomMessage(FMLInterModComms.IMCEvent event) {
        
    }
}