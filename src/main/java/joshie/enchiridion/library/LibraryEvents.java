package joshie.enchiridion.library;

import joshie.enchiridion.EClientHandler;
import joshie.enchiridion.EConfig;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.helpers.MCServerHelper;
import joshie.enchiridion.helpers.SyncHelper;
import joshie.enchiridion.lib.EInfo;
import joshie.enchiridion.network.PacketHandler;
import joshie.enchiridion.network.core.PacketPart;
import joshie.enchiridion.network.packet.PacketOpenLibrary;
import joshie.enchiridion.network.packet.PacketSyncLibraryContents;
import joshie.enchiridion.network.packet.PacketSyncMD5;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.WorldSelectionScreen;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import org.apache.logging.log4j.Level;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = EInfo.MODID)
public class LibraryEvents {
    //Setup the Client
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onOpenGui(GuiOpenEvent event) {
        if (event.getGui() instanceof WorldSelectionScreen || event.getGui() instanceof MultiplayerScreen) {
            LibraryHelper.resetClient();
        }
    }

    //Sync the library
    @SubscribeEvent
    public static void onPlayerLogin(PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player instanceof ServerPlayerEntity) { //Sync what's in the library
            ServerPlayerEntity mp = (ServerPlayerEntity) player;
            if (!SyncHelper.playersSynced.contains(mp)) {
                if (EConfig.SETTINGS.debugMode) Enchiridion.log(Level.INFO, "Did you call me?");
                //Sync what's allowed in the library
                String serverName = MCServerHelper.getHostName();
                //PacketHandler.sendToClient(new PacketSyncLibraryAllowed(PacketPart.SEND_HASH, serverName, ModSupport.getHashcode(serverName)), mp); //TODO

                //Sync what is in the library
                LibraryInventory inventory = LibraryHelper.getServerLibraryContents(player);
                //inventory.addDefaultBooks(); //TODO Fix
                PacketHandler.sendToClient(new PacketSyncLibraryContents(inventory), mp);

                //Start the md5 process
                if (EConfig.SETTINGS.syncDataAndImagesToClients.get()) {
                    PacketHandler.sendToClient(new PacketSyncMD5(PacketPart.SEND_SIZE, "", SyncHelper.servermd5.length), mp);
                }
                SyncHelper.playersSynced.add(mp);
            }
        }
    }

    //Opening the key binding
    @SubscribeEvent
    public static void onKeyPress(InputEvent.KeyInputEvent event) {
        if (EClientHandler.libraryKeyBinding == null) return; //If the keybinding was never created, skip this
        long handle = Minecraft.getInstance().mainWindow.getHandle();
        if (EClientHandler.libraryKeyBinding.isKeyDown() && Minecraft.getInstance().isGameFocused() && !InputMappings.isKeyDown(handle, GLFW.GLFW_KEY_F3)) {
            PacketHandler.sendToServer(new PacketOpenLibrary());
        }
    }
}