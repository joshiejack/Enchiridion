package joshie.enchiridion;

import java.util.List;

import joshie.enchiridion.api.book.IBook;
import joshie.enchiridion.data.book.BookRegistry;
import joshie.enchiridion.lib.GuiIDs;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class ItemEnchiridion extends Item {
    public ItemEnchiridion() {
        setHasSubtypes(true);
    }
    
	@Override
    public String getItemStackDisplayName(ItemStack stack) {
	    if (stack.getItemDamage() == 1) {
	        return Enchiridion.translate("library");
	    }
	    
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            return Enchiridion.translate("new");
        }
		
		IBook book = BookRegistry.INSTANCE.getBook(stack);
		return book == null ? Enchiridion.translate("new") : book.getDisplayName();
    }
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
	    if (stack.getItemDamage() == 1) player.openGui(Enchiridion.instance, GuiIDs.LIBRARY, world, 0, 0, 0);
	    else player.openGui(Enchiridion.instance, GuiIDs.BOOK, world, 0, 0, 0);
		return stack;
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
	    if (EConfig.libraryAsItem) list.add(new ItemStack(item, 1, 1));
	    
		list.add(new ItemStack(item));

		for (String book : BookRegistry.INSTANCE.getUniqueNames()) {
			ItemStack stack = new ItemStack(item);
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setString("identifier", book);
			list.add(stack);
		}
	}
	
	@Override
    public Item setUnlocalizedName(String unlocalizedName) {
        super.setUnlocalizedName(unlocalizedName);
        GameRegistry.registerItem(this, unlocalizedName);
        return this;
    }
}
