package joshie.enchiridion.items;

import joshie.enchiridion.data.book.BookRegistry;
import joshie.enchiridion.lib.EInfo;
import joshie.enchiridion.util.ECreativeTab;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EInfo.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EItems {
    public static final Item BOOK = new ItemBook(new Item.Properties().group(ECreativeTab.ENCHIRIDION)).setRegistryName(new ResourceLocation(EInfo.MODID, "book"));
    public static final Item LIBRARY = new ItemLibrary(new Item.Properties().group(ECreativeTab.ENCHIRIDION)).setRegistryName(new ResourceLocation(EInfo.MODID, "library"));


    @SubscribeEvent
    public static void registerItem(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(BOOK);
        event.getRegistry().register(LIBRARY);

        for (String book : BookRegistry.INSTANCE.getUniqueNames()) {
            ItemStack stack = new ItemStack(BOOK);
            stack.setTag(new CompoundNBT());
            if (stack.getTag() != null) {
                stack.getTag().putString("identifier", book);
            }
            event.getRegistry().register(stack.getItem());
        }
    }
}