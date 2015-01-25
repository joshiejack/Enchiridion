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
    public void add() {        
        if(editing != null) {
            editing.set(mod.getText(), tab.getText(), cat.getText(), page.getText());
        }
    }
    
    @Override
    public void cancel() {
        super.cancel();
        
        //reset  what we are editing after finishing
        ((PageEditLink)(WikiHelper.getInstance(PageEditLink.class))).setEditing(null);
    }
    
    public boolean isEditingLink() {
        return editing != null;
    }

    public void setEditing(ElementLink link) {
        editing = link;
        if(link != null) {
            mod.setText(link.getMod());
            tab.setText(link.getTab());
            cat.setText(link.getCat());
            page.setText(link.getPage());
        }
    }
}
