package joshie.enchiridion.api;

import net.minecraft.item.ItemStack;

public interface ILibraryHelper {
    public void registerDefault(ItemStack stack);
    public void registerSwitch(ItemStack stack);
    public void registerNetworkSwitch(ItemStack stack);
    public void register(ItemStack stack, IBookHandler handler);
}
