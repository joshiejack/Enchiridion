package joshie.enchiridion.wiki;

import java.util.HashMap;

import net.minecraft.util.StatCollector;
import joshie.lib.helpers.ClientHelper;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;

public class WikiTitles {
    @Expose
    private HashMap<String, String> translate = new HashMap();
    private static final WikiTitles instance = new WikiTitles();

    public static WikiTitles instance() {
        return instance;
    }
    
    public void addLocalization(String key, String translated) {
        this.translate.put(key, translated);
    }

    public String translateToLocal(String unlocalized) {
        unlocalized = unlocalized.toLowerCase();
        String ret = translate.get(unlocalized + "." + ClientHelper.getLang());
        if(ret == null || ret.equals(unlocalized + "." + ClientHelper.getLang())) {
            return StatCollector.translateToLocal(unlocalized);
        } else return ret;
    }
}
