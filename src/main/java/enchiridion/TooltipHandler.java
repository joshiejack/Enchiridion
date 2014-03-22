package enchiridion;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TooltipHandler {
	public static boolean PRINT;

	@SubscribeEvent
	public void addToolTip(ItemTooltipEvent event) {
		ItemStack stack = event.itemStack;
		List list = event.toolTip;
		String str = Item.itemRegistry.getNameForObject(stack.getItem());
		if(stack.getHasSubtypes()) str = str + " " + stack.getItemDamage();
		if(stack.hasTagCompound()) str = str + " 0 " + stack.stackTagCompound.toString();
		list.add(str);
		
		
		if(PRINT) System.out.println("Key for " + stack.getDisplayName() + " = " + str);
	}
}
