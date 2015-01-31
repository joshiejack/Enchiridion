package joshie.enchiridion.wiki.gui.scrollbars;

import static joshie.enchiridion.wiki.WikiHelper.getHeight;
import joshie.enchiridion.wiki.WikiHelper;

public class ScrollbarPage extends ScrollbarAbstract {
    public static boolean isMoving = false;
    public static int lastY;

    public ScrollbarPage() {
        super(1002);
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
        int scrollbarMaximum = getHeight() - 235; //Maximum Height of the Scrollbar aka 100% USE UR BRAIN FOR SOME MATHS BOY
        return scrollbarMaximum;
    }

    @Override
    public int getScrollbarPosition() {
        return WikiHelper.getPage().getData().getScroll();
    }

    @Override
    public void scroll(int amount) {
        WikiHelper.getPage().getData().scroll(WikiHelper.isEditMode(), amount);
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
