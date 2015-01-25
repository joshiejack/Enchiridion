package joshie.enchiridion.library;

import static joshie.enchiridion.api.EnchiridionHelper.bookRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModBooks {
    public static Item guide;

    public static void init() {
        if (Loader.isModLoaded("Mariculture")) {
            guide = GameRegistry.findItem("Mariculture", "guide");
            if (guide != null) {
                bookRegistry.registerSwitch(new ItemStack(guide, 1, 0));
                bookRegistry.registerSwitch(new ItemStack(guide, 1, 1));
                bookRegistry.registerSwitch(new ItemStack(guide, 1, 2));
                bookRegistry.registerSwitch(new ItemStack(guide, 1, 3));
                bookRegistry.registerSwitch(new ItemStack(guide, 1, 4));
            }
        }
    }
}
