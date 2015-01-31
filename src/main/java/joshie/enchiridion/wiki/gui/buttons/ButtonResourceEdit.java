package joshie.enchiridion.wiki.gui.buttons;

import java.util.List;

import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.gui.popups.PageEditResource;

public class ButtonResourceEdit extends ButtonWikiEdit {
    public ButtonResourceEdit(int id, int x, int y, Class clazz, List list) {
        super(id, x, y, clazz, list);
    }

    @Override
    public void onClicked() {
        if (((PageEditResource) (WikiHelper.getInstance(PageEditResource.class))).isEditingResource()) {
            WikiHelper.setVisibility(PageEditResource.class, true);
        } else super.onClicked();
    }
}
