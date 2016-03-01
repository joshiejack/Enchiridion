package joshie.enchiridion.data.library;

import joshie.enchiridion.helpers.LibraryHandlerHelper;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LibraryEvents {
    //Setup the Server
    @SubscribeEvent
    public void onLoad(WorldEvent.Load event) {
        World world = event.world;
        if (!world.isRemote && world.provider.getDimensionId() == 0) {
            LibraryHandlerHelper.reset(world);
        }
    }
    
    //Setup the Client
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onOpenGui(GuiOpenEvent event) {
        if (event.gui instanceof GuiSelectWorld || event.gui instanceof GuiMultiplayer) {
            LibraryHandlerHelper.reset(null);
        }
    }
}
