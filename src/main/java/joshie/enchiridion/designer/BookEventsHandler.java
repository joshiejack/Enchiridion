package joshie.enchiridion.designer;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.EInfo;
import joshie.enchiridion.Enchiridion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BookEventsHandler {
    @SubscribeEvent
    public void onItemRightClick(PlayerInteractEvent event) {
        if (event.world.isRemote) {
            EntityPlayer player = event.entityPlayer;
            ItemStack held = player.getCurrentEquippedItem();
            if (held != null) {
                if (BookRegistry.opensGui(held)) {
                    if (EConfig.CAN_EDIT_BOOKS && player.isSneaking()) {
                        player.openGui(Enchiridion.instance, EInfo.BOOKS_EDIT_ID, player.worldObj, 0, 0, 0);
                    } else player.openGui(Enchiridion.instance, EInfo.BOOKS_VIEW_ID, player.worldObj, 0, 0, 0);
                }
            }
        }
    }
}
