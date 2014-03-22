package enchiridion;

import java.io.File;

import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import enchiridion.api.GuideHandler;

@Mod(modid = "Enchiridion", name = "Enchiridion")
public class Enchiridion {
	public static final String modid = "books";
	public static Item items;

	@SidedProxy(clientSide = "enchiridion.ClientProxy", serverSide = "enchiridion.CommonProxy")
	public static CommonProxy proxy;

	@Mod.Instance("Enchiridion")
	public static Enchiridion instance = new Enchiridion();
	
	//Root folder
	public static File root;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		root = new File(event.getModConfigurationDirectory() + File.separator + "books");
		Config.init(new Configuration(new File(root + File.separator + "enchiridion.cfg")));
		items = new ItemEnchiridion().setUnlocalizedName("item");
		GameRegistry.registerItem(items, "items");
		proxy.preInit();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
		if(GuideHandler.DEBUG_ENABLED) {
			MinecraftForge.EVENT_BUS.register(new TooltipHandler());
		}
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit();
	}
}