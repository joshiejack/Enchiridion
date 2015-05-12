package joshie.enchiridion;

import static java.io.File.separator;
import static joshie.enchiridion.Enchiridion.instance;
import static joshie.enchiridion.helpers.OpenGLHelper.fixShitForThePedia;
import static joshie.enchiridion.wiki.WikiHandler.wiki;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.designer.BookEventsHandler;
import joshie.enchiridion.designer.BookIconPatcher;
import joshie.enchiridion.designer.BookRegistry;
import joshie.enchiridion.designer.BookRegistry.BookData;
import joshie.enchiridion.designer.DesignerCanvas;
import joshie.enchiridion.designer.DrawHelper;
import joshie.enchiridion.designer.features.FeatureText;
import joshie.enchiridion.designer.recipe.RecipeHandlerFurnace;
import joshie.enchiridion.designer.recipe.RecipeHandlerShapedOre;
import joshie.enchiridion.designer.recipe.RecipeHandlerShapedVanilla;
import joshie.enchiridion.designer.recipe.RecipeHandlerShapelessOre;
import joshie.enchiridion.designer.recipe.RecipeHandlerShapelessVanilla;
import joshie.enchiridion.helpers.ClientHelper;
import joshie.enchiridion.helpers.GsonClientHelper;
import joshie.enchiridion.library.BookHandlerRegistry;
import joshie.enchiridion.library.handlers.BookObtainEvents;
import joshie.enchiridion.wiki.WikiFont;
import joshie.enchiridion.wiki.WikiHandler;
import joshie.enchiridion.wiki.WikiRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.network.NetworkRegistry;

public class EClientProxy extends ECommonProxy {
    public static WikiFont font;

    @Override
    public void preClient() {
        if (EConfig.ENABLE_WIKI) {
            if (!EConfig.SHIT_COMPUTER) {
                fixShitForThePedia();
            }

            /** Register the GuiHandler clientSide only, no need for a server side gui **/
            NetworkRegistry.INSTANCE.registerGuiHandler(instance, new EGuiHandler());
            FMLCommonHandler.instance().bus().register(new BookObtainEvents());
            MinecraftForge.EVENT_BUS.register(new BookObtainEvents());
        }

        /** If we have the books enabled let's load them all up clientside only **/
        if (EConfig.ENABLE_BOOKS) {
            EnchiridionAPI.draw = new DrawHelper();
            
            //Load in all the books
            BookRegistry.init();

            //Create a copy of the book
            //Save the tab data
            if (EConfig.GEN_EXAMPLE_BOOK) {
                try {
                    File example = new File(Enchiridion.root + separator + "books", "enchiridion_introbook.json");
                    if (!example.exists()) {
                        //Register a dummy book
                        BookData data = new BookData("enchiridion.introbook", "Introduction Book", null, 0xFFFFFF);
                        DesignerCanvas page = new DesignerCanvas();
                        page.features.add(new FeatureText());
                        data.book.add(page);
                        BookRegistry.register(data);
                        //Write the json to file

                        Writer writer = new OutputStreamWriter(new FileOutputStream(example), "UTF-8");
                        writer.write(GsonClientHelper.getGson().toJson(BookRegistry.getData("enchiridion.introbook")));
                        writer.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            MinecraftForge.EVENT_BUS.register(new BookEventsHandler());
            MinecraftForge.EVENT_BUS.register(new BookIconPatcher());
            
            EnchiridionAPI.instance.registerModBooks("Enchiridion2:enchiridion");
            EnchiridionAPI.instance.registerRecipeHandler(new RecipeHandlerShapedVanilla());
            EnchiridionAPI.instance.registerRecipeHandler(new RecipeHandlerShapedOre());
            EnchiridionAPI.instance.registerRecipeHandler(new RecipeHandlerShapelessVanilla());
            EnchiridionAPI.instance.registerRecipeHandler(new RecipeHandlerShapelessOre());
            EnchiridionAPI.instance.registerRecipeHandler(new RecipeHandlerFurnace());
        }

        if (EConfig.ENABLE_WIKI) {
            /** Init the LibraryRegistry**/
            BookHandlerRegistry.initRegistry();

            //register ench pages
            ModContainer mod = Loader.instance().activeModContainer();
            String jar = mod.getSource().toString();
            if (jar.contains(".jar") || jar.contains(".zip")) {
                WikiRegistry.instance().registerJar(new File(jar));
            } else {
                WikiRegistry.instance().registerInDev(mod.getSource());
            }
        }
    }

    @Override
    public void postClient() {
        if (EConfig.ENABLE_WIKI) {
            MinecraftForge.EVENT_BUS.register(new WikiHandler());
            FMLCommonHandler.instance().bus().register(new WikiHandler());
            wiki = new KeyBinding("enchiridion.key.wiki", Keyboard.KEY_H, "key.categories.misc");
            ClientRegistry.registerKeyBinding(wiki);
        }

        Minecraft mc = ClientHelper.getMinecraft();
        font = new WikiFont(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.renderEngine, false);
        if (mc.getLanguageManager() != null) {
            font.setUnicodeFlag(mc.func_152349_b());
            font.setBidiFlag(mc.getLanguageManager().isCurrentLanguageBidirectional());
        }

        ((IReloadableResourceManager) mc.getResourceManager()).registerReloadListener(font);
    }
}
