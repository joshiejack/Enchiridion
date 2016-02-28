package joshie.enchiridion.books;

import java.util.List;

import joshie.enchiridion.Enchiridion;
import joshie.lib.item.ItemCoreMulti;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class ItemBook extends ItemCoreMulti {
	@Override
    public String getItemStackDisplayName(ItemStack stack) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            return Enchiridion.translate("new");
        }
		
		Book book = BookRegistry.INSTANCE.getBook(stack);
		return book == null ? Enchiridion.translate("new") : book.displayName;
    }
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		player.openGui(Enchiridion.instance, 0, world, 0, 0, 0);
		return stack;
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		list.add(new ItemStack(item));

		for (String book : BookRegistry.INSTANCE.getUniqueNames()) {
			ItemStack stack = new ItemStack(item);
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setString("identifier", book);
			list.add(stack);
		}
	}
}
