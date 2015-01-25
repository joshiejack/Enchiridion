package joshie.enchiridion.library;

import static cpw.mods.fml.common.Loader.isModLoaded;
import static joshie.enchiridion.api.EnchiridionHelper.bookRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModBooks {
    public static void init() {
        if(isModLoaded("Botania")) {
            
        }
        
        if (isModLoaded("HardcoreQuesting")) {
            Item hqm_quest_book = GameRegistry.findItem("HardcoreQuesting", "quest_book");
            if(hqm_quest_book != null) {
                bookRegistry.registerNetworkSwitch(new ItemStack(hqm_quest_book));
            }
        }
        
        if (isModLoaded("Mariculture")) {
            Item mariculture_guide = GameRegistry.findItem("Mariculture", "guide");
            if (mariculture_guide != null) {
                bookRegistry.registerSwitch(new ItemStack(mariculture_guide, 1, 0));
                bookRegistry.registerSwitch(new ItemStack(mariculture_guide, 1, 1));
                bookRegistry.registerSwitch(new ItemStack(mariculture_guide, 1, 2));
                bookRegistry.registerSwitch(new ItemStack(mariculture_guide, 1, 3));
                bookRegistry.registerSwitch(new ItemStack(mariculture_guide, 1, 4));
            }
        }
        
        if(isModLoaded("TConstruct")) {
            Item tconstruct_manual = GameRegistry.findItem("TConstruct", "manual");
            if(tconstruct_manual != null) {
                bookRegistry.registerDefault(new ItemStack(tconstruct_manual, 1, 0));
            }
        }
    }
}
