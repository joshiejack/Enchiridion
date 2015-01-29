package joshie.enchiridion.designer;

import static joshie.enchiridion.designer.BookRegistry.getData;
import static joshie.enchiridion.designer.BookRegistry.getID;

import java.util.List;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.EInfo;
import joshie.enchiridion.ETranslate;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.designer.BookRegistry.BookData;
import joshie.enchiridion.helpers.ClientHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemBook extends Item {
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        BookData data = getData(stack);
        return data == null ? super.getItemStackDisplayName(stack) : getDisplayName(data);
    }

    public String getDisplayName(BookData data) {
        if (data.displayNames.containsKey(ClientHelper.getLang())) {
            return data.displayNames.get(ClientHelper.getLang());
        } else return data.displayNames.get("en_US");
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean var) {
        if (EConfig.CAN_EDIT_BOOKS) {
            list.add(ETranslate.translate("edit.shift"));
            list.add(ETranslate.translate("edit.save"));
        }

        BookData data = getData(stack);
        if (data != null && data.information != null) {
            list.addAll(data.information);
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        String id = getID(stack);
        if (id != null) {
            if (EConfig.CAN_EDIT_BOOKS) {
                if (player.isSneaking()) {
                    player.openGui(Enchiridion.instance, EInfo.BOOKS_EDIT_ID, player.worldObj, 1, 0, 0);
                } else {
                    player.openGui(Enchiridion.instance, EInfo.BOOKS_VIEW_ID, player.worldObj, 1, 0, 0);
                }
            } else player.openGui(Enchiridion.instance, EInfo.BOOKS_VIEW_ID, player.worldObj, 1, 0, 0);
        }

        return stack;
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int pass) {
        BookData data = getData(stack);
        if (data != null) {
            return data.color;
        } else return 16777215;
    }

    @Override
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public int getRenderPasses(int meta) {
        return 2;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (String identifier : BookRegistry.getIDs()) {
            ItemStack stack = new ItemStack(item);
            stack.setTagCompound(new NBTTagCompound());
            stack.stackTagCompound.setString("identifier", identifier);
            list.add(stack);
        }
    }

    /** Worry about the icons later **/
}
