package joshie.enchiridion.wiki.mode.edit;

import joshie.enchiridion.wiki.WikiHelper;

public class ConfirmLocking extends GuiConfirmationBox {
    public ConfirmLocking() {
        super("lock");
    }

    @Override
    public void confirm() {
        WikiHelper.getPage().getData().lock();
    }
}
