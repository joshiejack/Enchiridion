package joshie.enchiridion.wiki;

import java.util.Collection;
import java.util.HashMap;

import net.minecraft.util.StatCollector;

public class WikiCategory {
    private HashMap<String, WikiPage> pages = new HashMap();
    private final String key;
    private boolean isHidden;
    
    public WikiCategory (String key) {
        this.key = key;
    }
    
    private WikiTab tab;
    public WikiCategory setTab(WikiTab tab) {
        this.tab = tab;
        return this;
    }
    
    public WikiPage get(String key) {
        WikiPage page = pages.get(key);
        if(page != null) {
            return page;
        } else {
            page = new WikiPage(key).setCategory(this);
            pages.put(key, page);
            return page;
        }
    }
    
    public String getKey() {
        return key;
    }

    public WikiTab getTab() {
        return tab;
    }
    
    public Collection<WikiPage> getPages() {
        return pages.values();
    }

    public String getUnlocalized() {
        return tab.getUnlocalized() + "." + getKey();
    }

    public String getTitle() {
        return WikiTitles.instance().translateToLocal(getUnlocalized());
    }

    /** Whether this category is currently hidden or not **/
    public boolean isVisible() {
        return !isHidden;
    }
    
    /** Sets this category to be visible */
    public void setVisible() {
        isHidden = false;
    }
    
    /** Sets this category to be hidden */
    public void setHidden() {
        isHidden = true;
    }
}
