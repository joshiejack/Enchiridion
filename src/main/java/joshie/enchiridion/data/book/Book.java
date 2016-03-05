package joshie.enchiridion.data.book;

import java.util.ArrayList;
import java.util.List;

import joshie.enchiridion.api.book.IBook;
import joshie.enchiridion.api.book.IPage;
import joshie.enchiridion.helpers.DefaultHelper;
import joshie.enchiridion.helpers.MCClientHelper;
import net.minecraft.util.ResourceLocation;

public class Book implements IBook {
	/** VARIABLES **/
	//Internal Information
    private String modid;
	private String uniqueName;
	private String saveName;
	
	//Display Information
	private String displayName;
	private String displayInfo;
	private String colorHex;
	private String language;
	private boolean hasCustomIcon;
    
    //Background, with default texture
	private boolean showBackground; //Whether to show any background at all
	private boolean legacyTexture; //Use the legacy texture for the books instead
	private String backgroundResource = "enchiridion:textures/books/rustic.png";
	private int backgroundStartX = -10;
	private int backgroundStartY = -10;
	private int backgroundEndX = 440;
	private int backgroundEndY = 240;
    
    //Extra Information
	private int defaultPage;
	private boolean isLocked;
	private boolean forgetPageOnClose;
    
    //Book itself
	private List<IPage> book;

    //Legacy information
	private transient int color;
	private transient boolean mc189book;
	private transient boolean showArrows;
	private transient String iconPass1;
	
	//Cached information
    private transient ResourceLocation resourceLocation;
    private transient boolean convertedColor;
    private transient List<String> information;
    
    /** CONSTRUCTOR **/
    public Book(){}
    public Book(String name, String display) {
    	this.displayName = display;
    	this.uniqueName = name;
    	this.saveName = name;
    	this.colorHex = "FFFFFFFF";
    	this.language = MCClientHelper.getLang();
    	this.hasCustomIcon = true;
    	this.showBackground = true;
    	this.book = new ArrayList();
		this.book.add(DefaultHelper.addDefaults(new Page(0)));
    }
    
    /** METHODS **/
    @Override
    public String getModID() {
        return modid;
    }
    
	@Override
	public String getUniqueName() {
		return uniqueName;
	}
	
	@Override
	public String getSaveName() {
		return saveName;
	}
	
	@Override
	public String getDisplayName() {
		return displayName;
	}
	
	@Override
	public int getColorAsInt() {
		if (!convertedColor && colorHex != null && !colorHex.equals("")) {
			try {
				convertedColor = true;
				color = (int) Long.parseLong(colorHex, 16);
			} catch (Exception e) {}
		}
		
		return color;
	}
	
	@Override
	public String getLanguageKey() {
		return language;
	}
    
	@Override
	public boolean isBackgroundVisible() {
		return showBackground;
	}
	
	@Override
	public boolean isBackgroundLegacy() {
		return legacyTexture;
	}
	
	@Override
    public ResourceLocation getBackgroundResource() {
    	if (resourceLocation == null) {
    		resourceLocation = new ResourceLocation(backgroundResource);
    	}
    	
    	return resourceLocation;
    }
	
	@Override
	public int getBackgroundStartX() {
		return backgroundStartX;
	}
	
	@Override
	public int getBackgroundStartY() {
		return backgroundStartY;
	}
	
	@Override
	public int getBackgroundEndX() {
		return backgroundEndX;
	}
	
	@Override
	public int getBackgroundEndY() {
		return backgroundEndY;
	}
	
	@Override
	public int getDefaultPage() {
		return defaultPage;
	}
	
	@Override
	public boolean isLocked() {
		return isLocked;
	}
	
	@Override
	public boolean doesBookForgetClose() {
		return forgetPageOnClose;
	}
	
	@Override
	public List<IPage> getPages() {
		return new ArrayList(book);
	}
	
	@Override
	public boolean isLegacyBook() {
		return !mc189book;
	}
	
	@Override
	public boolean wereArrowsVisible() {
		return showArrows;
	}
	
	@Override
	public String getIconPass1() {
		return iconPass1;
	}
	
	@Override
	public IBook setModID(String modidi) {
	    modid = modidi;
	    return this;
	}
	
	@Override
	public void setSaveName(String name) {
		saveName = name;
	}
	
	@Override
	public void setDisplayName(String name) {
		displayName = name;
	}
	
	@Override
	public void setColorAsInt(int color) {
		this.color = color;
		this.colorHex = Integer.toHexString(color);
	}
	
	@Override
	public void setLanguageKey(String language) {
		this.language = language;
	}
	
	@Override
	public void setBackgroundResource(String string) {
	    backgroundResource = string;
	    resourceLocation = new ResourceLocation(string);
	}
	
	@Override
	public void setLegacy() {
		mc189book = false;
		legacyTexture = true;
	}
	
	@Override
	public void setArrowVisiblity(boolean isVisible) {
		showArrows = isVisible;
	}
	
	@Override
	public void setIconPass1(String pass1) {
		iconPass1 = pass1;
	}
	
	@Override
	public void setMadeIn189() {
		mc189book = true;
	}
	
	@Override
	public void create() {
		book = new ArrayList();
	}
	
	@Override
	public void addPage(IPage page) {
		book.add(page);
	}
	
	@Override
	public void removePage(IPage page) {
	    book.remove(page);
	}
	
    @Override
    public void addInformation(List<String> tooltip) {
        if (information == null) {
            String[] split = displayInfo.split("/n");
            information = new ArrayList();
            for (String s: split) {
                information.add(s);
            }
        }
        
        tooltip.addAll(information);
    }
}
