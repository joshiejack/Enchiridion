package joshie.enchiridion;

import static java.io.File.separator;
import static joshie.enchiridion.EInfo.JAVAPATH;
import static joshie.enchiridion.EInfo.MODID;
import static joshie.enchiridion.EInfo.MODNAME;
import static joshie.enchiridion.EInfo.MODPATH;
import static joshie.enchiridion.EInfo.VERSION;

import java.io.File;

import joshie.enchiridion.library.LibraryHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = MODID, name = MODNAME, version = VERSION)
public class Enchiridion {
    @SidedProxy(clientSide = JAVAPATH + "EClientProxy", serverSide = JAVAPATH + "ECommonProxy")
    public static ECommonProxy proxy;

    @Instance(MODID)
    public static Enchiridion instance;
    public static File root;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        root = new File(event.getModConfigurationDirectory() + separator + MODPATH);
        EConfig.init(new Configuration(new File(root + File.separator + "enchiridion2.cfg")));
        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.initClient();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postClient();
    }
    
    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        ECommands.init(event.getServer().getCommandManager());
    }
    
    @EventHandler
    public void onServerStarted(FMLServerStartedEvent event) {
        LibraryHelper.init(MinecraftServer.getServer().worldServers[0]);
    }
}