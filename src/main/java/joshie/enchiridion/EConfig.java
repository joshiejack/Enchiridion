package joshie.enchiridion;

import static joshie.enchiridion.Enchiridion.root;

import java.io.File;

import org.apache.logging.log4j.Level;

import joshie.enchiridion.lib.EInfo;
import joshie.lib.helpers.StackHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

public class EConfig {
	public static final boolean RECIPE_DEBUG = true;
	public static boolean debugMode = true;
    public static boolean enableEditing = true;
	public static String defaultText = "";
	private static String defaultItem = "";
	
	public static int toolbarYPos;
	public static int timelineYPos;
	public static int layersXPos;
    
    public static void init() {
        Configuration config = new Configuration(new File(root, "enchiridion.cfg"));
        try {
            config.load();
            debugMode = config.getBoolean("Debug", "Settings", true, "Enabling debug mode, logs a bunch of extra info to the console");
            enableEditing = config.getBoolean("Enable Editing", "Settings", true, "Enables editing of books that aren't locked");
            defaultText = config.getString("Default Text", "Settings", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin id orci sed lectus interdum eleifend quis non dui. Nunc vehicula urna ac elit convallis, at auctor mi sollicitudin. Sed id est nec mauris facilisis hendrerit. Morbi ut imperdiet ligula. Aenean egestas velit quis tellus elementum, vitae viverra est ullamcorper. Class.", "Sets the default text when creating a text feature");
            defaultItem = config.getString("Default Item", "Settings", "minecraft:iron_sword", "Sets the default item when creating an item feature");
            toolbarYPos = config.getInt("Toolbar Y Pos", "Settings", -30, -1000, 1000, "This is the y position at which to render the toolbar (top bar)");
            timelineYPos = config.getInt("Timeline Y Pos", "Settings", 255, -1000, 1000, "This is the y position at which to render the timeline (bottom bar)");
            layersXPos = config.getInt("Layers X Pos", "Settings", 445, -5000, 5000, "This is the x position at which to render the layers (right bar)");
        } catch (Exception e) {
            ELogger.log(Level.ERROR, EInfo.MODNAME + " failed to load it's config");
            e.printStackTrace();
        } finally {
            config.save();
        }
    }

    private static ItemStack stack;
	public static ItemStack getDefaultItem() {
		if (stack == null) stack = StackHelper.getStackFromString(defaultItem);
		if (stack == null) stack = new ItemStack(Items.iron_sword);
		return stack;
	}

	public static int editorXPos = -100;
}