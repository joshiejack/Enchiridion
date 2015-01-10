package joshie.enchiridion.wiki.mode.edit;

import static joshie.enchiridion.wiki.WikiHelper.gui;
import static joshie.enchiridion.wiki.gui.GuiMain.button_id;

import java.util.List;

import joshie.enchiridion.wiki.elements.ElementBox;
import joshie.enchiridion.wiki.elements.ElementImage;
import joshie.enchiridion.wiki.elements.ElementItem;
import joshie.enchiridion.wiki.elements.ElementLink;
import joshie.enchiridion.wiki.elements.ElementRecipe;
import joshie.enchiridion.wiki.elements.ElementText;
import joshie.enchiridion.wiki.mode.ButtonSwitchMode;
import joshie.enchiridion.wiki.mode.SaveMode;
import joshie.enchiridion.wiki.mode.WikiMode;

public class EditMode extends WikiMode {
    private static final EditMode instance = new EditMode();
    public static EditMode getInstance() {
        return instance;
    }

    @Override
    public List addButtons(List list) {
        int yCoord = 50;
        list.add(new ButtonSwitchMode(button_id, 973, 14, 1, 1, "save", 2F, SaveMode.getInstance()));
        list.add(new ButtonWikiLayersVsMenu(button_id++, 1040, yCoord));
        list.add(new ButtonWikiPriority(button_id++, 1040, yCoord += 50));
        list.add(new ButtonWikiEdit(button_id++, 1040, yCoord += 50, ElementText.class, list));
        list.add(new ButtonWikiEdit(button_id++, 1040, yCoord += 50, ElementImage.class, list));
        list.add(new ButtonWikiEdit(button_id++, 1040, yCoord += 50, ElementItem.class, list));
        list.add(new ButtonWikiEdit(button_id++, 1040, yCoord += 50, ElementLink.class, list));
        list.add(new ButtonWikiEdit(button_id++, 1040, yCoord += 50, ElementBox.class, list));
        list.add(new ButtonWikiEdit(button_id++, 1040, yCoord += 50, ElementRecipe.class, list));
        return list;
    }
    
    @Override
    public void onSwitch() {
        gui.page.setEditMode(true);
    }
}
