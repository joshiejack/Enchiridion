package joshie.enchiridion.api.book;

import net.minecraft.client.resources.Language;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.Collection;
import java.util.List;

public interface IBook {
    //GETTERS
    /** Return the modid, returns enchiridion if config book **/
    String getModID();

    /** Return the unique identifier for this book,
     *  Is used to determine which book this is
     *  Used to grab language variants.
     *  Doesn't need to be unique for language variants **/
    String getUniqueName();

    /** Return the save name for this book
     *  Should be unique for every single book **/
    String getSaveName();

    /** Return the display name for this book **/
    ITextComponent getDisplayName();

    /** Return the hex color for this book **/
    int getColorAsInt();

    /** Return the language key for this book,
     *  for example en_US **/
    Language getLanguageKey();

    /** Whether or not this book displays it's background **/
    boolean isBackgroundVisible();

    /** Whether or not we should render the old texture **/
    boolean isBackgroundLegacy();

    /** The resource location of the background **/
    ResourceLocation getBackgroundResource();

    /** The start position for the background on the x axis **/
    int getBackgroundStartX();

    /** The start position for the background on the y axis **/
    int getBackgroundStartY();

    /** The end position for the background on the x axis **/
    int getBackgroundEndX();

    /** The end position for the background on the y axis **/
    int getBackgroundEndY();

    /** Returns the default page number **/
    int getDefaultPage();

    /** Whether this book is editable or not **/
    boolean isLocked();

    /** Returns true if this book doesn't remember the last page
     *  you were using when you close it. (Resets itself to the default) */
    boolean doesBookForgetClose();

    /** Returns a list of all the pages in the book
     *  Adding to this won't work, use {@link #addPage(IPage)} **/
    List<IPage> getPages();

    /** Returns true if this book was originally made in 1.7.10 **/
    boolean isLegacyBook();

    /** Returns true if the arrows were visible before in 1.7.10 **/
    boolean wereArrowsVisible();

    /** Returns whatever iconPass1 was set to in 1.7.10 **/
    String getIconPass1();

    //SETTERS
    /** Internal use only **/
    IBook setModID(String modid);

    /** Set the save name for this book **/
    void setSaveName(String name);

    /** Set the display name for this book **/
    void setDisplayName(ITextComponent name);

    /** Adds tooltip info **/
    void addInformation(List<ITextComponent> tooltip);

    /** Set the color of this book **/
    void setColorAsInt(int color);

    /** Set the language key for this book **/
    void setLanguageKey(Language language);

    /** Sets the books background **/
    void setBackgroundResource(String string);

    //Legacy Helpers
    /** Marks this as a legacy book **/
    void setLegacy();

    /** Sets arrow visibility **/
    void setArrowVisibility(boolean isVisible);

    /** Sets icon pass 1 **/
    void setIconPass1(String name);

    /** Save this book in the new format **/
    void setMadeIn189();

    /** Creates a new array list for the books page **/
    void create();

    /** Adds a new page **/
    void addPage(IPage page);

    /** Removes an existing page **/
    void removePage(IPage page);

    /** Returns a list of default ids **/
    List<String> getDefaultFeatures();

    /** Call to set the default features for this book **/
    void setDefaultFeatures(Collection<String> features);
}