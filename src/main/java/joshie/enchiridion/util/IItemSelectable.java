package joshie.enchiridion.util;

import net.minecraft.item.ItemStack;

public interface IItemSelectable {
    public void setItemStack(ItemStack stack);
    public boolean getTooltipsEnabled();
    public void setTooltips(boolean value);
}
