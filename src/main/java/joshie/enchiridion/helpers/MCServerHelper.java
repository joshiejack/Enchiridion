package joshie.enchiridion.helpers;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class MCServerHelper {
    public static World getWorld(int dimension) {
        return DimensionManager.getWorld(dimension);
    }
    
    public static String getHostName() {
        String hostname = MinecraftServer.getServer().isDedicatedServer()? MinecraftServer.getServer().getHostname(): "ssp";  
        if (hostname.equals("")) hostname = "smp";
        return hostname;
    }
}