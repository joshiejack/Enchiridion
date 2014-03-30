package enchiridion;

import java.io.File;
import java.util.Map.Entry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import enchiridion.CustomBooks.BookInfo;
import enchiridion.api.GuideHandler;

@Mod(modid = "Enchiridion", name = "Enchiridion")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = { "Enchiridion" })
public class Enchiridion {
	public static final String modid = "books";
	public static Item items;
    public static int id;

	@SidedProxy(clientSide = "enchiridion.ClientProxy", serverSide = "enchiridion.CommonProxy")
	public static CommonProxy proxy;

	@Mod.Instance("Enchiridion")
	public static Enchiridion instance = new Enchiridion();
	
	//Root folder
	public static File root;
	
	public EventsHandler handler;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		root = new File(event.getModConfigurationDirectory() + File.separator + "books");
		Config.init(new Configuration(new File(root + File.separator + "enchiridion.cfg")));
		items = new ItemEnchiridion(id).setUnlocalizedName("items");
		GameRegistry.registerItem(items, "enchiridion:items");
		proxy.preInit();
		handler = new EventsHandler();
		GameRegistry.registerCraftingHandler(handler);
		GameRegistry.registerPlayerTracker(handler);
		MinecraftForge.EVENT_BUS.register(handler);
        NetworkRegistry.instance().registerGuiHandler(instance, proxy);
        if(FMLCommonHandler.instance().getSide() == Side.CLIENT && GuideHandler.DEBUG_ENABLED) {
			MinecraftForge.EVENT_BUS.register(new TooltipHandler());
		}
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init();
		
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(items, 1, ItemEnchiridion.BINDER), new Object[] {
			"SP", "SP", 'S', Item.silk, 'P', Item.paper
		}));
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit();
		
		if(CustomBooks.bookInfo.size() > 0) {
			for (Entry<String, BookInfo> books : CustomBooks.bookInfo.entrySet()) {
				BookInfo info = books.getValue();
				if(info.crafting != null) {
					CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(CustomBooks.create(books.getKey()), info.crafting));
				}
			}
		}
	}
}