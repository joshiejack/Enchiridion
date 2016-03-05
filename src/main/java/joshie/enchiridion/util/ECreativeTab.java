package joshie.enchiridion.util;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.lib.EInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ECreativeTab extends CreativeTabs {
    public static ECreativeTab enchiridion = new ECreativeTab(EInfo.MODPATH);
    public final Item icon = Items.writable_book;

    public ECreativeTab(String label) {
        super(label);
    }

    @Override
    public Item getTabIconItem() {
        return icon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getTranslatedTabLabel() {
        return Enchiridion.translate("creative");
    }
}
