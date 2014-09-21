package joshie.enchiridion.wiki;

import static java.io.File.separator;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

import joshie.enchiridion.Enchiridion;

import org.apache.commons.io.FileUtils;

public class WikiRegistry {
    private HashMap<String, WikiMod> mods = new HashMap();
    private final static WikiRegistry instance = new WikiRegistry();
    
    public static WikiRegistry instance() {
        return instance;
    }
    
    /* On creation of the registry search through the config files and add, all found entries */
    public WikiRegistry() {
        try {
            Collection<File> files = FileUtils.listFiles(new File(Enchiridion.root + separator + "wiki"), new String[] { "json" }, true);
            for (File file : files) {
                register(file);
            }
        } catch (Exception e) {e.printStackTrace();}
    }
    
    private WikiMod get(String key) {
        WikiMod mod = mods.get(key);
        if(mod != null) {
            return mod;
        } else {
            mod = new WikiMod(key);
            mods.put(key, mod);
            return mod;
        }
    }
    
    /** Returns the mod with this path **/
    public WikiMod getMod(String mod) {
        return get(mod);
    }
    
    /** Returns the tab with this path **/
    public WikiTab getTab(String mod, String tab) {
        return getMod(mod).get(tab);
    }
    
    /** Returns the category with this path **/
    public WikiCategory getCat(String mod, String tab, String cat) {
        return getTab(mod, tab).get(cat);
    }
    
    /** Returns the page with this page **/
    public WikiPage getPage(String mod, String tab, String cat, String key) {
        return getCat(mod, tab, cat).get(key);
    }
    
    /** Call this to register a mod, it will search the mods zip file for wiki pages **/
    public void registerMod(String modid, String rootpath) {
        //Do nothing
    }
    
    private void register(File file) {
        String[] path = file.getAbsolutePath().replace(Enchiridion.root + separator + "wiki" + separator, "").replace(separator, ",").split(",");        
        if(path.length == 5) {
            String mod = path[0];
            String tab = path[1];
            String cat = path[2];
            String key = path[3];
            try {
                register(mod, tab, cat, key, getLang(file), FileUtils.readFileToString(file));
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
    
    private String getLang(File file) {        
        return file.toString().substring(file.toString().length() - 10).replace(".json", "");
    }

    private void register(String mod, String tab, String cat, String key, String lang, String json) {          
        WikiContents contents = WikiHelper.getGson().fromJson(json, WikiContents.class);
        if(contents == null) contents = new WikiContents();
        getPage(mod, tab, cat, key).setContents(lang, contents); //Creates the pages
    }
}
