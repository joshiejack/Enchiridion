package joshie.enchiridion;

import java.util.List;

import org.lwjgl.input.Keyboard;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IBook;
import joshie.enchiridion.data.book.BookRegistry;
import joshie.enchiridion.gui.book.GuiBook;
import joshie.enchiridion.gui.book.GuiBookCreate;
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
import joshie.enchiridion.gui.library.GuiLibrary;
import joshie.enchiridion.lib.GuiIDs;
import joshie.enchiridion.library.LibraryHelper;
import joshie.enchiridion.library.handlers.ComputerCraftHandler;
import joshie.enchiridion.library.handlers.WarpBookHandler;
import joshie.enchiridion.library.handlers.WriteableBookHandler.GuiScreenWriteable;
import joshie.enchiridion.util.EResourcePack;
import joshie.enchiridion.util.PenguinFont;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class EClientProxy extends ECommonProxy {
    public static KeyBinding libraryKeyBinding;
    
	@Override
	public void onConstruction() {
		try {
    		List<IResourcePack> defaultResourcePacks = ObfuscationReflectionHelper.getPrivateValue(FMLClientHandler.class, FMLClientHandler.instance(), "resourcePackList");
        	defaultResourcePacks.add(EResourcePack.INSTANCE);
    	} catch (Exception e) {}
	}
	
    @Override
    public void setupClient() {
        PenguinFont.load();
        LibraryHelper.resetClient();
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
        
        //Register the keybinding
        libraryKeyBinding = new KeyBinding("enchiridion.key.library", Keyboard.KEY_L, "key.categories.misc");
        ClientRegistry.registerKeyBinding(libraryKeyBinding);
    }
    
    /** GUI HANDLING **/
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int slotID, int y, int z) {
        if (ID == GuiIDs.COMPUTERCRAFT) {
            return ComputerCraftHandler.getComputercraftPrintoutGui(player, slotID);
        } else if (ID == GuiIDs.WARPLIST) {
            return WarpBookHandler.getWarplistGui(player, slotID);
        } else if (ID == GuiIDs.WARPBOOK) {
            return WarpBookHandler.getWarpbookGui(player, slotID);
        } else if (ID == GuiIDs.WRITEABLE) {
            return new GuiScreenWriteable(player, slotID);
        }else if (ID == GuiIDs.LIBRARY) {
            return new GuiLibrary(player.inventory, LibraryHelper.getClientLibraryContents());
        } else if (ID == GuiIDs.BOOK_FORCE) {
            return GuiBook.INSTANCE;
        } else {
            ItemStack held = player.getCurrentEquippedItem();
            if (held != null && held.getItem() == ECommonProxy.book) {
                IBook book = BookRegistry.INSTANCE.getBook(held);
                if (book != null) {
                    return GuiBook.INSTANCE.setBook(book, player.isSneaking());
                } else return GuiBookCreate.INSTANCE.setStack(player.getCurrentEquippedItem());
            }
        }

        return null;
    }
}