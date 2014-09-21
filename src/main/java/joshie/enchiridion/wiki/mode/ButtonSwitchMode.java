package joshie.enchiridion.wiki.mode;

import static joshie.enchiridion.wiki.WikiHelper.gui;

public class ButtonSwitchMode extends ButtonBase {
    protected final WikiMode mode;

    public ButtonSwitchMode(int id, int x, int y, int width, int height, String text, float scale, WikiMode mode) {
        super(id, x, y, width, height, text, 2F);
        this.mode = mode;
    }

    @Override
    public void onClicked() {
        if (mode == SaveMode.getInstance()) {
            SaveMode.getInstance().markDirty();
        }

        gui.setMode(mode);
    }
}
