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
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;

import joshie.enchiridion.ECommonProxy;
import joshie.enchiridion.EConfig;
import joshie.enchiridion.ELogger;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.IBook;
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
                IBook data = register(GsonHelper.getModifiedGson().fromJson(json, Book.class).setModID(modid));
                data.setModID(modid);
                ELogger.log(Level.INFO, "Successfully loaded in the book with the unique identifier: " + data.getUniqueName() + " for the language: " + data.getLanguageKey());
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
                        IBook data = register(GsonHelper.getModifiedGson().fromJson(json, Book.class).setModID(modid));
                        ELogger.log(Level.INFO, "Successfully loaded in the book with the unique identifier: " + data.getUniqueName() + " for the language: " + data.getLanguageKey());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            zipfile.close();
        } catch (Exception e) {}
    }
    
    private final HashMap<String, HashMap<String, IBook>> books = new HashMap();
    private final HashMap<String, ModelResourceLocation> locations = new HashMap();
    private ModelResourceLocation dflt = new ModelResourceLocation("minecraft:book", "inventory");

    public IBook register(IBook book) {
    	if (book == null || book.getUniqueName() == null) return null;
    	if (EConfig.debugMode) {
    		ELogger.log(Level.INFO, "==== Start Logging of: " + book.getUniqueName());
    		ELogger.log(Level.INFO, "Language: " + book.getLanguageKey());
    		ELogger.log(Level.INFO, "Locked: " + book.isLocked());
    		if (book.getPages() != null) {
        		ELogger.log(Level.INFO, "Number of Pages: " + book.getPages().size());
    		}

    		ELogger.log(Level.INFO, "==== End Logging of: " + book.getUniqueName());
    	}
    	
    	//**Init everything **//
    	if (book.getPages() != null) {
    		List<IPage> pages = book.getPages();
    		for (int i = 0; i < pages.size(); i++) {
    			IPage page = pages.get(i);
    			//Add the arrow features
	    		if (book.isLegacyBook()) {
	    			if (book.wereArrowsVisible()) {
	    				DefaultHelper.addArrows(page);
	    			}
	    			
	    			page.setPageNumber(i);
	    			if (book.getDisplayName() == null || book.getDisplayName().equals("")) {
	    				book.setDisplayName(book.getUniqueName());
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
    	
    	HashMap<String, IBook> translations = getTranslations(book.getUniqueName());
    	String language = book.getLanguageKey() == null ? "en_US" : book.getLanguageKey();
    	translations.put(language, book);
    	
    	String id = book.getModID() == null || book.getModID().equals("") ? "enchiridion" : "enchiridion";
        ModelResourceLocation location = new ModelResourceLocation(new ResourceLocation(id, book.getUniqueName()), "inventory");
        ModelBakery.registerItemVariants(ECommonProxy.book, location);
        locations.put(book.getUniqueName(), location);
        
        //Now that the book has been registered, we should go and check if the it has a corresponding json for it's icon,
        //If it does not, then we should create this json ourselves using the default enchiridion icon
        //Create the icons directory if it doesn't exist
        File directory = new File(new File(Enchiridion.root, "books"), "icons");
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IllegalStateException("Couldn't create dir: " + directory);
        }
        
        
       //Create the json for books which don't have the json already
       if (book.getModID().equals(null) || book.getModID().equals("")) {
           File iconJson = new File(directory, book.getSaveName() + ".json");
           if (!iconJson.exists()) {
        	   try {
        		   BookIconTemplate template = new BookIconTemplate();
        		   template.parent = "enchiridion:item/book";
    			   template.textures = new Icons();
        		   if (book.getIconPass1() != null && !book.getIconPass1().equals("")) {
        			   template.textures.layer0 = "enchiridion:items/" + book.getIconPass1().replace(".png", "");
        		   } else template.textures.layer0 = "enchiridion:items/book";
        		   
        		   Writer writer = new OutputStreamWriter(new FileOutputStream(iconJson), "UTF-8");
                   writer.write(GsonHelper.getModifiedGson().toJson(template));;
                   writer.close();
        	   } catch (Exception e) { e.printStackTrace(); }
        	}
       }
        
        return book;
    }
    
    public HashMap<String, IBook> getTranslations(String identifier) {
    	HashMap<String, IBook> translations = books.get(identifier);
    	if (translations != null) return translations;
    	else {
    		translations = new HashMap();
    		books.put(identifier, translations);
    		return translations;
    	}
    }

    public IBook getBook(ItemStack held) {
        if (held.getItem() == null) return null;
        if (!held.hasTagCompound()) return null;
        String identifier = held.getTagCompound().getString("identifier");
        return getBookByName(identifier);
    }

    public Collection<String> getUniqueNames() {
        return books.keySet();
    }

	public IBook getBookByName(String identifier) {
		if (identifier.equals("")) return null;
        HashMap<String, IBook> translations = books.get(identifier);
        if (translations == null) return null;
        String language = ClientHelper.getLang();
        IBook translated = translations.get(language);
        if (translated != null) return translated;
        else return translations.get("en_US");
	}

	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack) {
	    IBook book = getBook(stack);
		if (book == null) return dflt;
		else return locations.get(book.getUniqueName());
	}
}
