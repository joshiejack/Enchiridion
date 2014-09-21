package joshie.enchiridion.wiki.mode;

import java.util.List;

public abstract class WikiMode {
    public abstract List addButtons(List list);
    public abstract void onSwitch();
    
    public static WikiMode switchMode() {
        return DisplayMode.getInstance();
    }
}
