package joshie.enchiridion.items;

import joshie.enchiridion.data.book.BookRegistry;
import joshie.enchiridion.lib.EInfo;
import joshie.enchiridion.util.EItemGroup;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EInfo.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EItems {
    public static final Item LIBRARY = new ItemLibrary(new Item.Properties().group(EItemGroup.ENCHIRIDION)).setRegistryName(new ResourceLocation(EInfo.MODID, "library"));
    public static final Item BOOK = new ItemBook(new Item.Properties().group(EItemGroup.ENCHIRIDION)).setRegistryName(new ResourceLocation(EInfo.MODID, "book"));

    @SubscribeEvent
    public static void registerItem(RegistryEvent.Register<Item> event) {
        BookRegistry.INSTANCE.loadBooksFromConfig(); //Needs to be called before items get registered
        event.getRegistry().register(LIBRARY);
        event.getRegistry().register(BOOK);
    }
}