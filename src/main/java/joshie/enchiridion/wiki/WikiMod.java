package joshie.enchiridion.wiki;

import java.util.Collection;
import java.util.HashMap;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.helpers.ClientHelper;

public class WikiMod extends WikiPart {
    private HashMap<String, WikiTab> tabs = new HashMap();
    
    public WikiMod(String key) {
        super(key);
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

    public Collection<WikiTab> getTabs() {
        return tabs.values();
    }
    
    @Override
    public String getPath() {
    	WikiMod mod = this;
        String lang = ClientHelper.getLang();
        
        String dir = getData().getSaveDirectory();
        if(dir.equals("")) {
        	return Enchiridion.root + "\\wiki\\" + mod.getKey()  + "\\" + lang + ".json";
        } else {
        	String root = Enchiridion.root.getParentFile().getParentFile().getParentFile().toString();
        	return root + "\\src\\main\\resources\\assets\\" + dir + "\\wiki\\" + mod.getKey() + "\\" + lang + ".json";
        }
    }
}
