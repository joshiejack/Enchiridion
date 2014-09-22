package joshie.enchiridion.wiki;

import java.util.HashMap;

import joshie.lib.helpers.ClientHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class WikiTitles {
    private HashMap<String, WikiData> translate = new HashMap();
    private static final WikiTitles instance = new WikiTitles();

    public static WikiTitles instance() {
        return instance;
    }

    public void addData(String key, WikiData data) {
        this.translate.put(key, data);
    }

    public void addContent(String key, WikiContents contents) {
        this.translate.put(key, contents);
    }

    public String translateToLocal(String unlocalized) {
        String ret = getData(unlocalized + "." + ClientHelper.getLang()).getLocalisation();
        if (ret == null || ret.equals(unlocalized + "." + ClientHelper.getLang())) {
            return StatCollector.translateToLocal(unlocalized);
        } else return ret;
    }

    public WikiData getData(String string) {
        WikiData data = translate.get(string);
        if (data != null) {
            return data;
        } else {
            data = new WikiData(string, new ItemStack(Blocks.stone));
            translate.put(string, data);
            return data;
        }
    }

    public WikiContents getContent(String string) {
        WikiContents data = (WikiContents) translate.get(string);
        if (data != null) {
            return data;
        } else {
            data = new WikiContents(string, new ItemStack(Blocks.stone));
            translate.put(string, data);
            return data;
        }
    }
}
