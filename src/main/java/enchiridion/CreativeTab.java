package enchiridion;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTab extends CreativeTabs {
	public static CreativeTab books = new CreativeTab("enchiridion");
    public ItemStack icon = new ItemStack(Item.writableBook);
    public CreativeTab(String label) {
        super(label);
    }

    @Override
    public ItemStack getIconItemStack () {
        return icon;
    }

	@Override
	public Item getTabIconItem() {
		return icon.getItem();
	}
}
