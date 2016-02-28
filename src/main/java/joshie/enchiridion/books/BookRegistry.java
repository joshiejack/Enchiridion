package joshie.enchiridion.books;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;

import joshie.enchiridion.ECommonProxy;
import joshie.enchiridion.EConfig;
import joshie.enchiridion.ELogger;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.IFeatureProvider;
import joshie.enchiridion.api.IPage;
import joshie.enchiridion.helpers.DefaultHelper;
import joshie.enchiridion.helpers.FileHelper;
import joshie.enchiridion.helpers.GsonHelper;
import joshie.enchiridion.json.BookIconTemplate;
import joshie.enchiridion.json.BookIconTemplate.Icons;
import joshie.lib.helpers.ClientHelper;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class BookRegistry implements ItemMeshDefinition {
	public static final BookRegistry INSTANCE = new BookRegistry();
	
	private BookRegistry() {}
	
    public void loadBooksFromConfig() {   	
        //If the book directory doesn't exist create it
        File directory = new File(Enchiridion.root, "books");
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IllegalStateException("Couldn't create dir: " + directory);
        }

        Collection<File> files = FileUtils.listFiles(directory, new String[] { "json" }, false);
        for (File file : files) { //Grab a list of all the json files in the directory
            //Read all the json books from this directory
            try {
                register(GsonHelper.getModifiedGson().fromJson(FileUtils.readFileToString(file), Book.class));
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
    
    public void registerModInDev(String modid, File source) {
        File path = FileHelper.getDevAssetsForModPath(source.getParentFile(), modid, "books");
        if (!path.exists()) {
            path.mkdir();
        }
        
        Collection<File> files = FileUtils.listFiles(path, new String[] { "json" }, true);
        for (File file : files) {
            try {
                String json = FileUtils.readFileToString(file);
                Book data = BookRegistry.INSTANCE.register(GsonHelper.getModifiedGson().fromJson(json, Book.class));
                ELogger.log(Level.INFO, "Successfully loaded in the book with the unique identifier: " + data.uniqueName + " for the language: " + data.language);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void registerModInJar(String modid, File jar) {
        try {
            ZipFile zipfile = new ZipFile(jar);
            Enumeration enumeration = zipfile.entries();
            while (enumeration.hasMoreElements()) {
                ZipEntry zipentry = (ZipEntry) enumeration.nextElement();
                String fileName = zipentry.getName();
                Path path1 = Paths.get(fileName);
                Path path2 = Paths.get("assets", modid, "books");

                if (path1.startsWith(path2)) {
                    try {
                        String json = IOUtils.toString(zipfile.getInputStream(zipentry));
                        Book data = register(GsonHelper.getModifiedGson().fromJson(json, Book.class));
                        ELogger.log(Level.INFO, "Successfully loaded in the book with the unique identifier: " + data.uniqueName + " for the language: " + data.language);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            zipfile.close();
        } catch (Exception e) {}
    }
    
    private final HashMap<String, HashMap<String, Book>> books = new HashMap();
    private final HashMap<String, ModelResourceLocation> locations = new HashMap();
    private ModelResourceLocation dflt = new ModelResourceLocation("minecraft:book", "inventory");

    public Book register(Book book) {
    	if (book == null || book.uniqueName == null) return null;
    	if (EConfig.debugMode) {
    		ELogger.log(Level.INFO, "==== Start Logging of: " + book.uniqueName);
    		ELogger.log(Level.INFO, "Language: " + book.language);
    		ELogger.log(Level.INFO, "Locked: " + book.isLocked);
    		if (book.book != null) {
        		ELogger.log(Level.INFO, "Number of Pages: " + book.book.size());
    		}

    		ELogger.log(Level.INFO, "==== End Logging of: " + book.uniqueName);
    	}
    	
    	//**Init everything **//
    	if (book.book != null) {
    		for (int i = 0; i < book.book.size(); i++) {
    			IPage page = book.book.get(i);
    			//Add the arrow features
	    		if (!book.mc189book) {
	    			if (book.showArrows) {
	    				DefaultHelper.addArrows(page);
	    			}
	    			
	    			page.setPageNumber(i);
	    			if (book.displayName == null || book.displayName.equals("")) {
	    				book.displayName = book.uniqueName;
	    			}
	    		}
	    		
	    		//Initialise everything
	    		for (IFeatureProvider feature: page.getFeatures()) {
	    			feature.getFeature().update(feature);
	    		}
	    		
	    		//Sort the pages
	    		page.sort();
    		}
    	}
    	
    	HashMap<String, Book> translations = getTranslations(book.uniqueName);
    	String language = book.language == null ? "en_US" : book.language;
    	translations.put(language, book);
    	
        ModelResourceLocation location = new ModelResourceLocation(new ResourceLocation("enchiridion", book.uniqueName), "inventory");
        ModelBakery.registerItemVariants(ECommonProxy.book, location);
        locations.put(book.uniqueName, location);
        
        //Now that the book has been registered, we should go and check if the it has a corresponding json for it's icon,
        //If it does not, then we should create this json ourselves using the default enchiridion icon
        //Create the icons directory if it doesn't exist
        File directory = new File(new File(Enchiridion.root, "books"), "icons");
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IllegalStateException("Couldn't create dir: " + directory);
        }
        
        
       //Create the json for books which don't have the json already
       File iconJson = new File(directory, book.saveName + ".json");
       if (!iconJson.exists()) {
    	   try {
    		   BookIconTemplate template = new BookIconTemplate();
    		   template.parent = "enchiridion:item/book";
			   template.textures = new Icons();
    		   if (book.iconPass1 != null && !book.iconPass1.equals("")) {
    			   template.textures.layer0 = "enchiridion:items/" + book.iconPass1.replace(".png", "");
    		   } else template.textures.layer0 = "enchiridion:items/book";
    		   
    		   Writer writer = new OutputStreamWriter(new FileOutputStream(iconJson), "UTF-8");
               writer.write(GsonHelper.getModifiedGson().toJson(template));;
               writer.close();
    	   } catch (Exception e) { e.printStackTrace(); }
    	}
        
        return book;
    }
    
    public HashMap<String, Book> getTranslations(String identifier) {
    	HashMap<String, Book> translations = books.get(identifier);
    	if (translations != null) return translations;
    	else {
    		translations = new HashMap();
    		books.put(identifier, translations);
    		return translations;
    	}
    }

    public Book getBook(ItemStack held) {
        if (held.getItem() == null) return null;
        if (!held.hasTagCompound()) return null;
        String identifier = held.getTagCompound().getString("identifier");
        return getBookByName(identifier);
    }

    public Collection<String> getUniqueNames() {
        return books.keySet();
    }

	public Book getBookByName(String identifier) {
		if (identifier.equals("")) return null;
        HashMap<String, Book> translations = books.get(identifier);
        if (translations == null) return null;
        String language = ClientHelper.getLang();
        Book translated = translations.get(language);
        if (translated != null) return translated;
        else return translations.get("en_US");
	}

	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack) {
		Book book = getBook(stack);
		if (book == null) return dflt;
		else return locations.get(book.uniqueName);
	}
}
