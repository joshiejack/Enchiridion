package joshie.enchiridion;

import java.io.File;

import joshie.enchiridion.api.IBookHandler;
import joshie.enchiridion.api.IEnchiridionAPI;
import joshie.enchiridion.designer.BookRegistry;
import joshie.enchiridion.library.BookHandlerRegistry;
import net.minecraft.item.ItemStack;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public class EAPIHandler implements IEnchiridionAPI {
    @Override
    public void registerBookData(ItemStack stack, String identifier) {
        BookRegistry.registerItemStack(identifier, stack);
    }

    @Override
    public void registerModBooks(String id) {
        /** Grab the modid and the assets path **/
        String modid = id;
        String assetspath = id.toLowerCase();
        if (id.contains(":")) {
            String[] split = id.split(":");
            modid = split[0];
            assetspath = split[1].toLowerCase();
        }
        
        /** Find this mods container **/
        ModContainer mod = null;
        for (ModContainer container: Loader.instance().getActiveModList()) {
            if (container.getModId().equals(modid)) {
                mod = container;
                break;
            }
        }
        
        /** Attempt to register in dev or in jar **/
        if (mod == null) {
            ELogger.log(Level.ERROR, "When attempting to register books with Enchiridion 2 a mod with the modid " + modid + " could not be found");
        } else {
            String jar = mod.getSource().toString();
            if (jar.contains(".jar") || jar.contains(".zip")) {
                BookRegistry.registerModInJar(assetspath, new File(jar));
            } else {
                BookRegistry.registerModInDev(assetspath, mod.getSource());
            }
        }
    }

    @Override
    public void registerBookHandler(IBookHandler handler) {
        BookHandlerRegistry.registerHandler(handler);
    }
}
