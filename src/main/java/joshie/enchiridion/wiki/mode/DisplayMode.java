package joshie.enchiridion.wiki.mode;

import java.util.List;

public class DisplayMode extends WikiMode {
    private static final DisplayMode instance = new DisplayMode();
    public static DisplayMode getInstance() {
        return instance;
    }
    
    @Override
    public List addButtons(List list) {
        return list;
    }

    @Override
    public void onSwitch() {
        return;
    }
}
