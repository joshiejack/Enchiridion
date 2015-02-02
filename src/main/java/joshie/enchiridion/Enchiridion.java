package joshie.enchiridion;

import static java.io.File.separator;
import static joshie.enchiridion.EInfo.JAVAPATH;
import static joshie.enchiridion.EInfo.MODID;
import static joshie.enchiridion.EInfo.MODNAME;
import static joshie.enchiridion.EInfo.MODPATH;
import static joshie.enchiridion.EInfo.VERSION;

import java.io.File;

import joshie.enchiridion.library.LibraryHelper;
import joshie.enchiridion.wiki.WikiRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
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

        if (EConfig.DISABLE_AUTODISCOVERY) {
            ModContainer mod = Loader.instance().activeModContainer();
            String jar = mod.getSource().toString();
            if (jar.contains(".jar") || jar.contains(".zip")) {
                WikiRegistry.instance().registerJar(new File(jar));
            } else {
                WikiRegistry.instance().registerInDev(mod.getSource());
            }
        }
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

    @EventHandler
    public void handleIMCMessages(FMLInterModComms.IMCEvent event) {
        if (EConfig.DISABLE_AUTODISCOVERY) {
            for (FMLInterModComms.IMCMessage message : event.getMessages()) {
                if (message.key.equalsIgnoreCase("RegisterWikiMod")) {
                    String modid = message.getStringValue();
                    for (ModContainer mod : Loader.instance().getModList()) {
                        if (mod.getModId().equals(modid)) {
                            String jar = mod.getSource().toString();
                            if (jar.contains(".jar") || jar.contains(".zip")) {
                                WikiRegistry.instance().registerJar(new File(jar));
                            }
                        }
                    }
                }
            }
        }
    }
}