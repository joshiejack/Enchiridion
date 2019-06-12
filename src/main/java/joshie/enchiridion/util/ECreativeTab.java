package joshie.enchiridion.util;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.lib.EInfo;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class ECreativeTab extends ItemGroup {
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
    @OnlyIn(Dist.CLIENT)
    @Nonnull
    public ItemStack getIcon() {
        if (itemstack.isEmpty()) return super.getIcon();
        else return itemstack;
    }

    public void setItemStack(@Nonnull ItemStack stack) {
        itemstack = stack;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    @Nonnull
    public String getTranslationKey() {
        return Enchiridion.format("creative");
    }
}