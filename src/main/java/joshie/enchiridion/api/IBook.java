package joshie.enchiridion.api;

import java.util.List;

import net.minecraft.util.ResourceLocation;

public interface IBook {
	/** GETTERS **/
	/** Return the unique identifier for this book,
	 *  Is used to determine which book this is
	 *  Used to grab language variants.
	 *  Doesn't need to be unique for language variants **/
	public String getUniqueName();

	/** Return the save name for this book
	 *  Should be unique for every single book **/
	public String getSaveName();
	
	/** Return the display name for this book **/
	public String getDisplayName();
	
	/** Return the hex color for this book **/
	public int getColorAsInt();
	
	/** Return the language key for this book,
	 *  for example en_US **/
	public String getLanguageKey();
	
	/** Whether or not this book displays it's background **/
	public boolean isBackgroundVisible();
	
	/** Whether or not we should render the old texture **/
	public boolean isBackgroundLegacy();
	
	/** The resource location of the background **/
	public ResourceLocation getResource();
	
	/** The start position for the background on the x axis **/
	public int getBackgroundStartX();
	
	/** The start position for the background on the y axis **/
	public int getBackgroundStartY();
	
	/** The end position for the background on the x axis **/
	public int getBackgroundEndX();
	
	/** The end position for the background on the y axis **/
	public int getBackgroundEndY();
	
	/** Returns the default page number **/
	public int getDefaultPage();
	
	/** Whether this book is editable or not **/
	public boolean isLocked();
	
	/** Returns true if this book doesn't remember the last page
	 *  you were using when you close it. (Resets itself to the default) */
	public boolean doesBookForgetClose();

	/** Returns a list of all the pages in the book
	 *  Adding to this won't work, use {@link #addPage(IPage)} **/
	public List<IPage> getPages();
	
	/** Returns true if this book was originally made in 1.7.10 **/
	public boolean isLegacyBook();
	
	/** Returns true if the arrows were visible before in 1.7.10 **/
	public boolean wereArrowsVisible();
	
	/** Returns whatever iconPass1 was set to in 1.7.10 **/
	public String getIconPass1();
	
	/** SETTERS **/
	/** Set the save name for this book **/
	public void setSaveName(String name);
	
	/** Set the display name for this book **/
	public void setDisplayName(String name);
	
	/** Set the color of this book **/
	public void setColorAsInt(int color);
	
	/** Set the language key for this book **/
	public void setLanguageKey(String language);
	
	/** Legacy Helpers **/
	/** Marks this as a legacy book **/
	public void setLegacy();
	
	/** Sets arrow visiblity **/
	public void setArrowVisiblity(boolean isVisible);
	
	/** Sets iconpass1 **/
	public void setIconPass1(String name);
	
	/** Save this book in the new format **/
	public void setMadeIn189();
	
	/** Creates a new arraylist for the books page **/
	public void create();
	
	/** Adds a new page **/
	public void addPage(IPage page);
}
