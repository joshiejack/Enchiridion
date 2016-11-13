package joshie.enchiridion.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class FeatureVisibleEvent extends PlayerEvent {
    public final String bookID;
    public final int page;
    public final int layer;
    public boolean isVisible;

    public FeatureVisibleEvent(EntityPlayer player, boolean isVisible, String bookID, int page, int layer) {
        super(player);
        this.isVisible = isVisible;
        this.bookID = bookID;
        this.page = page;
        this.layer = layer;
    }
}