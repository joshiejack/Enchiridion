package joshie.enchiridion.wiki;

import java.util.Collection;
import java.util.HashMap;

import joshie.enchiridion.Enchiridion;
import joshie.lib.helpers.ClientHelper;

public class WikiTab extends WikiPart {
    private HashMap<String, WikiCategory> categories = new HashMap();

    public WikiTab(String key) {
        super(key);
    }

    public WikiCategory get(String key) {
        WikiCategory cat = categories.get(key);
        if (cat != null) {
            return cat;
        } else {
            cat = new WikiCategory(key).setTab(this);
            categories.put(key, cat);
            return cat;
        }
    }

    public Collection<WikiCategory> getCategories() {
        return categories.values();
    }

    private WikiMod mod;

    public WikiTab setMod(WikiMod mod) {
        this.mod = mod;
        return this;
    }

    public WikiMod getMod() {
        return mod;
    }

    @Override
    public String getUnlocalized() {
        return mod.getUnlocalized() + "." + getKey();
    }

    @Override
    public String getPath() {
        WikiMod mod = getMod();
        WikiTab tab = this;
        String lang = ClientHelper.getLang();
        return Enchiridion.root + "\\wiki\\" + mod.getKey() + "\\" + tab.getKey() + "\\" + lang + ".json";
    }
    
    @Override
    public void markDirty() {
        super.markDirty();
        mod.markDirty();
    }
}
