package joshie.enchiridion.wiki.gui.popups;

import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.gui.GuiTextEdit;

public class PageEditAddition extends PageEdit {
    public PageEditAddition() {
        super("addition");
    }

    @Override
    public boolean add() {
        ConfirmAddition.mod = mod.getText();
        ConfirmAddition.tab = tab.getText();
        ConfirmAddition.cat = cat.getText();
        ConfirmAddition.page = page.getText();

        if (ConfirmAddition.isValidated()) {
            WikiHelper.setVisibility(ConfirmAddition.class, true);
            return true;
        } else return false;
    }

    public PageEditAddition setToDefault() {
        this.page.setText("");
        if (GuiTextEdit.isSelected(page)) {
            GuiTextEdit.select(page);
        }

        return this;
    }
}
