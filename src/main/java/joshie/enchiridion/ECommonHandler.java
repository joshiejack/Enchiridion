package joshie.enchiridion;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.data.book.BookRegistry;
import joshie.enchiridion.items.ItemEnchiridion;
import joshie.enchiridion.items.ItemLibrary;
import joshie.enchiridion.lib.EInfo;
import joshie.enchiridion.library.LibraryRegistry;
import joshie.enchiridion.library.handlers.*;
import joshie.enchiridion.util.ECreativeTab;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EInfo.MODID)
public class ECommonHandler {
    public static final Item BOOK = new ItemEnchiridion(new Item.Properties().group(ECreativeTab.ENCHIRIDION)).setRegistryName("book");
    public static final Item LIBRARY = new ItemLibrary(new Item.Properties().group(ECreativeTab.ENCHIRIDION)).setRegistryName("library");

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

    /**
     * GUI HANDLING
     **/
    /*@Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int integer1, int handOrdinal, int z) {
        if (ID == GuiIDs.LIBRARY) {
            return new ContainerLibrary(player.inventory, LibraryHelper.getServerLibraryContents(player), HeldHelper.getHandFromOrdinal(handOrdinal));
        } else return null;
    }*/ //TODO
}