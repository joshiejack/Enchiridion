package joshie.enchiridion.wiki;

import static java.io.File.separator;

import java.io.File;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import joshie.enchiridion.ELogger;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.helpers.GsonClientHelper;
import joshie.enchiridion.wiki.data.Data;
import joshie.enchiridion.wiki.data.DataPage;
import joshie.enchiridion.wiki.data.DataTab;
import joshie.enchiridion.wiki.data.WikiData;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public class WikiRegistry {
    private HashMap<String, WikiMod> mods = new HashMap();
    private final static WikiRegistry instance = new WikiRegistry();

    public static WikiRegistry instance() {
        return instance;
    }

    /* On creation of the registry search through the config files and add, all found entries */
    public WikiRegistry() {
        try {
            File wiki = new File(Enchiridion.root, "wiki");
            if (!wiki.exists()) wiki.mkdir();

            Collection<File> files = FileUtils.listFiles(wiki, new String[] { "json" }, true);
            for (File file : files) {
                registerConfig(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private WikiMod get(String key) {
        WikiMod mod = mods.get(key);
        if (mod != null) {
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

    /** List of all mods **/
    public Collection<WikiMod> getMods() {
        return mods.values();
    }

    public static boolean registeredDev = false;

    /** Searches all the mod files for files in the assets/wiki/ folder and registers them **/
    public void registerMods() {
        for (ModContainer mod : Loader.instance().getModList()) {
            String jar = mod.getSource().toString();
            if (jar.contains(".jar") || jar.contains(".zip")) {
                registerJar(new File(jar));
            } else if (!registeredDev) {
                registerInDev(mod.getSource());
            }
        }
    }

    private boolean isWikiContents(String name) {
        if (!name.startsWith("assets")) return false;
        if (!name.contains("wiki")) return false;
        String extension = name.substring(name.length() - 4, name.length());
        return extension.equals("json");
    }

    public String[] slashDeath(String str) {
        if (str.startsWith("/") || str.startsWith("\\")) {
            str = str.substring(1);
        }

        return str.replace("\\", ",").replace("/", ",").split(",");
    }

    public void registerInDev(File dir) {
        Collection<File> files = FileUtils.listFiles(new File(dir + separator + "assets"), new String[] { "json" }, true);
        for (File file : files) {
            try {
                String path[] = slashDeath(file.toString().replaceFirst(".*assets.*wiki", ""));
                registerData(path, FileUtils.readFileToString(file));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        registeredDev = true;
    }

    public void registerJar(File jar) {
        try {
            ZipFile zipfile = new ZipFile(jar);
            Enumeration enumeration = zipfile.entries();
            while (enumeration.hasMoreElements()) {
                ZipEntry zipentry = (ZipEntry) enumeration.nextElement();
                String fileName = zipentry.getName();
                if (isWikiContents(zipentry.getName())) {
                    String path[] = slashDeath(fileName.replaceFirst("assets.*wiki", ""));
                    registerData(path, IOUtils.toString(zipfile.getInputStream(zipentry)));
                }
            }

            zipfile.close();
        } catch (Exception e) {}
    }

    private void registerConfig(File file) {
        String[] path = file.getAbsolutePath().replace(Enchiridion.root + separator + "wiki" + separator, "").replace(separator, ",").split(",");
        try {
            registerData(path, FileUtils.readFileToString(file));
        } catch (Exception e) {
            ELogger.log(Level.ERROR, "Unable to read some dang json");
        }
    }

    private void registerData(String[] path, String data) {
        if (path.length == 5) {
            String mod = path[0];
            String tab = path[1];
            String cat = path[2];
            String key = path[3];
            String lang = path[4].replace(".json", "");
            register(mod, tab, cat, key, lang, data);
        } else {
            /** Add translations to the system via json **/
            String lang = path[path.length - 1].replace(".json", "");
            String key = path[0];
            for (int i = 1; i < path.length - 1; i++) {
                key = key + "." + path[i];
            }

            key = key + "." + lang;

            Data langData = GsonClientHelper.getGson().fromJson(data, path.length == 3 ? DataTab.class : Data.class);
            if (langData == null) langData = new Data(key);
            WikiData.instance().addData(key, langData);
        }
    }

    private void register(String mod, String tab, String cat, String key, String lang, String json) {
        DataPage contents = GsonClientHelper.getGson().fromJson(json, DataPage.class);
        if (contents == null) contents = new DataPage();
        //Load all the images in this page
        WikiPage page = getPage(mod, tab, cat, key);
        contents.cacheImages(page);
        WikiData.instance().addData(page.getUnlocalized() + "." + lang, contents);
        WikiData.instance().addPage(page);
    }
}
