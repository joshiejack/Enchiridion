package enchiridion;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class TooltipHandler {
	public static boolean PRINT;

	@ForgeSubscribe
	public void addToolTip(ItemTooltipEvent event) {
		ItemStack stack = event.itemStack;
		List list = event.toolTip;
        String str = stack.getUnlocalizedName().substring(5);
		if(stack.getHasSubtypes()) str = str + " " + stack.getItemDamage();
		list.add(str);
		
		
		if(PRINT) System.out.println("Key for " + stack.getDisplayName() + " = " + str);
	}
}
