package joshie.enchiridion.library;

import joshie.enchiridion.EClientProxy;
import joshie.enchiridion.EConfig;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.helpers.MCClientHelper;
import joshie.enchiridion.helpers.MCServerHelper;
import joshie.enchiridion.helpers.SyncHelper;
import joshie.enchiridion.lib.GuiIDs;
import joshie.enchiridion.network.*;
import joshie.enchiridion.network.core.PacketPart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;
import org.lwjgl.input.Keyboard;

public class LibraryEvents {
    //Setup the Client
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onOpenGui(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiWorldSelection || event.getGui() instanceof GuiMultiplayer) {
            LibraryHelper.resetClient();
        }
    }

    //Sync the library
    @SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        if (player instanceof EntityPlayerMP) { //Sync what's in the library
            EntityPlayerMP mp = (EntityPlayerMP) player;
            if (!SyncHelper.playersSynced.contains(mp)) {
                if (EConfig.debugMode) Enchiridion.log(Level.INFO, "Did you call me?");
                //Sync what's allowed in the library
                String serverName = MCServerHelper.getHostName();
                PacketHandler.sendToClient(new PacketSyncLibraryAllowed(PacketPart.SEND_HASH, serverName, ModSupport.getHashcode(serverName)), mp);
                
                //Sync what is in the library
                LibraryInventory inventory = LibraryHelper.getServerLibraryContents(player);
                inventory.addDefaultBooks();
                PacketHandler.sendToClient(new PacketSyncLibraryContents(inventory), mp);
                
                //Start the md5 process
                if (EConfig.syncDataAndImagesToClients) {
                    PacketHandler.sendToClient(new PacketSyncMD5(PacketPart.SEND_SIZE, "", SyncHelper.servermd5.length), mp);
                }
                
                SyncHelper.playersSynced.add(mp);
            }
        }
    }

    //Opening the key binding
    @SubscribeEvent
    public void onKeyPress(KeyInputEvent event) {
        if (EClientProxy.libraryKeyBinding == null) return; //If the keybinding was never created, skip this
        if (GameSettings.isKeyDown(EClientProxy.libraryKeyBinding) && Minecraft.getMinecraft().inGameHasFocus && !Keyboard.isKeyDown(Keyboard.KEY_F3)) {
            PacketHandler.sendToServer(new PacketOpenLibrary()); //Let the server know!
            MCClientHelper.getPlayer().openGui(Enchiridion.instance, GuiIDs.LIBRARY, MCClientHelper.getWorld(), 0, 0, 0);
        }
    }
}
