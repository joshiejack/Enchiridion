package joshie.enchiridion.library.handlers;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IBookHandler;
import joshie.enchiridion.lib.GuiIDs;
import joshie.enchiridion.network.PacketHandler;
import joshie.enchiridion.network.packet.PacketSetLibraryBook;
import net.minecraft.client.gui.screen.EditBookScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

public class WritableBookHandler implements IBookHandler {
    @Override
    public String getName() {
        return "writeable";
    }

    @Override
    public void handle(@Nonnull ItemStack stack, ServerPlayerEntity player, Hand hand, int slotID, boolean isShiftPressed) {
        NetworkHooks.openGui(GuiIDs.WRITABLE, slotID);
    }

    //Our own version for the writeable so that we send packets to the library instead of the hand
    public static class GuiScreenWritable extends EditBookScreen {
        private int slot;

        public GuiScreenWritable(PlayerEntity player, int slot, Hand hand) {
            super(player, EnchiridionAPI.library.getLibraryInventory(player).getStackInSlot(slot), hand);
            this.slot = slot;
        }

        //Overwrite mc behaviour and send a custom packet instead
        @Override
        public void sendBookToServer(boolean publish) {
            if (this.field_214234_c) {
                this.func_214213_e();
                ListNBT nbtList = new ListNBT();
                this.field_214238_g.stream().map(StringNBT::new).forEach(nbtList::add);
                if (!this.field_214238_g.isEmpty()) {
                    this.field_214233_b.setTagInfo("pages", nbtList);
                }

                if (publish) {
                    this.field_214233_b.setTagInfo("author", new StringNBT(this.field_214232_a.getGameProfile().getName()));
                    this.field_214233_b.setTagInfo("title", new StringNBT(this.field_214239_h.trim()));
                }

                //Set the book in the library
                EnchiridionAPI.library.getLibraryInventory(this.field_214232_a).setInventorySlotContents(slot, this.field_214233_b);
                PacketHandler.sendToServer(new PacketSetLibraryBook(this.field_214233_b, slot));
            }
        }
    }
}