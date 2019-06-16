package joshie.enchiridion.util;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.lib.EInfo;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class EItemGroup extends ItemGroup {
    public static final EItemGroup ENCHIRIDION = new EItemGroup(EInfo.MODID);
    public ItemStack stack = ItemStack.EMPTY;

    public EItemGroup(String label) {
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
        if (this.stack.isEmpty()) return super.getIcon();
        else return this.stack;
    }

    public void setItemStack(@Nonnull ItemStack stack) {
        this.stack = stack;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    @Nonnull
    public String getTranslationKey() {
        return Enchiridion.format("creative");
    }
}