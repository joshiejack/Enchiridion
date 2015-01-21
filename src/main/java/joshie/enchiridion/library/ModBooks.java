package joshie.enchiridion.library;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModBooks {
    public static Item guide;
    
    public static void init() {
        if(Loader.isModLoaded("Mariculture")) {
            guide = GameRegistry.findItem("Mariculture", "guide");
            if(guide != null) {
	            LibraryRegistry.registerSwitch(new ItemStack(guide, 1, 0));
	            LibraryRegistry.registerSwitch(new ItemStack(guide, 1, 1));
	            LibraryRegistry.registerSwitch(new ItemStack(guide, 1, 2));
	            LibraryRegistry.registerSwitch(new ItemStack(guide, 1, 3));
	            LibraryRegistry.registerSwitch(new ItemStack(guide, 1, 4));
            }
        }
    }
}
