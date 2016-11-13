package joshie.enchiridion.helpers;

import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class MCServerHelper {
    public static World getWorld(int dimension) {
        return DimensionManager.getWorld(dimension);
    }

    public static String getHostName() {
        String hostname = FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer() ? FMLCommonHandler.instance().getMinecraftServerInstance().getServerHostname() : "ssp";
        if (hostname.equals("")) hostname = "smp";
        return hostname;
    }
}