package joshie.enchiridion.wiki.gui.buttons;

import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.gui.GuiLayers;
import joshie.enchiridion.wiki.gui.GuiMenu;

public class ButtonWikiLayersVsMenu extends ButtonWikiSwitch {
    public ButtonWikiLayersVsMenu(int id, int x, int y) {
        super(id, x, y, "layers", "menu");
    }

    @Override
    public void onClicked() {
        try {
            if(GuiMenu.isEditing()) {
                WikiHelper.clearEditGUIs();
                GuiLayers.setActive(true);
            } else {
                WikiHelper.clearEditGUIs();
                GuiLayers.setActive(false);
                GuiMenu.setEditing();
            }
        } catch (Exception e) {}
    }

    @Override
    public boolean isMode1() {
        return GuiMenu.isEditing();
    }
}
