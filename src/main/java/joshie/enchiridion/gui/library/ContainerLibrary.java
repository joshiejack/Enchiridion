package joshie.enchiridion.gui.library;

import joshie.lib.gui.ContainerCore;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;

public class ContainerLibrary extends ContainerCore {
    public IInventory library;

    public ContainerLibrary(InventoryPlayer playerInventory, IInventory library) {
        addSlotToContainer(new SlotBook(library, 0, 8, 15)); //Add one book slot
        bindPlayerInventory(playerInventory);
    }
}
