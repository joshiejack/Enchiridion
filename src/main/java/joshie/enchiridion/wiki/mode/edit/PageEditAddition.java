package joshie.enchiridion.wiki.mode.edit;

import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.gui.GuiTextEdit;

public class PageEditAddition extends GuiPageEditBox {
    public PageEditAddition() {
        super("addition");
    }

    @Override
    public void add() {        
        ConfirmAddition.mod = mod.getText();
        ConfirmAddition.tab = tab.getText();
        ConfirmAddition.cat = cat.getText();
        ConfirmAddition.page = page.getText();
        WikiHelper.setVisibility(ConfirmAddition.class, true);
    }
    
    public void setToDefault() {
        this.page.setText("");
        if(GuiTextEdit.isSelected(page)) {
            GuiTextEdit.select(page);
        }
    }
}
