package joshie.enchiridion;

import static java.io.File.separator;
import static joshie.enchiridion.Enchiridion.instance;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import joshie.enchiridion.api.EnchiridionHelper;
import joshie.enchiridion.designer.BookRegistry;
import joshie.enchiridion.designer.BookRegistry.BookData;
import joshie.enchiridion.designer.DesignerCanvas;
import joshie.enchiridion.designer.ItemBook;
import joshie.enchiridion.designer.features.FeatureText;
import joshie.enchiridion.library.LibraryRegistry;
import joshie.enchiridion.library.ModBooks;
import joshie.enchiridion.library.handlers.BotaniaBookHandler;
import joshie.enchiridion.wiki.WikiHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class ECommonProxy {
    public static Item book;
    
    public void preInit() {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new EGuiHandler());
        EnchiridionHelper.bookRegistry = LibraryRegistry.INSTANCE;
        
        if(EConfig.ENABLE_BOOKS) {
            book = new ItemBook().setCreativeTab(CreativeTabs.tabMisc).setHasSubtypes(true).setUnlocalizedName("book");
            GameRegistry.registerItem(book, "book");
            
            //Load in all the books
            BookRegistry.init();

            //Create a copy of the book
            //Save the tab data
            if(EConfig.GEN_EXAMPLE_BOOK) {
                try {
                    File example = new File(Enchiridion.root + separator + "books", "enchiridion_introbook.json");
                    if(!example.exists()) {
                        //Register a dummy book
                        BookData data = new BookData("enchiridion.introbook", "Introduction Book", null, 0xFFFFFF);
                        DesignerCanvas page = new DesignerCanvas();
                        page.features.add(new FeatureText());
                        data.book.add(page);
                        BookRegistry.register(data);
                        //Write the json to file
                        
                        Writer writer = new OutputStreamWriter(new FileOutputStream(example), "UTF-8");
                        writer.write(WikiHelper.getGson().toJson(BookRegistry.getData("enchiridion.introbook")));
                        writer.close();
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
    
    public void init() {
        LibraryRegistry.INSTANCE.initRegistry();
        ModBooks.init();
        
        if(Loader.isModLoaded("Botania")) {
            MinecraftForge.EVENT_BUS.register(new BotaniaBookHandler());
        }
    }
    
    public void postInit() {
        LibraryRegistry.INSTANCE.load();
        setupClient();
    }

    public void setupClient() {}
}
