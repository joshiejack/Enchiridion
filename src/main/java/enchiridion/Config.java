package enchiridion;

import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;
import enchiridion.api.GuideHandler;

public class Config {
	public static void init(Configuration config) {
		try {
			config.load();
			TooltipHandler.PRINT = config.get("Settings", "Print Item Codes to the Console", false).getBoolean(false);
			GuideHandler.DEBUG_ENABLED = config.get("Settings", "Debug Mode Enabled", false).getBoolean(false);
		} catch (Exception e) {
			FMLLog.getLogger().log(Level.ERROR, "Enchiridion failed to load it's config");
			e.printStackTrace();
		} finally {
			config.save();
		}
	}
}
