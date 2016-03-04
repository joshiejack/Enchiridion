package joshie.enchiridion.gui.library;

import joshie.lib.gui.ContainerCore;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerLibrary extends ContainerCore {
    public IInventory library;

    public ContainerLibrary(InventoryPlayer playerInventory, IInventory library) {
        //Left hand side slots
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                addSlotToContainer(new SlotBook(library, j + (i * 3),  -51 + (j * 18), 22 + (i * 23)));
            }
        }
        
        //Right hand side slots
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                addSlotToContainer(new SlotBook(library, 15 + j + (i * 3),  175 + (j * 18), 22 + (i * 23)));
            }
        }
        
        //Centre Slots
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                addSlotToContainer(new SlotBook(library, 30 + j + (i * 7),  26 + (j * 18), -1 + (i * 23)));
            }
        }
        
       // addSlotToContainer(new SlotBook(library, 0, 8, 15)); //Add one book slot
        bindPlayerInventory(playerInventory, 30);
    }
}
