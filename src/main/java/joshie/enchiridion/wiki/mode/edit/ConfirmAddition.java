package joshie.enchiridion.wiki.mode.edit;

import static joshie.enchiridion.lib.ETranslate.translate;
import joshie.enchiridion.wiki.WikiHelper;

public class ConfirmAddition extends GuiConfirmationBox {
    public static String mod;
    public static String tab;
    public static String cat;
    public static String page;
    
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
        WikiHelper.setPage(mod, tab, cat, page);
    }
}
