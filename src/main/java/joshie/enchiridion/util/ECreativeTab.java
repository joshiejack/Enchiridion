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
    public ItemStack createIcon() {
        return new ItemStack(Items.WRITABLE_BOOK);
    }

    @Override
    @SideOnly(Side.CLIENT)
    @Nonnull
    public ItemStack getIcon() {
        if (itemstack.isEmpty()) return super.getIcon();
        else return itemstack;
    }

    public void setItemStack(@Nonnull ItemStack stack) {
        itemstack = stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    @Nonnull
    public String getTranslationKey() {
        return Enchiridion.translate("creative");
    }
}