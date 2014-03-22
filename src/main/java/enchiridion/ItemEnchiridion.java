package enchiridion;

import java.util.List;
import java.util.Map.Entry;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enchiridion.CustomBooks.BookInfo;
import enchiridion.api.Formatting;

public class ItemEnchiridion extends Item {
	public IIcon pages;
	public IIcon[] icons;
	public static final int COUNT = 2;
	public static final int GUIDE = 0;
	public static final int BINDER = 1;

	public ItemEnchiridion() {
		setHasSubtypes(true);
		setCreativeTab(CreativeTab.books);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		if (stack.getItemDamage() != GUIDE) return super.getItemStackDisplayName(stack);
		else if (stack.hasTagCompound()) {
			return CustomBooks.getBookInfo(stack).displayName;
		} else return super.getItemStackDisplayName(stack);
	}
	
	public String getName(int meta) {
		switch(meta) {
			case GUIDE: 	return "guide";
			case BINDER:	return "binder";
			default:		return null;
		}
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean var) {
		if (stack.getItemDamage() == GUIDE) {
			if (stack.hasTagCompound()) {
				list.add(Formatting.translate("enchiridion.by") + " " + CustomBooks.getBookInfo(stack).author);
			}
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		int meta = stack.getItemDamage();
		switch (meta) {
			case GUIDE: player.openGui(Enchiridion.instance, 0, player.worldObj, 0, 0, 0);
			default: player.openGui(Enchiridion.instance, 0, player.worldObj, 0, 0, 0);
		}

		return stack;
	}
	
	@SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass) {
        if(stack.getItemDamage() != GUIDE) return 16777215;
        else if(pass == 0) {
        	if (stack.hasTagCompound()) {
        		BookInfo info = CustomBooks.getBookInfo(stack);
        		return info.bookColor;
        	}
        } 
        
        return 16777215;
    }
	
	@SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }
	
	@SideOnly(Side.CLIENT)
	public int getRenderPasses(int meta) {
		return meta == GUIDE? 2: 1;
    }
	
	@SideOnly(Side.CLIENT)
	 public IIcon getIcon(ItemStack stack, int pass) {
        if(stack.getItemDamage() != GUIDE) return super.getIcon(stack, pass);
        else if(pass == 0) {
        	return icons[stack.getItemDamage()];
        }
        
        return pages;
    }

	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (Entry<String, BookInfo> books : CustomBooks.bookInfo.entrySet()) {
			ItemStack guide = new ItemStack(item, 1, GUIDE);
			guide.setTagCompound(new NBTTagCompound());
			guide.stackTagCompound.setString(CustomBooks.id, books.getKey());
			list.add(guide);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		icons = new IIcon[COUNT];

		for (int i = 0; i < icons.length; i++) {
			String name = getName(i);
			if(name != null) {
				icons[i] = iconRegister.registerIcon("books" + ":" + name);
			}
		}
		
		pages = iconRegister.registerIcon("books:pages");
	}
}
