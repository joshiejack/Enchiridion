package joshie.enchiridion;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IBookHandler;
import joshie.enchiridion.gui.library.ContainerLibrary;
import joshie.enchiridion.helpers.HeldHelper;
import joshie.enchiridion.items.ItemEnchiridion;
import joshie.enchiridion.lib.GuiIDs;
import joshie.enchiridion.library.LibraryHelper;
import joshie.enchiridion.library.LibraryRegistry;
import joshie.enchiridion.library.handlers.*;
import joshie.enchiridion.network.*;
import joshie.enchiridion.util.ECreativeTab;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Level;

public class ECommonProxy implements IGuiHandler {
    public static Item book;

    public void onConstruction() {
    }

    public void preInit() {
        book = new ItemEnchiridion().setCreativeTab(ECreativeTab.ENCHIRIDION).setHasSubtypes(true).setTranslationKey("book");
        EnchiridionAPI.instance = new EAPIHandler();
        EnchiridionAPI.library = LibraryRegistry.INSTANCE;
        EnchiridionAPI.library.registerBookHandler(new EnchiridionBookHandler()); //Enchiridion
        EnchiridionAPI.library.registerBookHandler(new WritableBookHandler()); //Writeable Books
        EnchiridionAPI.library.registerBookHandler(new WrittenBookHandler()); //Written Books
        EnchiridionAPI.library.registerBookHandler(new TemporarySwitchHandler()); //Default Handler
        EnchiridionAPI.library.registerBookHandler(new RightClickHandler()); //Kept for backwards compatibility
        EnchiridionAPI.library.registerBookHandler(new CopyNBTHandler()); //Copy NBT Handler

        //Register packets
        PacketHandler.registerPacket(PacketSyncLibraryAllowed.class);
        PacketHandler.registerPacket(PacketLibraryCommand.class);
        PacketHandler.registerPacket(PacketSyncMD5.class);
        PacketHandler.registerPacket(PacketSyncFile.class);
        PacketHandler.registerPacket(PacketOpenBook.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncLibraryContents.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketOpenLibrary.class, Side.SERVER);
        PacketHandler.registerPacket(PacketHandleBook.class, Side.SERVER);
        PacketHandler.registerPacket(PacketSetLibraryBook.class, Side.SERVER);

        setupClient();
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
        if (ID == GuiIDs.LIBRARY) {
            return new ContainerLibrary(player.inventory, LibraryHelper.getServerLibraryContents(player), HeldHelper.getHandFromOrdinal(handOrdinal));
        } else return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}