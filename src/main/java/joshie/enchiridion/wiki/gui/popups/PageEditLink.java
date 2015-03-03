package joshie.enchiridion.wiki.gui.popups;

import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.elements.ElementLink;

public class PageEditLink extends PageEdit {
    public ElementLink editing;

    public PageEditLink() {
        super("link");
    }

    @Override
    public String getConfirmationText() {
        return "set";
    }

    @Override
    public boolean add() {
        if (editing != null) {
            editing.set(mod.getText(), tab.getText(), cat.getText(), page.getText());
            return true;
        } else return false;
    }

    @Override
    public void cancel() {
        super.cancel();
        ((PageEditLink) (WikiHelper.getInstance(PageEditLink.class))).setEditing(null);
    }

    public boolean isEditingLink() {
        return editing != null;
    }

    public void setEditing(ElementLink link) {
        editing = link;
        if (link != null) {
            mod.setText(link.getMod());
            tab.setText(link.getTab());
            cat.setText(link.getCat());
            page.setText(link.getPage());
        }
    }
}
