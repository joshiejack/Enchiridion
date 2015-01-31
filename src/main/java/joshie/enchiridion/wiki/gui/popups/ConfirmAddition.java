package joshie.enchiridion.wiki.gui.popups;

import static joshie.enchiridion.ETranslate.translate;
import joshie.enchiridion.wiki.WikiHelper;

public class ConfirmAddition extends Confirm {
    static String mod;
    static String tab;
    static String cat;
    static String page;
    
    public ConfirmAddition() {
        super("addition");
    }
    
    @Override
    public String getTitle() {
        return translate("pageedit." + descriptor + ".title");
    }
    
    @Override
    public String getDescription() {
        return translate("pageedit." + descriptor + ".description", mod, tab, cat, page);
    }
    
    @Override
    public void confirm() {
        WikiHelper.loadPage(mod, tab, cat, page);
    }
}
