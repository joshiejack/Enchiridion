package joshie.enchiridion.wiki.data;

import com.google.gson.annotations.Expose;

public class Data {
    @Expose
    private String name;
    public Data() {}

    public Data(String name) {
        this.name = name;
    }

    public String getLocalisation() {
        return name;
    }

    public void setTranslation(String text) {
        this.name = text;
    }
}
