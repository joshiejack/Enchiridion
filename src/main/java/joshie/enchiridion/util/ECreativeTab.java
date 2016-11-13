package joshie.enchiridion.util;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.lib.EInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ECreativeTab extends CreativeTabs {
    public static final ECreativeTab ENCHIRIDION = new ECreativeTab(EInfo.MODID);
    public final Item icon = Items.WRITABLE_BOOK;
    public ItemStack itemstack;

    public ECreativeTab(String label) {
        super(label);
    }

    @Override
    @Nonnull
    public Item getTabIconItem() {
        return icon;
    }

    @SideOnly(Side.CLIENT)
    @Nonnull
    public ItemStack getIconItemStack() {
        if (itemstack == null) return super.getIconItemStack();
        else return itemstack;
    }

    public void setItemStack(ItemStack stack) {
        itemstack = stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    @Nonnull
    public String getTranslatedTabLabel() {
        return Enchiridion.translate("creative");
    }
}