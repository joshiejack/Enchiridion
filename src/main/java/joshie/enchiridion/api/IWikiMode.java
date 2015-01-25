package joshie.enchiridion.api;

import java.util.List;

public interface IWikiMode {
	public List addButtons(List list);
	
	/** Called whenever this mode is switched to **/
    public void onSwitch();
    
    /** The type of mode this is **/
    public WikiMode getType();
    
    public static enum WikiMode {
    	DISPLAY, EDIT_GENERAL, EDIT_LINK;
    }
}
