package joshie.enchiridion.helpers;

import java.util.Collection;
import java.util.UUID;

import joshie.enchiridion.data.library.LibraryContents;
import joshie.enchiridion.data.library.handlers.LibraryHandlerAbstract;
import joshie.enchiridion.data.library.handlers.LibraryHandlerClient;
import joshie.enchiridion.data.library.handlers.LibraryHandlerServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LibraryHandlerHelper {
    @SideOnly(Side.CLIENT)
    private static LibraryHandlerClient theClient;
    private static LibraryHandlerServer theServer;
    
    public static void reset(World world) {
        if (world != null) {
            theServer = (new LibraryHandlerServer(world));
        } else theClient = new LibraryHandlerClient();
    }

    private static LibraryHandlerAbstract getHandler() {
        return isServer() ? theServer : theClient;
    }

    private static boolean isServer() {
        return FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER;
    }

    public static LibraryContents getLibraryContents(EntityPlayer player) {
        return getHandler().getLibraryContents(player);
    }

    public static LibraryContents getLibraryContents(UUID owner) {
        return getHandler().getLibraryContents(owner);
    }
    
    public static Collection<LibraryContents> getPlayerLibraries() {
        return theServer.getPlayerData();
    }
    
    @SideOnly(Side.CLIENT)
    public static LibraryContents getClientLibraryContents() {
        return theClient.getLibraryContents((EntityPlayer)null);
    }
    
    public static LibraryContents getServerLibraryContents(EntityPlayer player) {
        return theServer.getLibraryContents(player);
    }

    public static void markDirty() {
        theServer.markDirty();
    }
}