package joshie.enchiridion.wiki.data;

import joshie.enchiridion.EConfig;

import com.google.gson.annotations.Expose;

public class Data {	
    @Expose
    private String name;
    
    @Expose //Whether this can be edited or not
    private boolean canEdit = true;
    @Expose //The directory to save the data in, is only really useful when editing mods
    private String saveDir;
    
    public Data() {
    	this.saveDir = EConfig.DEFAULT_DIR;
    }

    public Data(String name) {
        this.name = name;
        this.saveDir = EConfig.DEFAULT_DIR;
    }

    public String getLocalisation() {
        return name;
    }

    public void setTranslation(String text) {
        this.name = text;
    }
    
    public boolean canEdit() {
    	return canEdit;
    }
    
    public void lock() {
    	canEdit = false;
    }
    
    public String getSaveDirectory() {
    	return saveDir;
    }
}
