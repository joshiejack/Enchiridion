package joshie.enchiridion.library.handlers;

import dan200.computercraft.client.gui.GuiPrintout;
import dan200.computercraft.shared.media.inventory.ContainerHeldItem;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IBookHandler;
import joshie.enchiridion.lib.GuiIDs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ComputerCraftHandler implements IBookHandler {
    public static Object getComputercraftPrintoutGui(EntityPlayer player, int slot) {
        ItemStack printed = EnchiridionAPI.library.getLibraryInventory(player).getStackInSlot(slot);
        InventoryPlayer fake = new InventoryPlayer(player);
        fake.mainInventory[0] = printed.copy();
        fake.currentItem = 0;
        ContainerHeldItem container = new ContainerHeldItem(fake);
        return new GuiPrintout(container);
    }
    
    @Override
    public String getName() {
        return "computercraft";
    }

    @Override
    public void handle(ItemStack stack, EntityPlayer player, int slotID, boolean isShiftPressed) {
        player.openGui(Enchiridion.instance, GuiIDs.COMPUTERCRAFT, player.worldObj, slotID, 0, 0);
    }
}
