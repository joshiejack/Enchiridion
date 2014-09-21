package joshie.enchiridion.wiki.mode.edit;

import static joshie.enchiridion.wiki.WikiHelper.gui;
import joshie.enchiridion.wiki.elements.Element;
import joshie.enchiridion.wiki.gui.GuiMain;

public abstract class ButtonWikiTextEdit extends ButtonWikiEdit {
    public ButtonWikiTextEdit(GuiMain wiki, int id, int x, int y, String text) {
        super(id, x, y, text);
        this.visible = false;
    }

    @Override
    public void onClicked() {
        Element component = gui.page.getSelected();
        if(component != null) {
            affect(component);
        }
    }
    
    public abstract void affect(Element component);
}
