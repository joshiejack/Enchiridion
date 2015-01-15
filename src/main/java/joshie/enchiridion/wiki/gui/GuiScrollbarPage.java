package joshie.enchiridion.wiki.gui;

import joshie.enchiridion.wiki.WikiHelper;


public class GuiScrollbarPage extends GuiScrollbar {
    public static boolean isMoving = false;
    public static int lastY;
    
    public GuiScrollbarPage() {
        super(1002);
    }
    
    public int getScrollHeight() {
        return 100;
    }

    @Override
    public boolean isVisible() {
        return WikiHelper.getPage().getData().getScrollMaximum(WikiHelper.isEditMode()) > 0;
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
