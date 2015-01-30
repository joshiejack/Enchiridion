package joshie.enchiridion.wiki.gui.buttons;

import java.util.List;

import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.gui.popups.ResourceEdit;

public class ButtonResourceEdit extends ButtonWikiEdit {
    public ButtonResourceEdit(int id, int x, int y, Class clazz, List list) {
        super(id, x, y, clazz, list);
    }

    @Override
    public void onClicked() {
        if(((ResourceEdit)(WikiHelper.getInstance(ResourceEdit.class))).isEditingResource()) {
            WikiHelper.setVisibility(ResourceEdit.class, true);
        } else super.onClicked();
    }
}
