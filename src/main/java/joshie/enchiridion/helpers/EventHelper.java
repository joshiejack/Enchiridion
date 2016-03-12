package joshie.enchiridion.helpers;

import joshie.enchiridion.api.event.FeatureVisibleEvent;
import joshie.enchiridion.gui.book.GuiBook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EventHelper {
    public static boolean isCanceled(Event event) {
        return MinecraftForge.EVENT_BUS.post(event);
    }
    
    public static boolean isFeatureVisible(int layer) {
        EntityPlayer player = MCClientHelper.getPlayer();
        String bookid = GuiBook.INSTANCE.getBook().getUniqueName();
        int page = GuiBook.INSTANCE.getPage().getPageNumber() + 1;
        return !isCanceled(new FeatureVisibleEvent(player, bookid, page, layer + 1));
    }
}
