package joshie.enchiridion.wiki.mode.edit;

import java.io.File;

import joshie.enchiridion.wiki.WikiCategory;
import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.WikiMod;
import joshie.enchiridion.wiki.WikiRegistry;
import joshie.enchiridion.wiki.WikiTab;
import joshie.enchiridion.wiki.mode.ButtonBase;
import net.minecraft.client.Minecraft;

public class ButtonDeletePage extends ButtonBase {
    public ButtonDeletePage(int id, int x, int y, int width, int height, float scale) {
        super(id, x, y, width, height, "delete", 2F);
    }

    @Override
    public void onClicked() {
        //Capture the category before deletion
        WikiCategory category = WikiHelper.getPage().getCategory();
        //Deletes the file
        deleteDir(WikiHelper.getPage().getPath());
        WikiHelper.delete();
        
        //Now that we have deleted the page
        //We should check to see if the category it was part of has anything available
        if(category.getPages().size() <= 0) {
            //If there are no pages, then delete this category from existence, and collect the tab
            WikiTab tab = category.getTab();
            tab.getCategories().remove(category);
            deleteDir(category.getPath());
            //After removing that category, if there are no more categories, let's remove the tab
            if(tab.getCategories().size() <= 0) {
                WikiMod mod = tab.getMod();
                mod.getTabs().remove(tab);
                deleteDir(tab.getPath());
                //Now that we have deleted a tab, let's check if the mod has anymore tabs
                if(mod.getTabs().size() <= 0) {
                    //If it has no more tabs, delete the mod also
                    WikiRegistry.instance().getMods().remove(mod);
                    deleteDir(mod.getPath());
                }
            }
        }
    }
    
    public void deleteDir(String path) {
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

    @Override
    public void drawButton(Minecraft mc, int x, int y) {
        super.drawButton(mc, x, y);
    }
}
