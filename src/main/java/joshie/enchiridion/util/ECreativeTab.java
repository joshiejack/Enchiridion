package joshie.enchiridion.util;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.lib.EInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ECreativeTab extends CreativeTabs {
    public static ECreativeTab enchiridion = new ECreativeTab(EInfo.MODPATH);
    public final Item icon = Items.WRITABLE_BOOK;
    public ItemStack itemstack;

    public ECreativeTab(String label) {
        super(label);
    }

    @Override
    public Item getTabIconItem() {
        return icon;
    }
    
    @SideOnly(Side.CLIENT)
    public ItemStack getIconItemStack() {
        if (itemstack == null) return super.getIconItemStack();
        else return itemstack;
    }
    
    public void setItemStack(ItemStack stack) {
        itemstack = stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getTranslatedTabLabel() {
        return Enchiridion.translate("creative");
    }
}
