package enchiridion;

import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import enchiridion.api.GuiGuide;

public class Config {
	public static boolean display_nbt;
	public static boolean binder_enabled;
	public static boolean binder_recipe;
	public static boolean enable_autopick;
	
	public static String[] book_stacks_remove;
	public static String[] book_strings_remove;
	
	public static String[] book_stacks_add;
	public static String[] book_strings_add;
	
	public static void init(Configuration config) {
		try {
			config.load();
			Config.enable_autopick = config.get("Settings", "Enable Autopickup with Bookbinder", true).getBoolean(true);
			Config.binder_enabled = config.get("Settings", "Enable Book Binder", true).getBoolean(true);
			Config.binder_recipe = config.get("Settings", "Enable Book Binder Recipe", true).getBoolean(true);
			Config.DEBUG_ENABLED = config.get("Settings", "Debug Mode Enabled", false).getBoolean(false);
			book_stacks_remove = config.get("Settings", "Book ItemStacks > Removals", BookBinderHelper.DFT_STACK_REMOVALS, "This is a list of itemstacks in form of: (itemName meta nbtTags) that cannot be used with the book binder whatsoever").getStringList();
			book_strings_remove = config.get("Settings", "Book Strings > Removals", BookBinderHelper.DFT_STRING_REMOVALS, "This is a list of strings, or registered names that cannot be used with the book binder whatsoever").getStringList();
			book_stacks_add = config.get("Settings", "Book ItemStacks > Additions", BookBinderHelper.DFT_STACK_ADDITIONS, "This is a list itemstacks in form of: (itemName meta nbtTags) for books that are accepted by the book binder.").getStringList();
			book_strings_add = config.get("Settings", "Book Strings > Additions", BookBinderHelper.DFT_STRING_ADDITIONS, "This is a list of additional words or registered names for books that are accepted by the book binder, this is a partial match, as long as the name contains this it will be accepted").getStringList();
			BookBinderHelper.spawn_binder = config.get("Settings", "Preloaded Binder > Enable", false, "Enabling this will spawn you in the world with a Book Binder").getBoolean(false);
			BookBinderHelper.setPreloadedBooks(config.get("Settings", "Preloaded Binder > Book List", new String[] {}, "Add a list of itemstacks here in the form of: (itemName meta nbtTags), feel free to omit meta or nbt").getStringList());
			TooltipHandler.PRINT = config.get("Settings", "Print Item Codes to the Console", false).getBoolean(false);
			
			if(FMLCommonHandler.instance().getSide() == Side.CLIENT) {
				Config.display_nbt = config.get("Settings", "Debug Mode > Display NBT", true).getBoolean(false);
				GuiGuide.loopRate = config.get("Settings", "Icon Update Rate", 96).getInt();
			}
		} catch (Exception e) {
			FMLLog.getLogger().log(Level.ERROR, "Enchiridion failed to load it's config");
			e.printStackTrace();
		} finally {
			config.save();
		}
	}

	public static boolean DEBUG_ENABLED;
}
