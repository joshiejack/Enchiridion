package enchiridion;

import java.util.logging.Level;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.Configuration;
import enchiridion.api.GuideHandler;


public class Config {
	public static final String[] prefix_dft = new String[] { "guide", "manual", "pedia", "lexicon", "thaumonomicon", "mudora", "book", "routing.table", "compendium" };
	public static String[] prefixes;
	
	public static void init(Configuration config) {
		try {
			config.load();
            Enchiridion.id = config.getItem("Enchiridion Items", 28999).getInt();
			TooltipHandler.PRINT = config.get("Settings", "Print Item Codes to the Console", false).getBoolean(false);
			Config.prefixes = config.get("Settings", "Book Strings", prefix_dft, "This is a list of words that are considered books, and can be put in to book binders, if they are found in the unlocalized name of an item.").getStringList();
			if(FMLCommonHandler.instance().getSide() == Side.CLIENT) {
				GuideHandler.DEBUG_ENABLED = config.get("Settings", "Debug Mode Enabled", false).getBoolean(false);
			}
		} catch (Exception e) {
            BookLogHandler.log(Level.WARNING, "Failed to load Enchiridion config correctly");
            e.printStackTrace();
		} finally {
			config.save();
		}
	}
}
