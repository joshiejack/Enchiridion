package joshie.enchiridion.wiki.gui.popups;

import static joshie.enchiridion.ETranslate.translate;
import static joshie.enchiridion.wiki.WikiHelper.getPage;

import java.io.File;

import joshie.enchiridion.ELogger;
import joshie.enchiridion.wiki.WikiCategory;
import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.WikiMod;
import joshie.enchiridion.wiki.WikiRegistry;
import joshie.enchiridion.wiki.WikiTab;

import org.apache.logging.log4j.Level;

public class ConfirmDeletion extends Confirm {
    public ConfirmDeletion() {
        super("delete");
    }

    @Override
    public String getDescription() {
        return translate("confirm." + descriptor + ".description", getPage().getTitle());
    }

    @Override
    public void confirm() {
        //Capture the category before deletion
        WikiCategory category = WikiHelper.getPage().getCategory();
        //Deletes the file
        removeDirectory(WikiHelper.getPage().getPath());
        WikiHelper.delete();

        //Now that we have deleted the page
        //We should check to see if the category it was part of has anything available
        if (category.getPages().size() <= 0) {
            //If there are no pages, then delete this category from existence, and collect the tab
            WikiTab tab = category.getTab();
            tab.getCategories().remove(category);
            removeDirectory(category.getPath());
            //After removing that category, if there are no more categories, let's remove the tab
            if (tab.getCategories().size() <= 0) {
                WikiMod mod = tab.getMod();
                mod.getTabs().remove(tab);
                removeDirectory(tab.getPath());
                //Now that we have deleted a tab, let's check if the mod has anymore tabs
                if (mod.getTabs().size() <= 0) {
                    //If it has no more tabs, delete the mod also
                    WikiRegistry.instance().getMods().remove(mod);
                    removeDirectory(mod.getPath());
                }
            }
        }
    }

    private void removeDirectory(String path) {
        File directory = new File(path).getParentFile();
        ELogger.log(Level.INFO, "Deleting all files in the directory: " + directory);
        String[] files = directory.list();
        if (files != null) {
            for (String s : files) {
                File currentFile = new File(directory.getPath(), s);
                currentFile.delete();
            }
        }

        directory.delete();
    }
}
