package joshie.enchiridion.util;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.lib.EInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ECreativeTab extends CreativeTabs {
    public static final ECreativeTab ENCHIRIDION = new ECreativeTab(EInfo.MODID);
    @Nonnull
    public ItemStack itemstack = ItemStack.EMPTY;

    public ECreativeTab(String label) {
        super(label);
    }

    @Override
    @Nonnull
    public ItemStack getTabIconItem() {
        return new ItemStack(Items.WRITABLE_BOOK);
    }

    @SideOnly(Side.CLIENT)
    @Nonnull
    public ItemStack getIconItemStack() {
        if (itemstack.isEmpty()) return super.getIconItemStack();
        else return itemstack;
    }

    public void setItemStack(@Nonnull ItemStack stack) {
        itemstack = stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    @Nonnull
    public String getTranslatedTabLabel() {
        return Enchiridion.translate("creative");
    }
}