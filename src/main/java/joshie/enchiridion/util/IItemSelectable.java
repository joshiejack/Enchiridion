package joshie.enchiridion.util;

import net.minecraft.item.ItemStack;

public interface IItemSelectable {
    void setItemStack(ItemStack stack);

    boolean getTooltipsEnabled();

    void setTooltips(boolean value);
}