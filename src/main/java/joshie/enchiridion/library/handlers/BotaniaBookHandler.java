package joshie.enchiridion.library.handlers;

import joshie.enchiridion.helpers.ClientHelper;
import joshie.enchiridion.helpers.StackHelper;
import joshie.enchiridion.library.LibraryHelper;
import joshie.enchiridion.library.ModBooks.ModBookData;
import joshie.enchiridion.network.EPacketHandler;
import joshie.enchiridion.network.PacketOverwrite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiOpenEvent;
import vazkii.botania.client.gui.lexicon.BotaniaHijackLexicon;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BotaniaBookHandler {
    public static boolean hasBook(ItemStack stack, String key) {
        for (ItemStack book : LibraryHelper.storage.getBooks()) {
            if (book.isItemEqual(stack) && ((book.hasTagCompound() && book.stackTagCompound.hasKey(key)) || key.equals(""))) {
                return true;
            }
        }

        return false;
    }

    @SubscribeEvent
    public void openGui(GuiOpenEvent event) {
        if (event.gui == null || LibraryHelper.modBooks == null) return;
        String clazz = event.gui.getClass().getName();
        for (ModBookData data : LibraryHelper.modBooks.books) {
            if (data.item == null || data.openGuiClass.equals("") || hasBook(data.item, data.openGuiNBT)) continue;
            if (data.openGuiClass.equals(clazz)) {
                ItemStack held = ClientHelper.getPlayer().getCurrentEquippedItem();
                if (data.openGuiNBT.equals("") || (held != null && held.hasTagCompound() && held.stackTagCompound.hasKey(data.openGuiNBT))) {
                    ItemStack overwrites = null;
                    try {
                        if (!data.overwrite.equals("")) overwrites = StackHelper.getStackFromString(data.overwrite);
                    } catch (Exception e) {}

                    if (overwrites != null) {
                        LibraryHelper.storage.overwrite(data.item, overwrites);
                    } else LibraryHelper.storage.add(data.item);

                    EPacketHandler.sendToServer(new PacketOverwrite(data.item, overwrites));
                }
            }
        }

        /** Botania fix...**/
        if (clazz.equals("vazkii.botania.client.gui.lexicon.GuiLexicon")) {
            event.gui = new BotaniaHijackLexicon();
        }
    }
}
