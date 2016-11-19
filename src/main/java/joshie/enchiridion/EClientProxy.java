package joshie.enchiridion;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IBook;
import joshie.enchiridion.api.recipe.IRecipeHandler;
import joshie.enchiridion.data.book.BookRegistry;
import joshie.enchiridion.data.book.Page;
import joshie.enchiridion.data.book.Template;
import joshie.enchiridion.gui.book.*;
import joshie.enchiridion.gui.book.buttons.*;
import joshie.enchiridion.gui.book.buttons.actions.*;
import joshie.enchiridion.gui.book.features.recipe.*;
import joshie.enchiridion.gui.library.GuiLibrary;
import joshie.enchiridion.helpers.DefaultHelper;
import joshie.enchiridion.helpers.EditHelper;
import joshie.enchiridion.helpers.HeldHelper;
import joshie.enchiridion.helpers.MCClientHelper;
import joshie.enchiridion.items.SmartLibrary;
import joshie.enchiridion.lib.EInfo;
import joshie.enchiridion.lib.GuiIDs;
import joshie.enchiridion.library.LibraryHelper;
import joshie.enchiridion.library.handlers.WriteableBookHandler.GuiScreenWriteable;
import joshie.enchiridion.util.ECreativeTab;
import joshie.enchiridion.util.ELocation;
import joshie.enchiridion.util.EResourcePack;
import joshie.enchiridion.util.PenguinFont;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class EClientProxy extends ECommonProxy {
    public static final ModelResourceLocation BOOK_RESOURCE = new ModelResourceLocation(new ResourceLocation(EInfo.MODID, "book"), "inventory");
    public static KeyBinding libraryKeyBinding;
    public static ModelResourceLocation library;
    public static ModelResourceLocation libraryItem;

    @Override
    public void onConstruction() {
        try {
            List<IResourcePack> defaultResourcePacks = ObfuscationReflectionHelper.getPrivateValue(FMLClientHandler.class, FMLClientHandler.instance(), "resourcePackList");
            defaultResourcePacks.add(EResourcePack.INSTANCE);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void setupClient() {
        LibraryHelper.resetClient();
        BookRegistry.INSTANCE.loadBooksFromConfig();
        ModelBakery.registerItemVariants(ECommonProxy.book, BOOK_RESOURCE);
        MinecraftForge.EVENT_BUS.register(new SmartLibrary());
        ModelLoader.setCustomMeshDefinition(ECommonProxy.book, BookRegistry.INSTANCE);
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
        EnchiridionAPI.instance.registerRecipeHandler(new RecipeHandlerShapedOre());
        EnchiridionAPI.instance.registerRecipeHandler(new RecipeHandlerShapelessVanilla());
        EnchiridionAPI.instance.registerRecipeHandler(new RecipeHandlerShapelessOre());
        EnchiridionAPI.instance.registerRecipeHandler(new RecipeHandlerFurnace());
        //attemptToRegisterRecipeHandler(RecipeHandlerMTAdvancedShaped.class, "MineTweaker3"); //TODO Re-implement when MT is updated
        //attemptToRegisterRecipeHandler(RecipeHandlerMTAdvancedShapeless.class, "MineTweaker3"); //TODO Re-implement when MT is updated

        //Register Button Template
        Template template = new Template("enchiridion_default_buttons", "Turn Page Arrows", new ELocation("default_buttons_thumbnail"), DefaultHelper.addArrows(new Page(0)));
        EnchiridionAPI.instance.registerTemplate(template);

        //Register the Enchiridion Book
        EnchiridionAPI.instance.registerModWithBooks(EInfo.MODID);
        //Setup the models for the library
        if (EConfig.libraryAsItem) {
            library = new ModelResourceLocation(new ResourceLocation(EInfo.MODID, "library"), "inventory");
            ModelBakery.registerItemVariants(ECommonProxy.book, library); //Load in the library texture
            libraryItem = new ModelResourceLocation(new ResourceLocation(EInfo.MODID, "libraryitem"), "inventory");
        }

        //Register the keybinding
        if (EConfig.libraryAsHotkey) {
            libraryKeyBinding = new KeyBinding("enchiridion.key.library", Keyboard.KEY_L, "key.categories.misc");
            ClientRegistry.registerKeyBinding(libraryKeyBinding);
        }

        ItemStack book = new ItemStack(ECommonProxy.book);
        book.setTagCompound(new NBTTagCompound());
        book.getTagCompound().setString("identifier", "enchiridion");
        ECreativeTab.ENCHIRIDION.setItemStack(book);
    }

    private void attemptToRegisterRecipeHandler(Class clazz, String mod) {
        try {
            if (Loader.isModLoaded(mod)) {
                IRecipeHandler handler = (IRecipeHandler) clazz.newInstance();
                if (handler != null) EnchiridionAPI.instance.registerRecipeHandler(handler);
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void setupFont() {
        PenguinFont.load();
        /* Colorize the books */
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((stack, tintIndex) -> {
            if (stack.getItemDamage() == 1) {
                ItemStack current = LibraryHelper.getLibraryContents(MCClientHelper.getPlayer()).getCurrentBookItem();
                if (!current.isEmpty()) {
                    return Minecraft.getMinecraft().getItemColors().getColorFromItemstack(current, tintIndex);
                }
            }

            return -1;
        }, ECommonProxy.book);
    }

    /**
     * GUI HANDLING
     **/
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int slotID, int handOrdinal, int z) { //TODO Re-add mod support when updated CC of Warp Book is updated to 1.11
        if (ID == GuiIDs.COMPUTERCRAFT) {
            //return ComputerCraftHandler.getComputercraftPrintoutGui(player, slotID);
        } else if (ID == GuiIDs.WARPLIST) {
            //return WarpBookHandler.getWarplistGui(player, slotID);
        } else if (ID == GuiIDs.WARPBOOK) {
            //return WarpBookHandler.getWarpbookGui(player, slotID);
        } else if (ID == GuiIDs.WRITEABLE) {
            return new GuiScreenWriteable(player, slotID);
        } else if (ID == GuiIDs.LIBRARY) {
            return new GuiLibrary(player.inventory, LibraryHelper.getClientLibraryContents(), HeldHelper.getHandFromOrdinal(handOrdinal));
        } else if (ID == GuiIDs.BOOK_FORCE) {
            return GuiBook.INSTANCE;
        } else {
            ItemStack held = HeldHelper.getStackFromOrdinal(player, handOrdinal);
            if (held != null && held.getItem() == ECommonProxy.book) {
                IBook book = BookRegistry.INSTANCE.getBook(held);
                if (book != null) {
                    return GuiBook.INSTANCE.setBook(book, player.isSneaking());
                } else return GuiBookCreate.INSTANCE.setStack(held);
            }
        }
        return null;
    }
}