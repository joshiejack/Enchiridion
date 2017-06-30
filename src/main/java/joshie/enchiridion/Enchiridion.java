package joshie.enchiridion;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.helpers.FileHelper;
import joshie.enchiridion.helpers.SyncHelper;
import joshie.enchiridion.library.LibraryCommand;
import joshie.enchiridion.library.LibraryHelper;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

import static joshie.enchiridion.lib.EInfo.*;

@Mod(modid = MODID, name = MODNAME, version = VERSION, acceptedMinecraftVersions = ACCEPTED_MC_VERSIONS, guiFactory = GUI_FACTORY_CLASS)
public class Enchiridion {
    @SidedProxy(clientSide = JAVAPATH + INITIALS + "ClientProxy", serverSide = JAVAPATH + INITIALS + "CommonProxy")
    public static ECommonProxy proxy;

    @Instance(MODID)
    public static Enchiridion instance;
    public static File root;

    private static final Logger LOGGER = LogManager.getLogger(MODNAME);

    @EventHandler
    public void onConstruction(FMLConstructionEvent event) {
        proxy.onConstruction();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        root = new File(event.getModConfigurationDirectory(), MODID);
        EConfig.init(FileHelper.getConfigFile());

        NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
        proxy.preInit();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.setupFont();
    }

    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        LibraryHelper.resetServer(FMLCommonHandler.instance().getMinecraftServerInstance().worlds[0]);
        SyncHelper.resetSyncing();

        //Register commands
        ICommandManager manager = event.getServer().getCommandManager();
        if (manager instanceof ServerCommandManager) {
            ServerCommandManager serverCommandManager = ((ServerCommandManager) manager);
            serverCommandManager.registerCommand(new LibraryCommand());
        }
    }

    @EventHandler
    public void handleIMCMessages(FMLInterModComms.IMCEvent event) {
        event.getMessages().stream().filter(message -> message.key.equalsIgnoreCase("registerBook")).forEach(message -> {
            NBTTagCompound tag = message.getNBTValue();
            String handlerType = tag.getString("handlerType");
            ItemStack stack = new ItemStack(tag.getCompoundTag("stack"));
            boolean matchDamage = tag.hasKey("matchDamage") && tag.getBoolean("matchDamage");
            boolean matchNBT = tag.hasKey("matchNBT") && tag.getBoolean("matchNBT");
            EnchiridionAPI.library.registerBookHandlerForStack(handlerType, stack, matchDamage, matchNBT);
        });
    }

    //Universal log helper
    public static void log(Level level, String message) {
        LOGGER.log(level, message);
    }

    //Universal helper translation
    public static String translate(String string) {
        return I18n.translateToLocal("enchiridion." + string);
    }

    public static String format(String string, Object... format) {
        return I18n.translateToLocalFormatted("enchiridion." + string, format);
    }
}