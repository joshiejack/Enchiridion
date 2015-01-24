package joshie.enchiridion.wiki.mode.edit;

import static joshie.enchiridion.lib.ETranslate.translate;
import static joshie.enchiridion.wiki.WikiHelper.drawScaledTexture;
import static joshie.enchiridion.wiki.WikiHelper.getIntFromMouse;
import static joshie.enchiridion.wiki.gui.GuiMain.texture;
import static joshie.lib.helpers.OpenGLHelper.end;
import static joshie.lib.helpers.OpenGLHelper.fixColors;
import static joshie.lib.helpers.OpenGLHelper.resetZ;
import static joshie.lib.helpers.OpenGLHelper.start;

import java.io.File;

import joshie.enchiridion.lib.ETranslate;
import joshie.enchiridion.wiki.WikiCategory;
import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.WikiMod;
import joshie.enchiridion.wiki.WikiRegistry;
import joshie.enchiridion.wiki.WikiTab;
import joshie.enchiridion.wiki.gui.GuiExtension;

public class GuiConfirmDeletion extends GuiExtension {	
	public GuiConfirmDeletion() {
		setVisibility(false);
	}
	
	@Override
	public void draw() {
		start();
		resetZ();
		
		drawRect(450, 150, 850, 310, 0xEE1B2C43);
		drawRect(450, 310, 850, 313, 0xFFC2C29C);
		drawRect(450, 147, 850, 150, 0xFFC2C29C);
		drawRect(447, 147, 450, 313, 0xFFC2C29C);
		drawRect(850, 147, 853, 313, 0xFFC2C29C);
		verticalGradient(450, 150, 850, 165, 0xFF09111E, 0xFF1B2C43);
        verticalGradient(450, 165, 850, 180, 0xFF1B2C43, 0xFF09111E);
        drawRect(450, 180, 850, 183, 0xFFC2C29C);
		
		drawScaledCentredText(1.75F, "[b]" + translate("confirm.title", getPage().getTitle()) + "[/b]", 630, 160, 0xFFFFFF);
		drawScaledSplitText(2F, "" + translate("confirm.description") + "", 463, 200, 0xFFFFFF, 189);
		
		fixColors();
		//Highlighting the buttons
		int xYes = 0, xNo = 0;
		if (getIntFromMouse(500, 624, 260, 299, 0, 1) == 1) {
            xYes = 130;
        }
		
		if (getIntFromMouse(660, 784, 260, 299, 0, 1) == 1) {
            xNo = 130;
        }
		
		
		drawScaledTexture(texture, 500, 260, xYes, 104, 124, 39, 1F);
		drawScaledTexture(texture, 660, 260, xNo, 104, 124, 39, 1F);
        drawScaledCentredText(2F, "[b]" + ETranslate.translate("yes") + "[/b]", 560, 272, 0xFFFFFF);
        drawScaledCentredText(2F, "[b]" + ETranslate.translate("no") + "[/b]", 720, 272, 0xFFFFFF);
        end();
	}
	
	@Override
    public void clicked(int button) {
		if (getIntFromMouse(500, 624, 260, 299, 0, 1) == 1) {
            confirm();
            cancel();
        }
		
		if (getIntFromMouse(650, 774, 260, 299, 0, 1) == 1) {
            cancel();
        }
	}
	
	private void cancel() {
		WikiHelper.setVisibility(getClass(), false);
	}
	
	private void confirm() {
		//Capture the category before deletion
        WikiCategory category = WikiHelper.getPage().getCategory();
        //Deletes the file
        removeDirectory(WikiHelper.getPage().getPath());
        WikiHelper.delete();
        
        //Now that we have deleted the page
        //We should check to see if the category it was part of has anything available
        if(category.getPages().size() <= 0) {
            //If there are no pages, then delete this category from existence, and collect the tab
            WikiTab tab = category.getTab();
            tab.getCategories().remove(category);
            removeDirectory(category.getPath());
            //After removing that category, if there are no more categories, let's remove the tab
            if(tab.getCategories().size() <= 0) {
                WikiMod mod = tab.getMod();
                mod.getTabs().remove(tab);
                removeDirectory(tab.getPath());
                //Now that we have deleted a tab, let's check if the mod has anymore tabs
                if(mod.getTabs().size() <= 0) {
                    //If it has no more tabs, delete the mod also
                    WikiRegistry.instance().getMods().remove(mod);
                    removeDirectory(mod.getPath());
                }
            }
        }
	}
	
	private void removeDirectory(String path) {
        File directory = new File(path).getParentFile();
        String[] files = directory.getParentFile().list();
        if (files != null) {
            for (String s : files) {
                File currentFile = new File(directory.getPath(), s);
                currentFile.delete();
            }
        }

        directory.delete();
    }
}
