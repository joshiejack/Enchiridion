package joshie.enchiridion.helpers;

import joshie.enchiridion.api.book.IPage;
import joshie.enchiridion.api.event.FeatureVisibleEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;

public class EventHelper {
    public static boolean isFeatureVisible(IPage ipage, boolean isVisible, int layer) {
        if (ipage.getBook() == null) return isVisible;
        PlayerEntity player = MCClientHelper.getPlayer();
        String bookID = ipage.getBook().getUniqueName();
        int page = ipage.getPageNumber();
        FeatureVisibleEvent event = new FeatureVisibleEvent(player, isVisible, bookID, page, layer);
        MinecraftForge.EVENT_BUS.post(event);
        return event.isVisible;
    }
}