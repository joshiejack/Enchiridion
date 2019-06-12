package joshie.enchiridion;

import joshie.enchiridion.helpers.StackHelper;
import joshie.enchiridion.lib.EInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import javax.annotation.Nonnull;

@EventBusSubscriber(modid = EInfo.MODID)
public class EConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final Settings SETTINGS = new Settings(BUILDER);

    public static class Settings {
        public static final String CATEGORY = "settings";
        public boolean debugMode = false;
        public ForgeConfigSpec.BooleanValue enableEditing;
        public ForgeConfigSpec.BooleanValue resourceReload;
        public ForgeConfigSpec.BooleanValue offlineMode;
        public ForgeConfigSpec.BooleanValue allowDataAndImagesFromServers;
        public ForgeConfigSpec.BooleanValue syncDataAndImagesToClients;
        public ForgeConfigSpec.BooleanValue libraryAsItem;
        public ForgeConfigSpec.BooleanValue libraryAsHotkey;
        public ForgeConfigSpec.BooleanValue addWrittenBookRecipeForLibrary;
        public ForgeConfigSpec.BooleanValue addOreDictionaryRecipeForLibrary;
        public ForgeConfigSpec.ConfigValue<String> defaultText;
        public ForgeConfigSpec.ConfigValue<String> defaultItem;

        public int editorXPos = -100;
        public ForgeConfigSpec.IntValue toolbarYPos;
        public ForgeConfigSpec.IntValue timelineYPos;
        public ForgeConfigSpec.IntValue layersXPos;

        public Settings(ForgeConfigSpec.Builder builder) {
            builder.push(CATEGORY);
            enableEditing = builder.comment("Enables editing of books that aren't locked").define("Enable Editing", true);
            resourceReload = builder.comment("Reloads resources whenever you change a books icon, causes long delays, the more mods you have the longer").define("Reload Resources", false);
            offlineMode = builder.comment("Uses the offline player to load library data").define("Library Offline Mode", false);
            defaultText = builder.comment("Sets the default text when creating a text feature").define("Default Text", "Lorem ipsum dolor sit amet.");
            defaultItem = builder.comment("Sets the default item when creating an item feature").define("Default Item", "minecraft:iron_sword");
            toolbarYPos = builder.comment("This is the y position at which to render the toolbar (top bar)").defineInRange("Toolbar Y Pos", -30, -1000, 1000);
            timelineYPos = builder.comment("This is the y position at which to render the timeline (bottom bar)").defineInRange("Timeline Y Pos", 255, -1000, 1000);
            layersXPos = builder.comment("This is the x position at which to render the layers (right bar)").defineInRange("Layers X Pos", 445, -5000, 5000);
            allowDataAndImagesFromServers = builder.comment("Enabling this will allow servers to keep you up to date with the books that they have on them, remember this will only work if the servers have it enabled, this will sync all the json and data, this can use up more data than you may like especially if servers are suspicious and sending you hundreds of huge files, so it can be very dangerous, only enable it on servers that you can 100% trust. You have been warned. They can only send jpeg, jpg, png, gif and json files").define("Allow JSON and Image Syncing from Servers for Custom Books", debugMode);
            syncDataAndImagesToClients = builder.comment("Enabling this will sync your books and images for them to any clients. Keep in mind this will require clients to have the syncing to true too for it to work. Also beware that if you have a lot of images, it could use up a lot of bandwidth. Syncing sends the hash of all the files that you have for books to clients, then clients will request the files that they are missing. With malicious clients they could potentially keep deleting files and requesting them everytime they join, which could use up further bandwidth. So please only enable this if you are 100% sure all your users can be trusted. You have been warned.").define("Send JSON and Images to clients for Custom Books", debugMode);
            libraryAsItem = builder.comment("The library can be opened with an item if this is true").define("Enable Library as Item", true);
            libraryAsHotkey = builder.comment("The library can be opened with a hotkey if this is true").define("Enable Library as Hotkey", true);
            addWrittenBookRecipeForLibrary = builder.comment("Disabling this will not add the default recipe for the library in written book form").define("Add a Recipe for the Library Item using Written Books", false);
            addOreDictionaryRecipeForLibrary = builder.comment("Disabling this will not add the default recipe for the library, note this overrides the other recipe").define("Add a Recipe for the Library Item using Any Books", true);
            builder.pop();
        }
    }

    public static final ForgeConfigSpec spec = BUILDER.build();

    private static ItemStack stack = ItemStack.EMPTY;

    @Nonnull
    public static ItemStack getDefaultItem() {
        if (stack.isEmpty()) stack = StackHelper.getStackFromString(SETTINGS.defaultItem.get());
        if (stack.isEmpty()) stack = new ItemStack(Items.IRON_SWORD);
        return stack;
    }
}