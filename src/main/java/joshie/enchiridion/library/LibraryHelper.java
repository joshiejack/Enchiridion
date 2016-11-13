package joshie.enchiridion.library;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;

public class LibraryHelper {
    @SideOnly(Side.CLIENT)
    private static LibraryProxy theClient;
    private static LibraryProxyServer theServer;

    public static void resetServer(World world) {
        if (world != null) {
            theServer = (new LibraryProxyServer(world));
        }
    }

    @SideOnly(Side.CLIENT)
    public static void resetClient() {
        theClient = new LibraryProxyClient();
    }

    private static LibraryProxy getHandler() {
        return isServer() ? theServer : theClient;
    }

    private static boolean isServer() {
        return FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER;
    }

    public static LibraryInventory getLibraryContents(EntityPlayer player) {
        return getHandler().getLibraryInventory(player);
    }

    @SideOnly(Side.CLIENT)
    public static LibraryInventory getClientLibraryContents() {
        return theClient.getLibraryInventory(null);
    }

    public static LibraryInventory getServerLibraryContents(EntityPlayer player) {
        return theServer.getLibraryInventory(player);
    }

    public static Collection<LibraryInventory> getAllInventories() {
        return theServer.getAllInventories();
    }

    public static void markDirty() {
        theServer.markDirty();
    }
}