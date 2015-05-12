package joshie.enchiridion.designer;

import joshie.enchiridion.designer.BookRegistry.BookData;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.client.event.TextureStitchEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BookIconPatcher {
    static TextureMap map;

    @SubscribeEvent
    public void onStitch(TextureStitchEvent.Pre event) {
        if (event.map.getTextureType() == 1) {
            map = event.map;
            for (String book : BookRegistry.getIDs()) {
                BookData data = BookRegistry.getData(book);
                if (data != null) {
                    if (data.iconPass1 != null && !data.iconPass1.equals("")) {
                        map.setTextureEntry(data.iconPass1, new BookIconAtlas(data.iconPass1));
                    }

                    if (data.iconPass2 != null && !data.iconPass2.equals("")) {
                        map.setTextureEntry(data.iconPass2, new BookIconAtlas(data.iconPass2));
                    }
                }
            }
        }
    }
}
