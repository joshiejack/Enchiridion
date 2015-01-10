package joshie.enchiridion.wiki.mode.edit;

import static joshie.enchiridion.wiki.WikiHelper.gui;

public class ButtonWikiPriority extends ButtonWikiSwitch {
    public ButtonWikiPriority(int id, int x, int y) {
        super(id, x, y, "priority.add", "priority.remove");
    }

    @Override
    public void onClicked() {
        gui.page.switchPriority();
    }

    @Override
    public boolean isMode1() {
        return !gui.page.getData().isPrioritised();
    }
}
