package joshie.enchiridion.library;

import org.lwjgl.input.Keyboard;

import joshie.enchiridion.EClientProxy;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.lib.GuiIDs;
import joshie.enchiridion.network.PacketHandler;
import joshie.enchiridion.network.PacketSyncLibrary;
import joshie.lib.helpers.ClientHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LibraryEvents {   
    //Setup the Client
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onOpenGui(GuiOpenEvent event) {
        if (event.gui instanceof GuiSelectWorld || event.gui instanceof GuiMultiplayer) {
            LibraryHelper.resetClient();
        }
    }
    
    //Sync the library
    @SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        if (player instanceof EntityPlayerMP) {
            PacketHandler.sendToClient(new PacketSyncLibrary(LibraryHelper.getServerLibraryContents(player)), (EntityPlayerMP) player);
        }
    }
    
    //Opening the key binding
    @SubscribeEvent
    public void onKeyPress(KeyInputEvent event) {
        if (GameSettings.isKeyDown(EClientProxy.libraryKeyBinding) && Minecraft.getMinecraft().inGameHasFocus && !Keyboard.isKeyDown(Keyboard.KEY_F3)) {
            ClientHelper.getPlayer().openGui(Enchiridion.instance, GuiIDs.LIBRARY, ClientHelper.getWorld(), 0, 0, 0);
        }
    }
}
