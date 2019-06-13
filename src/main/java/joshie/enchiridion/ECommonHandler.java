package joshie.enchiridion;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.data.book.BookRegistry;
import joshie.enchiridion.gui.library.ContainerLibrary;
import joshie.enchiridion.items.ItemBook;
import joshie.enchiridion.items.ItemLibrary;
import joshie.enchiridion.lib.EInfo;
import joshie.enchiridion.lib.GuiIDs;
import joshie.enchiridion.library.LibraryRegistry;
import joshie.enchiridion.library.handlers.*;
import joshie.enchiridion.util.ECreativeTab;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = EInfo.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECommonHandler {
    public static final Item BOOK = new ItemBook(new Item.Properties().group(ECreativeTab.ENCHIRIDION)).setRegistryName(new ResourceLocation(EInfo.MODID, "book"));
    public static final Item LIBRARY = new ItemLibrary(new Item.Properties().group(ECreativeTab.ENCHIRIDION)).setRegistryName(new ResourceLocation(EInfo.MODID, "library"));

    @ObjectHolder(GuiIDs.LIBRARY)
    public static ContainerType<ContainerLibrary> LIBRARY_CONTAINER;

    public static void init() {
        EnchiridionAPI.instance = new EAPIHandler();
        EnchiridionAPI.library = LibraryRegistry.INSTANCE;
        EnchiridionAPI.library.registerBookHandler(new EnchiridionBookHandler()); //Enchiridion
        EnchiridionAPI.library.registerBookHandler(new WritableBookHandler()); //Writeable Books
        EnchiridionAPI.library.registerBookHandler(new WrittenBookHandler()); //Written Books
        EnchiridionAPI.library.registerBookHandler(new TemporarySwitchHandler()); //Default Handler
        EnchiridionAPI.library.registerBookHandler(new RightClickHandler()); //Kept for backwards compatibility
        EnchiridionAPI.library.registerBookHandler(new CopyNBTHandler()); //Copy NBT Handler
    }

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

    @SubscribeEvent
    public static void registerContainers(RegistryEvent.Register<ContainerType<?>> event) {
        event.getRegistry().register(IForgeContainerType.create(ContainerLibrary::new).setRegistryName(GuiIDs.LIBRARY));
    }
}