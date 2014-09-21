package joshie.enchiridion.wiki;

import java.util.Collection;
import java.util.HashMap;

public class WikiMod {
    private HashMap<String, WikiTab> tabs = new HashMap();
    private final String key;

    public WikiMod(String key) {
        this.key = key;
    }

    public WikiTab get(String key) {
        WikiTab tab = tabs.get(key);
        if(tab != null) {
            return tab;
        } else {
            tab = new WikiTab(key).setMod(this);
            tabs.put(key, tab);
            return tab;
        }
    }

    public String getKey() {
        return key;
    }

    public String getUnlocalized() {
        return getKey();
    }
    
    public String getTitle() {
        return WikiTitles.instance().translateToLocal(getUnlocalized());
    }

    public Collection<WikiTab> getTabs() {
        return tabs.values();
    }
}
