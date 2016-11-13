package joshie.enchiridion;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IBookHandler;
import joshie.enchiridion.data.book.BookEvents;
import joshie.enchiridion.gui.library.ContainerLibrary;
import joshie.enchiridion.helpers.HeldHelper;
import joshie.enchiridion.items.ItemEnchiridion;
import joshie.enchiridion.lib.GuiIDs;
import joshie.enchiridion.library.LibraryEvents;
import joshie.enchiridion.library.LibraryHelper;
import joshie.enchiridion.library.LibraryRecipe;
import joshie.enchiridion.library.LibraryRegistry;
import joshie.enchiridion.library.handlers.*;
import joshie.enchiridion.network.*;
import joshie.enchiridion.util.ECreativeTab;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.logging.log4j.Level;

public class ECommonProxy implements IGuiHandler {
    public static Item book;

    public void onConstruction() {
    }

    public void preInit() {
        book = new ItemEnchiridion().setCreativeTab(ECreativeTab.ENCHIRIDION).setHasSubtypes(true).setUnlocalizedName("book");
        EnchiridionAPI.instance = new EAPIHandler();
        EnchiridionAPI.library = LibraryRegistry.INSTANCE;
        EnchiridionAPI.library.registerBookHandler(new EnchiridionBookHandler()); //Enchiridion
        EnchiridionAPI.library.registerBookHandler(new WriteableBookHandler()); //Writeable Books
        EnchiridionAPI.library.registerBookHandler(new RightClickBookHandler()); //Default Handler
        EnchiridionAPI.library.registerBookHandler(new TemporarySwitchHandler()); //Switch Click Handler
        EnchiridionAPI.library.registerBookHandler(new CopyNBTHandler()); //Copy NBT Handler
        //if (EConfig.loadComputercraft) attemptToRegisterModdedBookHandler(ComputerCraftHandler.class); //TODO Re-add when updated to 1.11
        //if (EConfig.loadWarpbook) attemptToRegisterModdedBookHandler(WarpBookHandler.class); // TODO Re-add when updated to 1.11

        //Register events
        MinecraftForge.EVENT_BUS.register(new BookEvents());
        MinecraftForge.EVENT_BUS.register(new LibraryEvents());

        //Register packets#
        PacketHandler.registerPacket(PacketSyncLibraryAllowed.class);
        PacketHandler.registerPacket(PacketLibraryCommand.class);
        PacketHandler.registerPacket(PacketSyncMD5.class);
        PacketHandler.registerPacket(PacketSyncFile.class);
        PacketHandler.registerPacket(PacketOpenBook.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncLibraryContents.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketOpenLibrary.class, Side.SERVER);
        PacketHandler.registerPacket(PacketHandleBook.class, Side.SERVER);
        PacketHandler.registerPacket(PacketSetLibraryBook.class, Side.SERVER);

        //Prepare the client for shizz
        setupClient();
    }

    //Adds the library recipe
    public void addRecipe() {
        if (EConfig.addOreDictionaryRecipeForLibrary) {
            RecipeSorter.register("enchiridion:library", LibraryRecipe.class, Category.SHAPED, "after:minecraft:shaped before:minecraft:shapeless");
            GameRegistry.addRecipe(new LibraryRecipe());
        } else if (EConfig.addWrittenBookRecipeForLibrary) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ECommonProxy.book, 1, 1), "WWW", "BBB", "WWW", new ItemStack(Blocks.PLANKS, 1, 1), new ItemStack(Items.WRITABLE_BOOK), new ItemStack(Blocks.PLANKS, 1, 1)));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ECommonProxy.book, 1, 1), "WWW", "BBB", "WWW", new ItemStack(Blocks.PLANKS, 1, 1), new ItemStack(Items.WRITABLE_BOOK), new ItemStack(Blocks.PLANKS, 1, 5)));
        }
    }

    public void attemptToRegisterModdedBookHandler(Class clazz) {
        try {
            Object o = clazz.newInstance(); //Let's try this!
            EnchiridionAPI.library.registerBookHandler((IBookHandler) o);
        } catch (Exception e) {
            Enchiridion.log(Level.INFO, "Enchiridion could not create an instance of " + clazz.getName() + " as the mods this handler relies on, are not supplied");
        }
    }

    public void setupClient() {
    }

    public void setupFont() {
    }

    /**
     * GUI HANDLING
     **/
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int integer1, int handOrdinal, int z) {
        /*if (ID == GuiIDs.WARPBOOK) { //TODO Re-add when updated to 1.11
            return WarpBookHandler.getWarpbookContainer(player, integer1);
        } else */
        if (ID == GuiIDs.LIBRARY) {
            return new ContainerLibrary(player.inventory, LibraryHelper.getServerLibraryContents(player), HeldHelper.getHandFromOrdinal(handOrdinal));
        } else return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}