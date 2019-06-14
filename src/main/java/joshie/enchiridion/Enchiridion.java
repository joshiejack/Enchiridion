package joshie.enchiridion;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.helpers.FileHelper;
import joshie.enchiridion.helpers.SyncHelper;
import joshie.enchiridion.library.LibraryCommand;
import joshie.enchiridion.library.LibraryHelper;
import joshie.enchiridion.network.PacketHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

import static joshie.enchiridion.lib.EInfo.MODID;
import static joshie.enchiridion.lib.EInfo.MODNAME;

@Mod(value = MODID)
public class Enchiridion {
    private static final Logger LOGGER = LogManager.getLogger(MODNAME);
    public static File root = new File(FMLPaths.CONFIGDIR.get().toFile(), MODID);

    public Enchiridion() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setupCommon);
        eventBus.addListener(this::setupClient);
        eventBus.addListener(this::handleIMCMessages);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStarting);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EConfig.spec, FileHelper.getConfigFile().getAbsolutePath());
    }

    public void setupCommon(final FMLCommonSetupEvent event) {
        ECommonHandler.init();
        PacketHandler.registerPackets();
    }

    public void setupClient(final FMLClientSetupEvent event) {
        EClientHandler.setupClient();
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        LibraryHelper.resetServer(ServerLifecycleHooks.getCurrentServer().getWorld(DimensionType.OVERWORLD));
        SyncHelper.resetSyncing();

        //Register commands
        LibraryCommand.register(event.getCommandDispatcher());
    }

    public void handleIMCMessages(final InterModProcessEvent event) {
        event.getIMCStream().filter(message -> message.getMethod().equalsIgnoreCase("registerBook")).forEach(message -> { //TODO Test
            CompoundNBT tag = new CompoundNBT();
            String handlerType = tag.getString("handlerType");
            ItemStack stack = ItemStack.read(tag.getCompound("stack"));
            boolean matchNBT = tag.contains("matchNBT") && tag.getBoolean("matchNBT");
            EnchiridionAPI.library.registerBookHandlerForStack(handlerType, stack, matchNBT);
        });
    }

    //Universal log helper
    public static void log(Level level, String message) {
        LOGGER.log(level, message);
    }

    //Universal helper translation
    public static String format(String string, Object... format) {
        return new TranslationTextComponent("enchiridion." + string, format).getString();
    }
}