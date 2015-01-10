package joshie.enchiridion;

import static java.io.File.separator;
import static joshie.enchiridion.lib.EnchiridionInfo.JAVAPATH;
import static joshie.enchiridion.lib.EnchiridionInfo.MODID;
import static joshie.enchiridion.lib.EnchiridionInfo.MODNAME;
import static joshie.enchiridion.lib.EnchiridionInfo.MODPATH;

import java.io.File;

import joshie.enchiridion.library.ModBooks;
import joshie.enchiridion.wiki.WikiRegistry;
import joshie.enchiridion.wiki.data.Data;
import joshie.enchiridion.wiki.data.WikiData;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = MODID, name = MODNAME)
public class Enchiridion {
    @SidedProxy(clientSide = JAVAPATH + "EClientProxy", serverSide = JAVAPATH + "ECommonProxy")
    public static ECommonProxy proxy;

    @Instance(MODID)
    public static Enchiridion instance;
    public static File root;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        root = new File(event.getModConfigurationDirectory() + separator + MODPATH);
        proxy.preInit();

        WikiRegistry.instance().registerMod(MODID, MODPATH);
        WikiData.instance().addData("enchiridion.default.basics.menu.en_US", new Data("Menu"));
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
        for (FMLInterModComms.IMCMessage message : event.getMessages()) {
            if (message.key.equalsIgnoreCase("registerMod")) {
                if (message.isStringMessage()) {
                    String value = message.getStringValue();
                    String[] split = value.split("$");
                    if(split.length == 2) {
                        WikiRegistry.instance().registerMod(split[0], split[1]);
                    } else {
                        WikiRegistry.instance().registerMod(value, "assets/" + value + "/wiki/");
                    }
                }
            }
        }
    }
}