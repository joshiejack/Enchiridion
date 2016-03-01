package joshie.enchiridion;

import java.util.List;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.books.BookRegistry;
import joshie.enchiridion.books.BookResourcePack;
import joshie.enchiridion.books.buttons.ButtonChangeBackground;
import joshie.enchiridion.books.buttons.ButtonChangeIcon;
import joshie.enchiridion.books.buttons.ButtonDeletePage;
import joshie.enchiridion.books.buttons.ButtonInsertBox;
import joshie.enchiridion.books.buttons.ButtonInsertButton;
import joshie.enchiridion.books.buttons.ButtonInsertImage;
import joshie.enchiridion.books.buttons.ButtonInsertItem;
import joshie.enchiridion.books.buttons.ButtonInsertRecipe;
import joshie.enchiridion.books.buttons.ButtonInsertText;
import joshie.enchiridion.books.buttons.ButtonToggleGrid;
import joshie.enchiridion.books.features.actions.ActionJumpPage;
import joshie.enchiridion.books.features.actions.ActionNextPage;
import joshie.enchiridion.books.features.actions.ActionOpenWebpage;
import joshie.enchiridion.books.features.actions.ActionPreviousPage;
import joshie.enchiridion.books.features.recipe.RecipeHandlerFurnace;
import joshie.enchiridion.books.features.recipe.RecipeHandlerShapedOre;
import joshie.enchiridion.books.features.recipe.RecipeHandlerShapedVanilla;
import joshie.enchiridion.books.features.recipe.RecipeHandlerShapelessOre;
import joshie.enchiridion.books.features.recipe.RecipeHandlerShapelessVanilla;
import joshie.enchiridion.books.gui.GuiBook;
import joshie.enchiridion.books.gui.GuiGrid;
import joshie.enchiridion.books.gui.GuiLayers;
import joshie.enchiridion.books.gui.GuiSimpleEditor;
import joshie.enchiridion.books.gui.GuiTimeLine;
import joshie.enchiridion.books.gui.GuiToolbar;
import net.minecraft.client.resources.IResourcePack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class EClientProxy extends ECommonProxy {
	@Override
	public void onConstruction() {
		try {
    		List<IResourcePack> defaultResourcePacks = ObfuscationReflectionHelper.getPrivateValue(FMLClientHandler.class, FMLClientHandler.instance(), "resourcePackList");
        	defaultResourcePacks.add(BookResourcePack.INSTANCE);
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