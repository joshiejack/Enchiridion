package joshie.enchiridion;

import org.apache.logging.log4j.Level;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IBookHandler;
import joshie.enchiridion.gui.library.ContainerLibrary;
import joshie.enchiridion.lib.GuiIDs;
import joshie.enchiridion.library.LibraryEvents;
import joshie.enchiridion.library.LibraryHelper;
import joshie.enchiridion.library.LibraryRegistry;
import joshie.enchiridion.library.handlers.ComputerCraftHandler;
import joshie.enchiridion.library.handlers.EnchiridionBookHandler;
import joshie.enchiridion.library.handlers.RightClickBookHandler;
import joshie.enchiridion.library.handlers.TemporarySwitchHandler;
import joshie.enchiridion.library.handlers.WarpBookHandler;
import joshie.enchiridion.library.handlers.WriteableBookHandler;
import joshie.enchiridion.network.PacketHandleBook;
import joshie.enchiridion.network.PacketHandler;
import joshie.enchiridion.network.PacketLibraryCommand;
import joshie.enchiridion.network.PacketOpenLibrary;
import joshie.enchiridion.network.PacketSetLibraryBook;
import joshie.enchiridion.network.PacketSyncFile;
import joshie.enchiridion.network.PacketSyncLibraryAllowed;
import joshie.enchiridion.network.PacketSyncLibraryContents;
import joshie.enchiridion.network.PacketSyncMD5;
import joshie.enchiridion.util.ECreativeTab;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;

public class ECommonProxy implements IGuiHandler {
    public static Item book;
    
    public void onConstruction() {}
    
    public void preInit() {
        book = new ItemEnchiridion().setCreativeTab(ECreativeTab.enchiridion).setHasSubtypes(true).setUnlocalizedName("book");
        EnchiridionAPI.instance = new EAPIHandler();
        EnchiridionAPI.library = new LibraryRegistry();
        EnchiridionAPI.library.registerBookHandler(new EnchiridionBookHandler()); //Enchiridion
        EnchiridionAPI.library.registerBookHandler(new WriteableBookHandler()); //Writeable Books
        EnchiridionAPI.library.registerBookHandler(new RightClickBookHandler()); //Default Handler
        EnchiridionAPI.library.registerBookHandler(new TemporarySwitchHandler()); //Switch Click Handler
        if (EConfig.loadComputercraft) attemptToRegisterModdedBookHandler(ComputerCraftHandler.class);
        if (EConfig.loadWarpbook) attemptToRegisterModdedBookHandler(WarpBookHandler.class);
        
        
        //Register events
        FMLCommonHandler.instance().bus().register(new LibraryEvents());
        MinecraftForge.EVENT_BUS.register(new LibraryEvents());
        
        //Register packets#
        PacketHandler.registerPacket(PacketSyncLibraryAllowed.class);
        PacketHandler.registerPacket(PacketLibraryCommand.class);
        PacketHandler.registerPacket(PacketSyncMD5.class);
        PacketHandler.registerPacket(PacketSyncFile.class);
        PacketHandler.registerPacket(PacketSyncLibraryContents.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketOpenLibrary.class, Side.SERVER);
        PacketHandler.registerPacket(PacketHandleBook.class, Side.SERVER);
        PacketHandler.registerPacket(PacketSetLibraryBook.class, Side.SERVER);
        
        //Prepare the client for shizz
        setupClient();
    }
    
    public void attemptToRegisterModdedBookHandler(Class clazz) {
        try {
            Object o = clazz.newInstance(); //Let's try this!
            EnchiridionAPI.library.registerBookHandler((IBookHandler) o);
        } catch (Exception e) { Enchiridion.log(Level.INFO, "Enchiridion could not create an instance of " + clazz.getName() + " as the mods this handler relies on, are not supplied");}
    }

    public void setupClient() {}
	public void setupFont() {}
	
	
	/** GUI HANDLING **/
	@Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int integer1, int y, int z) {
        if (ID == GuiIDs.WARPBOOK) {
            return WarpBookHandler.getWarpbookContainer(player, integer1);
        } else if (ID == GuiIDs.LIBRARY) {
            return new ContainerLibrary(player.inventory, LibraryHelper.getServerLibraryContents(player));
        } else return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}