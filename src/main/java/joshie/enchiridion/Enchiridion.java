package joshie.enchiridion;

import static java.io.File.separator;
import static joshie.enchiridion.lib.EInfo.DEPENDENCIES;
import static joshie.enchiridion.lib.EInfo.INITIALS;
import static joshie.enchiridion.lib.EInfo.JAVAPATH;
import static joshie.enchiridion.lib.EInfo.MODID;
import static joshie.enchiridion.lib.EInfo.MODNAME;
import static joshie.enchiridion.lib.EInfo.MODPATH;
import static joshie.enchiridion.lib.EInfo.VERSION;

import java.io.File;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import joshie.enchiridion.helpers.SyncHelper;
import joshie.enchiridion.library.LibraryCommand;
import joshie.enchiridion.library.LibraryHelper;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = MODID, name = MODNAME, version = VERSION, dependencies = DEPENDENCIES)
public class Enchiridion {
    @SidedProxy(clientSide = JAVAPATH + INITIALS + "ClientProxy", serverSide = JAVAPATH + INITIALS + "CommonProxy")
    public static ECommonProxy proxy;

    @Instance(MODID)
    public static Enchiridion instance;
    public static File root;

    private static final Logger logger = LogManager.getLogger(MODNAME);

    @EventHandler
    public void onConstruction(FMLConstructionEvent event) {
        proxy.onConstruction();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        root = new File(event.getModConfigurationDirectory() + separator + MODPATH);
        EConfig.init();
        proxy.preInit();
    }

    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        LibraryHelper.resetServer(MinecraftServer.getServer().worldServers[0]);
        SyncHelper.resetSyncing();
        //Register commands
        
        ICommandManager manager = event.getServer().getCommandManager();
        if(manager instanceof ServerCommandManager) {
            ServerCommandManager serverCommandManager = ((ServerCommandManager)manager);
            serverCommandManager.registerCommand(new LibraryCommand());
        }
    }

    //Universal log helper
    public static void log(Level level, String message) {
        logger.log(level, message);
    }

    //Universal helper translation
    public static String translate(String string) {
        return StatCollector.translateToLocal("enchiridion." + string);
    }
}