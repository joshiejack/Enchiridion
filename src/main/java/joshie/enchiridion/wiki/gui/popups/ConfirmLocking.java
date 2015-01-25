package joshie.enchiridion.wiki.gui.popups;

import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.gui.GuiMain;
import joshie.enchiridion.wiki.mode.DisplayMode;
import joshie.enchiridion.wiki.mode.SaveMode;

public class ConfirmLocking extends Confirm {
    public ConfirmLocking() {
        super("lock");
    }

    @Override
    public void confirm() {
        WikiHelper.getPage().getData().lock();
        WikiHelper.getPage().markDirty();
        SaveMode.getInstance().markDirty();
        GuiMain.setMode(SaveMode.getInstance());
        GuiMain.setMode(DisplayMode.getInstance());
    }
}
