package enchiridion;

import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import enchiridion.api.GuideHandler;

public class Config {
	public static final String[] prefix_dft = new String[] { "guide", "manual", "pedia", "lexicon", "thaumonomicon", "mudora", "book", "routing.table", "compendium" };
	public static String[] prefixes;

	public static void init(Configuration config) {
		try {
			config.load();
			Config.prefixes = config.get("Settings", "Book Strings", prefix_dft, "This is a list of words that are considered books, and can be put in to book binders, if they are found in the unlocalized name of an item.").getStringList();
			TooltipHandler.PRINT = config.get("Settings", "Print Item Codes to the Console", false).getBoolean(false);
			if(FMLCommonHandler.instance().getSide() == Side.CLIENT) {
				GuideHandler.DEBUG_ENABLED = config.get("Settings", "Debug Mode Enabled", false).getBoolean(false);
			}
		} catch (Exception e) {
			FMLLog.getLogger().log(Level.ERROR, "Enchiridion failed to load it's config");
			e.printStackTrace();
		} finally {
			config.save();
		}
	}
}
