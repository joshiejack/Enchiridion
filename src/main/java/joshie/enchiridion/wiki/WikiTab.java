package joshie.enchiridion.wiki;

import java.util.Collection;
import java.util.HashMap;

import net.minecraft.util.StatCollector;

public class WikiTab {
    private HashMap<String, WikiCategory> categories = new HashMap();
    private final String key;

    public WikiTab (String key) {
        this.key = key;
    }
    
    private WikiMod mod;
    public WikiTab setMod(WikiMod mod) {
        this.mod = mod;
        return this;
    }

    public WikiCategory get(String key) {
        WikiCategory cat = categories.get(key);
        if(cat != null) {
            return cat;
        } else {
            cat = new WikiCategory(key).setTab(this);
            categories.put(key, cat);
            return cat;
        }
    }

    public String getKey() {
        return key;
    }
    
    public String getUnlocalized() {
        return mod.getUnlocalized() + "." + getKey();
    }

    public String getTitle() {
        return WikiTitles.instance().translateToLocal(getUnlocalized());
    }

    public WikiMod getMod() {
        return mod;
    }
    
    public Collection<WikiCategory> getCategories() {
        return categories.values();
    }
}
