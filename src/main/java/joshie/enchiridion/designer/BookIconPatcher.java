package joshie.enchiridion.designer;

import joshie.enchiridion.ELogger;
import joshie.enchiridion.designer.BookRegistry.BookData;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.client.event.TextureStitchEvent;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BookIconPatcher {
    public static TextureMap map;

    @SubscribeEvent
    public void onStitch(TextureStitchEvent.Pre event) {
        if (event.map.getTextureType() == 1) {
            map = event.map;
            for (String book : BookRegistry.getIDs()) {
                BookData data = BookRegistry.getData(book);
                if (data.iconPass1 != null && !data.iconPass1.equals("")) {
                    ELogger.log(Level.INFO, "Patching in the icon, marked as " + data.uniqueName + "1");
                    map.setTextureEntry(data.iconPass1, new BookIconAtlas(data.iconPass1));
                }

                if (data.iconPass2 != null && !data.iconPass2.equals("")) {
                    ELogger.log(Level.INFO, "Patching in the icon, marked as " + data.uniqueName + "2");
                    map.setTextureEntry(data.iconPass2, new BookIconAtlas(data.iconPass2));
                }
            }
        }
    }
}
