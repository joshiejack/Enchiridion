package joshie.enchiridion.books;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;

import joshie.enchiridion.api.IPage;
import joshie.enchiridion.helpers.DefaultHelper;
import joshie.lib.helpers.ClientHelper;
import net.minecraft.util.ResourceLocation;

public class Book {
	//Internal Information
	public String uniqueName;
	public String saveName;
	
	//Display Information
    public String displayName;
    public String colorHex;
    public String language;
    public boolean hasCustomIcon;
    
    //Background, with default texture
    public boolean showBackground; //Whether to show any background at all
    public boolean legacyTexture; //Use the legacy texture for the books instead
	public String backgroundResource = "enchiridion:textures/books/rustic.png";
	public int backgroundStartX = -10;
	public int backgroundStartY = -10;
	public int backgroundEndX = 440;
	public int backgroundEndY = 240;
    
    //Extra Information
    public int defaultPage;
    public boolean isLocked;
    public boolean forgetPageOnClose;
    
    //Book itself
    public List<IPage> book;

    //Legacy information
	public transient int color;
	public transient boolean mc189book;
	public transient boolean showArrows;
	public transient String iconPass1;
	
    
    public Book(){}
    public Book(String name, String display) {
    	this.displayName = display;
    	this.uniqueName = name;
    	this.saveName = name;
    	this.colorHex = "FFFFFFFF";
    	this.language = ClientHelper.getLang();
    	this.hasCustomIcon = true;
    	this.showBackground = true;
    	this.book = new ArrayList();
		this.book.add(DefaultHelper.addDefaults(new Page(0)));
    }
    
    private transient ResourceLocation resourceLocation;
    public ResourceLocation getResource() {
    	if (resourceLocation == null) {
    		resourceLocation = new ResourceLocation(backgroundResource);
    	}
    	
    	return resourceLocation;
    }
}
