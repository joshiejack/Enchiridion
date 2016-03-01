package joshie.enchiridion;

import java.util.List;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.data.book.BookRegistry;
import joshie.enchiridion.gui.book.GuiBook;
import joshie.enchiridion.gui.book.GuiGrid;
import joshie.enchiridion.gui.book.GuiLayers;
import joshie.enchiridion.gui.book.GuiSimpleEditor;
import joshie.enchiridion.gui.book.GuiTimeLine;
import joshie.enchiridion.gui.book.GuiToolbar;
import joshie.enchiridion.gui.book.buttons.ButtonChangeBackground;
import joshie.enchiridion.gui.book.buttons.ButtonChangeIcon;
import joshie.enchiridion.gui.book.buttons.ButtonDeletePage;
import joshie.enchiridion.gui.book.buttons.ButtonInsertBox;
import joshie.enchiridion.gui.book.buttons.ButtonInsertButton;
import joshie.enchiridion.gui.book.buttons.ButtonInsertImage;
import joshie.enchiridion.gui.book.buttons.ButtonInsertItem;
import joshie.enchiridion.gui.book.buttons.ButtonInsertRecipe;
import joshie.enchiridion.gui.book.buttons.ButtonInsertText;
import joshie.enchiridion.gui.book.buttons.ButtonToggleGrid;
import joshie.enchiridion.gui.book.buttons.actions.ActionJumpPage;
import joshie.enchiridion.gui.book.buttons.actions.ActionNextPage;
import joshie.enchiridion.gui.book.buttons.actions.ActionOpenWebpage;
import joshie.enchiridion.gui.book.buttons.actions.ActionPreviousPage;
import joshie.enchiridion.gui.book.features.recipe.RecipeHandlerFurnace;
import joshie.enchiridion.gui.book.features.recipe.RecipeHandlerShapedOre;
import joshie.enchiridion.gui.book.features.recipe.RecipeHandlerShapedVanilla;
import joshie.enchiridion.gui.book.features.recipe.RecipeHandlerShapelessOre;
import joshie.enchiridion.gui.book.features.recipe.RecipeHandlerShapelessVanilla;
import joshie.enchiridion.util.EResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class EClientProxy extends ECommonProxy {
	@Override
	public void onConstruction() {
		try {
    		List<IResourcePack> defaultResourcePacks = ObfuscationReflectionHelper.getPrivateValue(FMLClientHandler.class, FMLClientHandler.instance(), "resourcePackList");
        	defaultResourcePacks.add(EResourcePack.INSTANCE);
    	} catch (Exception e) {}
	}
	
    @Override
    public void setupClient() {
        BookRegistry.INSTANCE.loadBooksFromConfig();
    	ModelLoader.setCustomMeshDefinition(ECommonProxy.book, BookRegistry.INSTANCE);
    	EnchiridionAPI.book = GuiBook.INSTANCE;
        EnchiridionAPI.draw = GuiBook.INSTANCE;
        //Register editor overlays
        EnchiridionAPI.instance.registerEditorOverlay(GuiGrid.INSTANCE);
        EnchiridionAPI.instance.registerEditorOverlay(GuiTimeLine.INSTANCE);
        EnchiridionAPI.instance.registerEditorOverlay(GuiToolbar.INSTANCE);
        EnchiridionAPI.instance.registerEditorOverlay(GuiLayers.INSTANCE);
        EnchiridionAPI.instance.registerEditorOverlay(GuiSimpleEditor.INSTANCE);
        
        //Left aligned buttons
        EnchiridionAPI.instance.registerToolbarButton(new ButtonInsertText());
        EnchiridionAPI.instance.registerToolbarButton(new ButtonInsertImage());
        EnchiridionAPI.instance.registerToolbarButton(new ButtonInsertButton());
        EnchiridionAPI.instance.registerToolbarButton(new ButtonInsertBox());
        EnchiridionAPI.instance.registerToolbarButton(new ButtonInsertItem());
        EnchiridionAPI.instance.registerToolbarButton(new ButtonInsertRecipe());
        
        //Right aligned
        EnchiridionAPI.instance.registerToolbarButton(new ButtonDeletePage());
        EnchiridionAPI.instance.registerToolbarButton(new ButtonChangeBackground());
        EnchiridionAPI.instance.registerToolbarButton(new ButtonChangeIcon());
        EnchiridionAPI.instance.registerToolbarButton(new ButtonToggleGrid());
        
        //Register button actions
        EnchiridionAPI.instance.registerButtonAction(new ActionJumpPage());
        EnchiridionAPI.instance.registerButtonAction(new ActionNextPage());
        EnchiridionAPI.instance.registerButtonAction(new ActionPreviousPage());
        EnchiridionAPI.instance.registerButtonAction(new ActionOpenWebpage());
        
        //Register Recipe Handlers
        EnchiridionAPI.instance.registerRecipeHandler(new RecipeHandlerShapedVanilla());
        EnchiridionAPI.instance.registerRecipeHandler(new RecipeHandlerShapedOre());
        EnchiridionAPI.instance.registerRecipeHandler(new RecipeHandlerShapelessVanilla());
        EnchiridionAPI.instance.registerRecipeHandler(new RecipeHandlerShapelessOre());
        EnchiridionAPI.instance.registerRecipeHandler(new RecipeHandlerFurnace());
        
        //Register the Enchiridion Book
        EnchiridionAPI.instance.registerModWithBooks("enchiridion");
    }
}