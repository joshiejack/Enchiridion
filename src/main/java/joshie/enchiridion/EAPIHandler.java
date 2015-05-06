package joshie.enchiridion;

import java.io.File;

import joshie.enchiridion.api.IBookHandler;
import joshie.enchiridion.api.IEnchiridionAPI;
import joshie.enchiridion.designer.BookRegistry;
import joshie.enchiridion.library.BookHandlerRegistry;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public class EAPIHandler implements IEnchiridionAPI {
    @Override
    public void registerBookData(ItemStack stack, String identifier) {
        BookRegistry.registerItemStack(identifier, stack);
    }

    @Override
    public void registerModBooks(String assetspath) {
        ModContainer mod = Loader.instance().activeModContainer();
        String jar = mod.getSource().toString();
        if (jar.contains(".jar") || jar.contains(".zip")) {
            BookRegistry.registerModInJar(new File(jar));
        } else {
            BookRegistry.registerModInDev(assetspath, mod.getSource());
        }
    }

    @Override
    public void registerBookHandler(IBookHandler handler) {
        BookHandlerRegistry.registerHandler(handler);
    }
}
