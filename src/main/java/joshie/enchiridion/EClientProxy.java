package joshie.enchiridion;

import static java.io.File.separator;
import static joshie.enchiridion.Enchiridion.instance;
import static joshie.enchiridion.wiki.WikiHandler.wiki;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import joshie.enchiridion.designer.BookIconPatcher;
import joshie.enchiridion.designer.BookRegistry;
import joshie.enchiridion.designer.BookRegistry.BookData;
import joshie.enchiridion.designer.DesignerCanvas;
import joshie.enchiridion.designer.features.FeatureText;
import joshie.enchiridion.helpers.ClientHelper;
import joshie.enchiridion.helpers.GsonClientHelper;
import joshie.enchiridion.helpers.ItemHelper;
import joshie.enchiridion.library.BookHandlerRegistry;
import joshie.enchiridion.library.mods.BotaniaClient;
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
import cpw.mods.fml.common.network.NetworkRegistry;

public class EClientProxy extends ECommonProxy {
    public static WikiFont font;

    @Override
    public void preClient() {
        /** Register the GuiHandler clientSide only, no need for a server side gui **/
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new EGuiHandler());

        /** If we have the books enabled let's load them all up clientside only **/
        if (EConfig.ENABLE_BOOKS) {
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

            MinecraftForge.EVENT_BUS.register(new BookIconPatcher());
        }

        /** Init the LibraryRegistry**/
        BookHandlerRegistry.initRegistry();
    }

    @Override
    public void initClient() {
        if (Loader.isModLoaded("Botania")) {
            BotaniaClient.INSTANCE.init();
        }

        /** Let's initialise the wiki and search through it **/
        //Search through all the mods for relevant pages
        WikiRegistry.instance().registerMods();
    }

    @Override
    public void postClient() {
        MinecraftForge.EVENT_BUS.register(new WikiHandler());
        FMLCommonHandler.instance().bus().register(new WikiHandler());
        wiki = new KeyBinding("enchiridion.key.wiki", Keyboard.KEY_H, "key.categories.misc");
        ClientRegistry.registerKeyBinding(wiki);
        Minecraft mc = ClientHelper.getMinecraft();
        font = new WikiFont(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.renderEngine, false);
        if (mc.getLanguageManager() != null) {
            font.setUnicodeFlag(mc.func_152349_b());
            font.setBidiFlag(mc.getLanguageManager().isCurrentLanguageBidirectional());
        }

        ((IReloadableResourceManager) mc.getResourceManager()).registerReloadListener(font);

        /** Initialise the item helper **/
        ItemHelper.init();
    }
}
