package joshie.enchiridion.library.handlers;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IBookHandler;
import joshie.enchiridion.lib.GuiIDs;
import joshie.enchiridion.network.PacketHandler;
import joshie.enchiridion.network.PacketSetLibraryBook;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;
import java.io.IOException;

public class WritableBookHandler implements IBookHandler {
    @Override
    public String getName() {
        return "writeable";
    }

    @Override
    public void handle(@Nonnull ItemStack stack, EntityPlayer player, EnumHand hand, int slotID, boolean isShiftPressed) {
        player.openGui(Enchiridion.instance, GuiIDs.WRITABLE, player.world, slotID, 0, 0);
    }

    //Our own version for the writeable so that we send packets to the library instead of the hand
    public static class GuiScreenWritable extends GuiScreenBook {
        private int slot;

        public GuiScreenWritable(EntityPlayer player, int slot) {
            super(player, EnchiridionAPI.library.getLibraryInventory(player).getStackInSlot(slot), true);
            this.slot = slot;
        }

        //Overwrite mc behaviour and send a custom packet instead
        @Override
        public void sendBookToServer(boolean publish) throws IOException {
            if (this.bookIsUnsigned && this.bookIsModified) {
                if (this.bookPages != null) {
                    while (this.bookPages.tagCount() > 1) {
                        String s = this.bookPages.getStringTagAt(this.bookPages.tagCount() - 1);

                        if (s.length() != 0) {
                            break;
                        }

                        this.bookPages.removeTag(this.bookPages.tagCount() - 1);
                    }

                    if (this.bookObj.hasTagCompound()) {
                        NBTTagCompound nbttagcompound = this.bookObj.getTagCompound();
                        nbttagcompound.setTag("pages", this.bookPages);
                    } else {
                        this.bookObj.setTagInfo("pages", this.bookPages);
                    }

                    ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);

                    if (publish) {
                        this.bookObj.setTagInfo("author", new NBTTagString(this.editingPlayer.getName()));
                        this.bookObj.setTagInfo("title", new NBTTagString(this.bookTitle.trim()));

                        for (int i = 0; i < this.bookPages.tagCount(); ++i) {
                            String s1 = this.bookPages.getStringTagAt(i);
                            ITextComponent textComponent = new TextComponentString(s1);
                            s1 = ITextComponent.Serializer.componentToJson(textComponent);
                            this.bookPages.set(i, new NBTTagString(s1));
                        }

                        stack.setTagCompound(this.bookObj.getTagCompound().copy());
                    }

                    //Set the book in the library
                    EnchiridionAPI.library.getLibraryInventory(editingPlayer).setInventorySlotContents(slot, bookObj);
                    PacketHandler.sendToServer(new PacketSetLibraryBook(stack, slot));
                }
            }
        }
    }
}