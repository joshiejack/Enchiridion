package joshie.enchiridion.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class FeatureVisibleEvent extends PlayerEvent {
    public final String bookid;
    public final int page;
    public final int layer;
    
    public FeatureVisibleEvent(EntityPlayer player, String bookid, int page, int layer) {
        super(player);
        this.bookid = bookid;
        this.page = page;
        this.layer = layer;
    }
}
