package joshie.enchiridion.wiki.gui.buttons;

import java.util.List;

import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.gui.popups.PageEditLink;

public class ButtonLinkEdit extends ButtonWikiEdit {
    public ButtonLinkEdit(int id, int x, int y, Class clazz, List list) {
        super(id, x, y, clazz, list);
    }

    @Override
    public void onClicked() {
        if(((PageEditLink)(WikiHelper.getInstance(PageEditLink.class))).isEditingLink()) {
            WikiHelper.setVisibility(PageEditLink.class, true);
        } else super.onClicked();
    }
}
