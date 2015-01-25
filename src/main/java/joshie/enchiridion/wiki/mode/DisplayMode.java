package joshie.enchiridion.wiki.mode;

import static joshie.enchiridion.wiki.gui.GuiMain.button_id;

import java.util.List;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.api.IWikiMode;
import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.gui.buttons.ButtonAddPage;

public class DisplayMode implements IWikiMode {
	/** This is a generic mode, where you can't do anything except for view **/
    private static final DisplayMode instance = new DisplayMode();
    public static DisplayMode getInstance() {
        return instance;
    }
    
    @Override
    public List addButtons(List list) {
        if(EConfig.EDIT_ENABLED && !WikiHelper.isLibrary()) {
            list.add(new ButtonAddPage(button_id++, 723, 14, 1, 1, 2F));
        }
        
        return list;
    }

    @Override
    public void onSwitch() {
        return;
    }

	@Override
	public WikiMode getType() {
		return WikiMode.DISPLAY;
	}
}
