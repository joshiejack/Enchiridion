package joshie.enchiridion;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.recipe.IRecipeHandler;
import joshie.enchiridion.data.book.BookRegistry;
import joshie.enchiridion.data.book.Page;
import joshie.enchiridion.data.book.Template;
import joshie.enchiridion.gui.book.*;
import joshie.enchiridion.gui.book.buttons.*;
import joshie.enchiridion.gui.book.buttons.actions.*;
import joshie.enchiridion.gui.book.features.recipe.RecipeHandlerFurnace;
import joshie.enchiridion.gui.book.features.recipe.RecipeHandlerShapedVanilla;
import joshie.enchiridion.gui.book.features.recipe.RecipeHandlerShapelessVanilla;
import joshie.enchiridion.gui.library.GuiLibrary;
import joshie.enchiridion.helpers.DefaultHelper;
import joshie.enchiridion.helpers.EditHelper;
import joshie.enchiridion.items.SmartLibrary;
import joshie.enchiridion.lib.EInfo;
import joshie.enchiridion.library.LibraryHelper;
import joshie.enchiridion.util.ECreativeTab;
import joshie.enchiridion.util.ELocation;
import joshie.enchiridion.util.EResourcePack;
import joshie.enchiridion.util.PenguinFont;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

public class EClientHandler {
    public static KeyBinding libraryKeyBinding;

    public static void setupClient() {
        Minecraft.getInstance().getResourceManager().addResourcePack(EResourcePack.INSTANCE);
        ScreenManager.registerFactory(ECommonHandler.LIBRARY_CONTAINER, GuiLibrary::new);
        LibraryHelper.resetClient();
        BookRegistry.INSTANCE.loadBooksFromConfig();
        MinecraftForge.EVENT_BUS.register(new SmartLibrary());
        EnchiridionAPI.book = GuiBook.INSTANCE;
        EnchiridionAPI.draw = GuiBook.INSTANCE;
        EnchiridionAPI.editor = new EditHelper();
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
        EnchiridionAPI.instance.registerToolbarButton(new ButtonInsertPreviewWindow());

        //Right aligned
        EnchiridionAPI.instance.registerToolbarButton(new ButtonDeletePage());
        EnchiridionAPI.instance.registerToolbarButton(new ButtonChangeBackground());
        EnchiridionAPI.instance.registerToolbarButton(new ButtonChangeIcon());
        EnchiridionAPI.instance.registerToolbarButton(new ButtonToggleGrid());
        EnchiridionAPI.instance.registerToolbarButton(new ButtonToggleScrollable());
        EnchiridionAPI.instance.registerToolbarButton(new ButtonSaveTemplate());
        EnchiridionAPI.instance.registerToolbarButton(new ButtonInsertTemplate());

        //Register button actions
        EnchiridionAPI.instance.registerButtonAction(new ActionJumpPage());
        EnchiridionAPI.instance.registerButtonAction(new ActionNextPage());
        EnchiridionAPI.instance.registerButtonAction(new ActionPreviousPage());
        EnchiridionAPI.instance.registerButtonAction(new ActionOpenWebpage());
        EnchiridionAPI.instance.registerButtonAction(new ActionToggleLayer());
        EnchiridionAPI.instance.registerButtonAction(new ActionExecuteCommand());

        //Register Recipe Handlers
        EnchiridionAPI.instance.registerRecipeHandler(new RecipeHandlerShapedVanilla());
        //EnchiridionAPI.instance.registerRecipeHandler(new RecipeHandlerShapedOre());
        EnchiridionAPI.instance.registerRecipeHandler(new RecipeHandlerShapelessVanilla());
        //EnchiridionAPI.instance.registerRecipeHandler(new RecipeHandlerShapelessOre());
        EnchiridionAPI.instance.registerRecipeHandler(new RecipeHandlerFurnace());
        //attemptToRegisterRecipeHandler(RecipeHandlerMTAdvancedShaped.class, "crafttweaker");
        //attemptToRegisterRecipeHandler(RecipeHandlerMTAdvancedShapeless.class, "crafttweaker");

        //Register Button Template
        Template template = new Template("enchiridion_default_buttons", "Turn Page Arrows", new ELocation("default_buttons_thumbnail"), DefaultHelper.addArrows(new Page(0)));
        EnchiridionAPI.instance.registerTemplate(template);

        //Register the Enchiridion Book
        EnchiridionAPI.instance.registerModWithBooks(EInfo.MODID);

        //Register the keybinding
        if (EConfig.SETTINGS.libraryAsHotkey.get()) {
            libraryKeyBinding = new KeyBinding("enchiridion.key.library", GLFW.GLFW_KEY_I, "key.categories.misc");
            ClientRegistry.registerKeyBinding(libraryKeyBinding);
        }

        ItemStack book = new ItemStack(ECommonHandler.BOOK);
        book.setTag(new CompoundNBT());
        if (book.getTag() != null) {
            book.getTag().putString("identifier", "enchiridion");
        }
        ECreativeTab.ENCHIRIDION.setItemStack(book);
        setupFont();
    }

    private static void attemptToRegisterRecipeHandler(Class clazz, String mod) {
        try {
            if (ModList.get().isLoaded(mod)) {
                IRecipeHandler handler = (IRecipeHandler) clazz.newInstance();
                if (handler != null) EnchiridionAPI.instance.registerRecipeHandler(handler);
            }
        } catch (Exception ignored) {
        }
    }

    public static void setupFont() {
        PenguinFont.load();
        /* Colorize the books */
        /*Minecraft.getInstance().getItemColors().register((stack, tintIndex) -> { //TODO
                ItemStack current = LibraryHelper.getLibraryContents(MCClientHelper.getPlayer()).getCurrentBookItem();
                if (!current.isEmpty()) {
                    return Minecraft.getInstance().getItemColors().getColor(current, tintIndex);
                }
            return -1;
        }, ECommonHandler.LIBRARY);*/
    }

    /*@Override
    public Object getClientGuiElement(int ID, PlayerEntity player, World world, int slotID, int handOrdinal, int z) { //TODO
        if (ID == GuiIDs.WRITABLE) {
            return new GuiScreenWritable(player, slotID);
        } else if (ID == GuiIDs.WRITTEN) {
            return new GuiScreenBook(player, EnchiridionAPI.library.getLibraryInventory(player).getStackInSlot(slotID), false);
        } else if (ID == GuiIDs.LIBRARY) {
            return new GuiLibrary(player.inventory, LibraryHelper.getClientLibraryContents(), HeldHelper.getHandFromOrdinal(handOrdinal));
        } else if (ID == GuiIDs.BOOK_FORCE) {
            return GuiBook.INSTANCE;
        }
        return null;
    }*/
}