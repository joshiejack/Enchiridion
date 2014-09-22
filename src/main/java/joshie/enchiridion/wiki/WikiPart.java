package joshie.enchiridion.wiki;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import joshie.enchiridion.Enchiridion;
import joshie.lib.helpers.ClientHelper;
import net.minecraft.item.ItemStack;

public class WikiPart {
    private final String key;
    private boolean isDirty;

    public WikiPart(String key) {
        this.key = key;
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
    
    public ItemStack getItemStack() {
        return getData().getStack();
    }
    
    public WikiPart setStack(ItemStack stack) {
        getData().setStack(stack);
        this.markDirty();
        return this;
    }
    
    public WikiData getData() {
        return WikiTitles.instance().getData(getUnlocalized() + "." + ClientHelper.getLang());
    }

    public void setTranslation(String text) {
        getData().setTranslation(text);
        this.markDirty();
    }

    public void markDirty() {
        this.isDirty = true;
    }

    public boolean isDirty() {
        if (this.isDirty) {
            this.isDirty = false;
            return true;
        } else return false;
    }
    
    public String getPath() {
        WikiPart part = this;
        String lang = ClientHelper.getLang();
        return Enchiridion.root + "\\wiki\\" + getKey() + "\\" + lang + ".json";
    }

    public void save() {
        try {
            //Save the tab data
            File parent = new File(getPath()).getParentFile();
            if (!parent.exists() && !parent.mkdirs()) {
                throw new IllegalStateException("Couldn't create dir: " + parent);
            }
    
            Writer writer = new OutputStreamWriter(new FileOutputStream(getPath()), "UTF-8");
            writer.write(WikiHelper.getGson().toJson(getData()));
            writer.close();
        } catch (Exception e) {}
    }
}
