package joshie.enchiridion.wiki.gui.scrollbars;

import static joshie.enchiridion.wiki.WikiHelper.getHeight;

public class ScrollbarMenu extends ScrollbarAbstract {
    public static boolean isMoving = false;
    public static int lastY;

    public ScrollbarMenu() {
        super(278);
    }

    public int getScrollHeight() {
        return 100;
    }

    @Override
    public boolean displayScroll() {
        return false;
    }

    @Override
    public int getScrollbarHeight() {
        int scrollbarMaximum = getHeight() - 235;
        return scrollbarMaximum;
    }

    @Override
    public int getScrollbarPosition() {
        return 0;
    }

    @Override
    public void scroll(int amount) {
        //;
    }

    @Override
    public void setLastY(int y) {
        this.lastY = y;
    }

    @Override
    public int getLastY() {
        return lastY;
    }

    @Override
    public void setMoving(boolean value) {
        this.isMoving = value;
    }

    @Override
    public boolean isMoving() {
        return isMoving;
    }
}
