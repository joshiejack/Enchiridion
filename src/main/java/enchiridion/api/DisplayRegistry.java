package enchiridion.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class DisplayRegistry {
	/** Caches **/
	private static final HashMap<String, Icon> fluidsCache = new HashMap();
	private static final HashMap<String, ItemStack> itemCache = new HashMap();
	private static final HashMap<String, Integer[]> metaCache = new HashMap();
	private static final ArrayList<String> oreDicCache = new ArrayList<String>();
	
	static {
		registerShorthand("brick", new ItemStack(Item.brick));
		registerShorthand("brickBlock", new ItemStack(Block.brick));
		registerShorthand("quartzSlab", new ItemStack(Block.stoneSingleSlab, 1, 7));
		registerShorthand("stoneSlab", new ItemStack(Block.stoneSingleSlab, 1, 0));
		registerShorthand("minecraft:bookshelf", new ItemStack(Block.bookShelf));
		registerShorthand("minecraft:hopper", new ItemStack(Block.hopperBlock));
		registerShorthand("minecraft:sponge", new ItemStack(Block.sponge));
		registerShorthand("piston", new ItemStack(Block.pistonBase));
		registerShorthand("reeds", new ItemStack(Item.reed));
		registerMetaCycling(Item.itemsList[Block.cloth.blockID], "wool", new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 });
	}

	// Registers Ore Dictionary names to cycle, by default no ore dictionary names will cycle
	// Mariculture adds everything to the cycling list
	public static void registerOreDictionaryCycling(String name) {
		if(!oreDicCache.contains(name)) oreDicCache.add(name);
	}
	
	// Register items with their meta values to cycle through on the use of a key
	public static void registerMetaCycling(Item item, String key, Integer[] metas) {
		metaCache.put(key, metas);
		registerShorthand(key, new ItemStack(item));
	}
	
	//Register an item shorthand key for calling throughout
	public static void registerShorthand(String key, ItemStack stack) {
		if(!itemCache.containsKey(key)) itemCache.put(key, stack);
	}
	
	public static ItemStack getIcon(String str) {
		if(str.equals("")) return null;
		if (itemCache.containsKey(str)) return itemCache.get(str);
		else {
			if(OreDictionary.getOres(str).size() > 0) itemCache.put(str, OreDictionary.getOres(str).get(0));
			else {
				ItemStack stack = StackHelper.getStackFromString(str);
				itemCache.put(str, stack);
			}
			
			return itemCache.get(str);
		}
	}

	public static Icon getFluidIcon(String str) {
		if (fluidsCache.containsKey(str))
			return fluidsCache.get(str);
		else {
			Fluid fluid = FluidRegistry.getFluid(str);
			if(fluid == null) return null;
			fluidsCache.put(str, fluid.getIcon());
			return FluidRegistry.getFluid(str).getIcon();
		}
	}

	public static void updateIcons() {
		for (String str : oreDicCache) {
			ArrayList<ItemStack> ores = OreDictionary.getOres(str);
			if(ores != null && ores.size() > 0) {
				itemCache.put(str, ores.get(GuideHandler.rand.nextInt(ores.size())));
			}
		}

		for (Entry<String, Integer[]> meta : metaCache.entrySet()) {
			ItemStack stack = itemCache.get(meta.getKey());
			if(stack != null) {
				if(meta != null && meta.getValue().length > 0) {
					stack.setItemDamage(meta.getValue()[GuideHandler.rand.nextInt(meta.getValue().length)]);
					itemCache.put(meta.getKey(), stack);
				}
			}
		}
	}
}
