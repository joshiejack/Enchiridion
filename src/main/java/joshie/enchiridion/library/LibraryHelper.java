package joshie.enchiridion.library;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.thread.EffectiveSide;

import java.util.Collection;

public class LibraryHelper {
    @OnlyIn(Dist.CLIENT)
    private static LibraryProxy theClient;
    private static LibraryProxyServer theServer;

    public static void resetServer(ServerWorld world) {
        if (world != null) {
            System.out.println("World is not null ENCH");
            theServer = (new LibraryProxyServer(world));
        }
        System.out.println("Server might be null ENCH");
    }

    @OnlyIn(Dist.CLIENT)
    public static void resetClient() {
        theClient = new LibraryProxyClient();
    }

    private static LibraryProxy getHandler() {
        return isServer() ? theServer : theClient;
    }

    private static boolean isServer() {
        return EffectiveSide.get() == LogicalSide.SERVER;
    }

    public static LibraryInventory getLibraryContents(PlayerEntity player) {
        return getHandler().getLibraryInventory(player);
    }

    @OnlyIn(Dist.CLIENT)
    public static LibraryInventory getClientLibraryContents() {
        return theClient.getLibraryInventory(null);
    }

    public static LibraryInventory getServerLibraryContents(PlayerEntity player) {
        return theServer.getLibraryInventory(player);
    }

    public static Collection<LibraryInventory> getAllInventories() {
        return theServer.getAllInventories();
    }

    public static void markDirty() {
        theServer.markDirty();
    }
}