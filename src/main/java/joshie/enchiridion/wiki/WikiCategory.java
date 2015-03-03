package joshie.enchiridion.wiki;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.helpers.ClientHelper;
import joshie.enchiridion.wiki.data.WikiData;

public class WikiCategory extends WikiPart {
    private HashMap<String, WikiPage> pages = new HashMap();
    private boolean isHidden;

    public WikiCategory(String key) {
        super(key);
    }

    public WikiPage get(String key) {
        WikiPage page = pages.get(key);
        if (page != null) {
            return page;
        } else {
            page = new WikiPage(key).setCategory(this);
            pages.put(key, page);
            WikiData.instance().addPage(page);
            return page;
        }
    }

    public Collection<WikiPage> getPages() {
        return pages.values();
    }

    private WikiTab tab;

    public WikiCategory setTab(WikiTab tab) {
        this.tab = tab;
        return this;
    }

    public WikiTab getTab() {
        return tab;
    }

    @Override
    public String getUnlocalized() {
        return tab.getUnlocalized() + "." + getKey();
    }

    @Override
    public String getPath() {
        WikiMod mod = getTab().getMod();
        WikiTab tab = getTab();
        WikiCategory cat = this;
        String lang = ClientHelper.getLang();

        String dir = getData().getSaveDirectory();
        if (dir.equals("")) {
            return Enchiridion.root + File.separator + "wiki" + File.separator + mod.getKey() + File.separator + tab.getKey() + File.separator + cat.getKey() + File.separator + lang + ".json";
        } else {
            String root = Enchiridion.root.getParentFile().getParentFile().getParentFile().toString();
            return root + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "assets" + File.separator + dir + File.separator + "wiki" + File.separator + mod.getKey() + File.separator + tab.getKey()+ File.separator + cat.getKey() + File.separator + lang + ".json";
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();
        tab.markDirty();
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
