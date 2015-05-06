package joshie.enchiridion.wiki.mode;

import static joshie.enchiridion.wiki.WikiHelper.gui;
import static joshie.enchiridion.wiki.gui.GuiMain.button_id;

import java.util.List;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.util.IWikiMode;
import joshie.enchiridion.wiki.WikiCategory;
import joshie.enchiridion.wiki.WikiMod;
import joshie.enchiridion.wiki.WikiPage;
import joshie.enchiridion.wiki.WikiRegistry;
import joshie.enchiridion.wiki.WikiTab;
import joshie.enchiridion.wiki.gui.buttons.ButtonAddPage;
import joshie.enchiridion.wiki.gui.buttons.ButtonDeletePage;
import joshie.enchiridion.wiki.gui.buttons.ButtonSwitchMode;

public class SaveMode implements IWikiMode {
    /** This mode is set when you press the save button, or by default if you have editing enabled
     *  It is only ever the mode when the page can be edited. **/
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
        list.add(new ButtonSwitchMode(button_id++, 975, 14, 1, 1, "edit", 2F, EditMode.getInstance()));
        list.add(new ButtonAddPage(button_id++, 723, 14, 1, 1, 2F));

        if (EConfig.ENABLE_DELETE) {
            list.add(new ButtonDeletePage(button_id++, 843, 14, 1, 1, 2F));
        }
        
        return list;
    }

    @Override
    public void onSwitch() {
        if (isDirty && gui.page.shouldSave()) {
            try {
                for (WikiMod mod : WikiRegistry.instance().getMods()) {
                    if (mod.isDirty()) {
                        for (WikiTab tab : mod.getTabs()) {
                            if (tab.isDirty()) {
                                for (WikiCategory cat : tab.getCategories()) {
                                    if (cat.isDirty()) {
                                        for (WikiPage page : cat.getPages()) {
                                            if (page.isDirty()) {
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

        gui.page.setEditMode(false);
    }

    @Override
    public WikiMode getType() {
        return WikiMode.DISPLAY;
    }
}
