package joshie.enchiridion.handlers;

import java.io.File;

import org.apache.logging.log4j.Level;

import joshie.enchiridion.ELogger;
import joshie.enchiridion.api.IBookEditorOverlay;
import joshie.enchiridion.api.IBookHandler;
import joshie.enchiridion.api.IButtonAction;
import joshie.enchiridion.api.IEnchiridionAPI;
import joshie.enchiridion.api.IRecipeHandler;
import joshie.enchiridion.api.IToolbarButton;
import joshie.enchiridion.books.BookRegistry;
import joshie.enchiridion.books.gui.GuiBook;
import joshie.enchiridion.books.gui.GuiSimpleEditorButton;
import joshie.enchiridion.books.gui.GuiToolbar;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

public class EAPIHandler implements IEnchiridionAPI {
	@Override
	public void registerEditorOverlay(IBookEditorOverlay overlay) {
		GuiBook.INSTANCE.registerOverlay(overlay);
	}
	
    @Override
    public void registerBookData(ItemStack stack, String identifier) {
        //BookRegistry.registerItemStack(identifier, stack);
    }
    

	@Override
	public void registerToolbarButton(IToolbarButton button) {
		GuiToolbar.INSTANCE.registerButton(button);
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
                BookRegistry.INSTANCE.registerModInJar(assetspath, new File(jar));
            } else {
                BookRegistry.INSTANCE.registerModInDev(assetspath, mod.getSource());
            }
        }
    }

    @Override
    public void registerBookHandler(IBookHandler handler) {
        //BookHandlerRegistry.registerHandler(handler);
    }
    
    @Override
    public void registerRecipeHandler(IRecipeHandler handler) {
        //FeatureRecipe.handlers.add(handler);
        ELogger.log(Level.INFO, "Registered a new recipe handler: " + handler.getRecipeName());
    }

	@Override
	public void registerButtonAction(IButtonAction action) {
		GuiSimpleEditorButton.INSTANCE.registerAction(action);
	}
}
