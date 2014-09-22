package joshie.enchiridion.wiki.mode;

import static joshie.enchiridion.wiki.WikiHelper.gui;
import static joshie.enchiridion.wiki.gui.GuiMain.button_id;

import java.util.List;

import joshie.enchiridion.wiki.WikiCategory;
import joshie.enchiridion.wiki.WikiMod;
import joshie.enchiridion.wiki.WikiPage;
import joshie.enchiridion.wiki.WikiRegistry;
import joshie.enchiridion.wiki.WikiTab;
import joshie.enchiridion.wiki.mode.edit.EditMode;

public class SaveMode extends WikiMode {
    private static final SaveMode instance = new SaveMode();
    private static boolean isDirty;

    public static SaveMode getInstance() {
        return instance;
    }

    public static void markDirty() {
        isDirty = true;
    }

    @Override
    public List addButtons(List list) {
        list.add(new ButtonSwitchMode(button_id++, 900, 14, 1, 1, "edit", 2F, EditMode.getInstance()));
        return list;
    }

    @Override
    public void onSwitch() {
        gui.page.setEditMode(false);
        if (isDirty) {
            try {
                for (WikiMod mod : WikiRegistry.instance().getMods()) {
                    if (mod.isDirty()) {
                        for (WikiTab tab : mod.getTabs()) {
                            if (tab.isDirty()) {
                                for (WikiCategory cat : tab.getCategories()) {
                                    if (cat.isDirty()) {
                                        for(WikiPage page: cat.getPages()) {
                                            if(page.isDirty()) {
                                                page.getData().refreshY();
                                                page.save();
                                            }
                                        }
                                        
                                        cat.save();
                                    }
                                }

                                tab.save();
                            }
                        }

                        mod.save();
                    }
                }
                
                isDirty = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
