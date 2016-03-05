package joshie.enchiridion;

import org.apache.logging.log4j.Level;

import joshie.enchiridion.helpers.FileHelper;
import joshie.enchiridion.helpers.StackHelper;
import joshie.enchiridion.lib.EInfo;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

public class EConfig {
    public static boolean allowDataAndImagesFromServers;
    public static boolean syncDataAndImagesToClients;
    public static boolean debugMode = false;
    public static boolean enableEditing;
    public static boolean resourceReload;
    public static boolean offlineMode;
    public static boolean libraryAsItem;
    public static boolean libraryAsHotkey;
    public static String defaultText = "";
    private static String defaultItem = "";

    public static int editorXPos = -100;
    public static int toolbarYPos;
    public static int timelineYPos;
    public static int layersXPos;

    public static void init() {
        Configuration config = new Configuration(FileHelper.getConfigFile());
        try {
            config.load();
            enableEditing = config.getBoolean("Enable Editing", "Settings", true, "Enables editing of books that aren't locked");
            resourceReload = config.getBoolean("Reload Resources", "Settings", true, "Reloads resources whenever you change a books icon, causes long delays, the more mods you have the longer");
            offlineMode = config.getBoolean("Library Offline Mode", "Settings", false, "Uses the offline player to load library data");
            defaultText = config.getString("Default Text", "Settings", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin id orci sed lectus interdum eleifend quis non dui. Nunc vehicula urna ac elit convallis, at auctor mi sollicitudin. Sed id est nec mauris facilisis hendrerit. Morbi ut imperdiet ligula. Aenean egestas velit quis tellus elementum, vitae viverra est ullamcorper. Class.", "Sets the default text when creating a text feature");
            defaultItem = config.getString("Default Item", "Settings", "minecraft:iron_sword", "Sets the default item when creating an item feature");
            toolbarYPos = config.getInt("Toolbar Y Pos", "Settings", -30, -1000, 1000, "This is the y position at which to render the toolbar (top bar)");
            timelineYPos = config.getInt("Timeline Y Pos", "Settings", 255, -1000, 1000, "This is the y position at which to render the timeline (bottom bar)");
            layersXPos = config.getInt("Layers X Pos", "Settings", 445, -5000, 5000, "This is the x position at which to render the layers (right bar)");
            allowDataAndImagesFromServers = config.getBoolean("Allow JSON and Image Syncing from Servers for Custom Books", "Settings", false, "Enabling this will allow servers to keep you up to date with the books that they have on them, remember this will only work if the servers have it enabled, this will sync all the json and data, this can use up more data than you may like especially if servers are suspicious and sending you hundreds of huge files, so it can be very dangerous, only enable it on servers that you can 100% trust. You have been warned. They can only send jpeg, jpg, png, gif and json files");
            syncDataAndImagesToClients = config.getBoolean("Send JSON and Images to clients for Custom Books", "Settings", false, "Enabling this will sync your books and images for them to any clients. Keep in mind this will require clients to have the syncing to true too for it to work. Also beware that if you have a lot of images, it could use up a lot of bandwidth. Syncing sends the hash of all the files that you have for books to clients, then clients will request the files that they are missing. With malicious clients they could potentially keep deleting files and requesting them everytime they join, which could use up further bandwidth. So please only enable this if you are 100% sure all your users can be trusted. You have been warned.");
            libraryAsItem = config.getBoolean("Enable Library as Item", "Settings", true, "The library can be opened with an item if this is true");
            libraryAsHotkey = config.getBoolean("Enable Library as Hotkey", "Settings", true, "The library can be opened with a hotkey if this is true");
            if (debugMode) {
                allowDataAndImagesFromServers = true;
                syncDataAndImagesToClients = true;
            }
            
        } catch (Exception e) {
            Enchiridion.log(Level.ERROR, EInfo.MODNAME + " failed to load it's config");
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
}