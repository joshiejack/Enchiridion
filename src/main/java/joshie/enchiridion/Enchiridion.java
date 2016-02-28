package joshie.enchiridion;

import static java.io.File.separator;
import static joshie.enchiridion.lib.EInfo.DEPENDENCIES;
import static joshie.enchiridion.lib.EInfo.INITIALS;
import static joshie.enchiridion.lib.EInfo.JAVAPATH;
import static joshie.enchiridion.lib.EInfo.MODID;
import static joshie.enchiridion.lib.EInfo.MODNAME;
import static joshie.enchiridion.lib.EInfo.MODPATH;
import static joshie.enchiridion.lib.EInfo.VERSION;

import java.io.File;

import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = MODID, name = MODNAME, version = VERSION, dependencies = DEPENDENCIES)
public class Enchiridion {
    @SidedProxy(clientSide = JAVAPATH + INITIALS + "ClientProxy", serverSide = JAVAPATH + INITIALS + "CommonProxy")
    public static ECommonProxy proxy;

    @Instance(MODID)
    public static Enchiridion instance;
    public static File root;
    
    @EventHandler
    public void onConstruction(FMLConstructionEvent event) {
    	proxy.onConstruction();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        root = new File(event.getModConfigurationDirectory() + separator + MODPATH);
        EConfig.init();
        proxy.preInit();
    }

    //Universal helper translation
	public static String translate(String string) {
		return StatCollector.translateToLocal("enchiridion." + string);
	}
}