package joshie.enchiridion.library.handlers;

import java.lang.reflect.Method;

import org.lwjgl.input.Keyboard;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.gui.GuiBook;
import com.panicnot42.warpbook.gui.GuiWarpBookItemInventory;
import com.panicnot42.warpbook.inventory.WarpBookInventoryItem;
import com.panicnot42.warpbook.inventory.container.WarpBookContainerItem;
import com.panicnot42.warpbook.inventory.container.WarpBookSpecialInventory;
import com.panicnot42.warpbook.util.CommandUtils;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IBookHandler;
import joshie.enchiridion.lib.GuiIDs;
import joshie.enchiridion.library.LibraryHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class WarpBookHandler implements IBookHandler {
    public static Object getWarpbookContainer(EntityPlayer player, int slot) {
        ItemStack warpbook = EnchiridionAPI.library.getLibraryInventory(player).getStackInSlot(slot);
        return new WarpBookContainerItem(player, player.inventory, new WarpBookInventoryItem(warpbook), new WarpBookSpecialInventory(warpbook));
    }

    public static Object getWarpbookGui(EntityPlayer player, int slot) {
        ItemStack warpbook = EnchiridionAPI.library.getLibraryInventory(player).getStackInSlot(slot);
        return new GuiWarpBookItemInventory(new WarpBookContainerItem(player, player.inventory, new WarpBookInventoryItem(warpbook), new WarpBookSpecialInventory(warpbook)));
    }
    
    public static Object getWarplistGui(EntityPlayer player, int slot) {
        return new GuiWarpbook(player, slot);
    }

    @Override
    public String getName() {
        return "warpbook";
    }

    @Override
    public void handle(ItemStack stack, EntityPlayer player, int slotID, boolean isShiftPressed) {
        if (isShiftPressed) {
            player.openGui(Enchiridion.instance, GuiIDs.WARPBOOK, player.worldObj, slotID, 0, 0);
            //Because I can't trust what is going on when editing, let's mark the library as dirty
            if (!player.worldObj.isRemote) LibraryHelper.markDirty();
        } else {
            WarpBookMod.lastHeldBooks.put(player, stack); //YOU BE LAST!
            player.openGui(Enchiridion.instance, GuiIDs.WARPLIST, player.worldObj, slotID, 0, 0);
        }
    }

    public static class GuiWarpbook extends GuiBook {
        //Code from Warpbook by panicnot42, Overwriting so that it can be handled properly in the library
        //Only reason this code exists in here ^^^
        private final EntityPlayer entityPlayer;
        private NBTTagList items;
        private int slot;
        
        public GuiWarpbook(EntityPlayer entityPlayer, int slot) {
            super(entityPlayer);
     
            this.entityPlayer = entityPlayer;
            this.slot = slot;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void initGui() {
            ItemStack stack = EnchiridionAPI.library.getLibraryInventory(entityPlayer).getStackInSlot(slot);
            
            Keyboard.enableRepeatEvents(true);
            buttonList.clear();
            if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
            items = stack.getTagCompound().getTagList("WarpPages", new NBTTagCompound().getId());
            if (items.tagCount() == 0) {
                CommandUtils.showError(entityPlayer, I18n.format("help.nopages"));
                mc.displayGuiScreen((GuiScreen) null);
                return;
            }
            for (int i = 0; i < items.tagCount(); ++i) {
                NBTTagCompound compound = ItemStack.loadItemStackFromNBT(items.getCompoundTagAt(i)).getTagCompound();
                try {
                    buttonList.add(new GuiButton(i, ((width - 404) / 2) + ((i % 6) * 68), 16 + (24 * (i / 6)), 64, 16, (String) getButtonText.invoke(null, compound)));
                } catch (Exception e) { e.printStackTrace();
                    // old page
                }
            }
        }
        
        private static Method getButtonText;
        static {
            try {
                getButtonText = GuiBook.class.getDeclaredMethod("getButtonText", NBTTagCompound.class);
                getButtonText.setAccessible(true);
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}
