package joshie.enchiridion;

import joshie.enchiridion.helpers.StackHelper;
import joshie.enchiridion.lib.EInfo;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import java.io.File;

@EventBusSubscriber
public class EConfig {
    public static Configuration config;
    public static final String CATEGORY_SETTINGS = "Settings";
    public static final String CATEGORY_MOD_SUPPORT = "Mod Support";
    public static boolean allowDataAndImagesFromServers;
    public static boolean syncDataAndImagesToClients;
    public static boolean debugMode = false;
    public static boolean enableEditing;
    public static boolean resourceReload;
    public static boolean offlineMode;
    public static boolean libraryAsItem;
    public static boolean libraryAsHotkey;
    public static boolean addWrittenBookRecipeForLibrary;
    public static boolean addOreDictionaryRecipeForLibrary;
    public static String defaultText = "";
    private static String defaultItem = "";

    public static int editorXPos = -100;
    public static int toolbarYPos;
    public static int timelineYPos;
    public static int layersXPos;

    public static void init(File configFile) {
        if (config == null) {
            config = new Configuration(configFile);
            loadConfiguration();
        }
    }

    private static void loadConfiguration() {
        try {
            enableEditing = config.getBoolean("Enable Editing", CATEGORY_SETTINGS, true, "Enables editing of books that aren't locked");
            resourceReload = config.getBoolean("Reload Resources", CATEGORY_SETTINGS, false, "Reloads resources whenever you change a books icon, causes long delays, the more mods you have the longer");
            offlineMode = config.getBoolean("Library Offline Mode", CATEGORY_SETTINGS, false, "Uses the offline player to load library data");
            defaultText = config.getString("Default Text", CATEGORY_SETTINGS, "Lorem ipsum dolor sit amet.", "Sets the default text when creating a text feature");
            defaultItem = config.getString("Default Item", CATEGORY_SETTINGS, "minecraft:iron_sword", "Sets the default item when creating an item feature");
            toolbarYPos = config.getInt("Toolbar Y Pos", CATEGORY_SETTINGS, -30, -1000, 1000, "This is the y position at which to render the toolbar (top bar)");
            timelineYPos = config.getInt("Timeline Y Pos", CATEGORY_SETTINGS, 255, -1000, 1000, "This is the y position at which to render the timeline (bottom bar)");
            layersXPos = config.getInt("Layers X Pos", CATEGORY_SETTINGS, 445, -5000, 5000, "This is the x position at which to render the layers (right bar)");
            allowDataAndImagesFromServers = config.getBoolean("Allow JSON and Image Syncing from Servers for Custom Books", CATEGORY_SETTINGS, false, "Enabling this will allow servers to keep you up to date with the books that they have on them, remember this will only work if the servers have it enabled, this will sync all the json and data, this can use up more data than you may like especially if servers are suspicious and sending you hundreds of huge files, so it can be very dangerous, only enable it on servers that you can 100% trust. You have been warned. They can only send jpeg, jpg, png, gif and json files");
            syncDataAndImagesToClients = config.getBoolean("Send JSON and Images to clients for Custom Books", CATEGORY_SETTINGS, false, "Enabling this will sync your books and images for them to any clients. Keep in mind this will require clients to have the syncing to true too for it to work. Also beware that if you have a lot of images, it could use up a lot of bandwidth. Syncing sends the hash of all the files that you have for books to clients, then clients will request the files that they are missing. With malicious clients they could potentially keep deleting files and requesting them everytime they join, which could use up further bandwidth. So please only enable this if you are 100% sure all your users can be trusted. You have been warned.");
            libraryAsItem = config.getBoolean("Enable Library as Item", CATEGORY_SETTINGS, true, "The library can be opened with an item if this is true");
            libraryAsHotkey = config.getBoolean("Enable Library as Hotkey", CATEGORY_SETTINGS, true, "The library can be opened with a hotkey if this is true");
            addWrittenBookRecipeForLibrary = config.getBoolean("Add a Recipe for the Library Item using Written Books", CATEGORY_SETTINGS, false, "Disabling this will not add the default recipe for the library in written book form");
            addOreDictionaryRecipeForLibrary = config.getBoolean("Add a Recipe for the Library Item using Any Books", CATEGORY_SETTINGS, true, "Disabling this will not add the default recipe for the library, note this overrides the other recipe");
            if (debugMode) {
                allowDataAndImagesFromServers = true;
                syncDataAndImagesToClients = true;
            }

        } catch (Exception e) {
            Enchiridion.log(Level.ERROR, EInfo.MODNAME + " failed to load it's config");
            e.printStackTrace();
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }

    @SubscribeEvent
    public static void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equalsIgnoreCase(EInfo.MODID)) {
            loadConfiguration();
        }
    }

    private static ItemStack stack = ItemStack.EMPTY;

    @Nonnull
    public static ItemStack getDefaultItem() {
        if (stack.isEmpty()) stack = StackHelper.getStackFromString(defaultItem);
        if (stack.isEmpty()) stack = new ItemStack(Items.IRON_SWORD);
        return stack;
    }
}