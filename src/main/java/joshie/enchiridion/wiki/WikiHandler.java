package joshie.enchiridion.wiki;

import static joshie.enchiridion.EInfo.WIKI_ID;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.helpers.ClientHelper;
import joshie.enchiridion.wiki.gui.GuiMain;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;

public class WikiHandler {
    public static KeyBinding wiki;

    @SubscribeEvent
    public void onKeyPress(KeyInputEvent event) {
        if (GameSettings.isKeyDown(wiki)) {
            ClientHelper.getPlayer().openGui(Enchiridion.instance, WIKI_ID, ClientHelper.getWorld(), 0, 0, 0);
        }
    }

    @SubscribeEvent
    public void onPreRender(RenderGameOverlayEvent.Pre event) {
        if (ClientHelper.getMinecraft().currentScreen instanceof GuiMain) {
            if (event.type == ElementType.CROSSHAIRS || event.type == ElementType.HOTBAR) {
                event.setCanceled(true);
            }
        }
    }
}
