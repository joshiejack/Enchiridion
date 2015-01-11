package joshie.enchiridion.wiki.gui;

import static joshie.enchiridion.wiki.WikiHelper.drawScaledTexture;
import static joshie.enchiridion.wiki.WikiHelper.getIntFromMouse;
import static joshie.enchiridion.wiki.WikiHelper.mouseY;
import static joshie.enchiridion.wiki.gui.GuiMain.texture;
import joshie.enchiridion.wiki.elements.Element;
import joshie.lib.helpers.OpenGLHelper;

public abstract class GuiScrollbar extends GuiExtension {
    public final int x;

    public GuiScrollbar(int xPosition) {
        this.x = xPosition;
    }

    //Start from 1002
    @Override
    public void draw() {
        //Black Box
        drawRect(x, 43, x + 17, getHeight(), 0xFF000000);

        if (isVisible()) {
            //Draw Scrollbar if the menu is long as fuck.
            drawRect(x, 48, x + 2, getHeight() - 155, 0xFFC2C29C);
            drawRect(x + 12, 48, x + 14, getHeight() - 155, 0xFFC2C29C);
            drawRect(x, getHeight() - 157, x + 14, getHeight() - 155, 0xFFC2C29C);
            drawRect(x, 46, x + 14, 48, 0xFFC2C29C);

            OpenGLHelper.fixColors();
            //Draw the Bottom Arrow
            if (getIntFromMouse(x + 3, x + 11, getHeight() - 170, getHeight() - 157, 0, 1) == 1) {
                drawScaledTexture(texture, x + 3, getHeight() - 170, 229, 201, 8, 13, 1F);
            } else {
                drawScaledTexture(texture, x + 3, getHeight() - 170, 216, 201, 8, 13, 1F);
            }

            //Draw the Top Arrow
            if (getIntFromMouse(x + 3, x + 11, 48, 61, 0, 1) == 1) {
                drawScaledTexture(texture, x + 3, 48, 229, 191, 8, 13, 1F);
            } else {
                drawScaledTexture(texture, x + 3, 48, 216, 191, 8, 13, 1F);
            }

            //Draw the Scrollbar
            int scrollHeight = getScrollbarHeight();
            drawRect(x + 2, 63 + getScrollbarPosition(), x + 12, 63 + getScrollbarPosition() + scrollHeight, 0xFF365E7F);
            drawRect(x + 5, 63 + getScrollbarPosition(), x + 9, 63 + getScrollbarPosition() + scrollHeight, 0xFF172742);
        }

        OpenGLHelper.fixColors();
    }

    @Override
    public void clicked(int button) {
        if (getIntFromMouse(x + 2, x + 12, 63 + getScrollbarPosition(), 63 + getScrollbarPosition() + getScrollbarHeight(), 0, 1) == 1) {
            setMoving(true);
        }
    }

    @Override
    public void follow() {
        if (isMoving()) {
            int y = mouseY - Element.BASE_Y;
            int lastY = getLastY();
            scroll(lastY - y);
            setLastY(y);
        }
    }

    @Override
    public void release(int button) {
        setMoving(false);
    }

    public abstract void setLastY(int y);
    public abstract int getLastY();
    public abstract void setMoving(boolean value);
    public abstract boolean isMoving();
    public abstract boolean isVisible();
    public abstract int getScrollbarHeight();
    public abstract int getScrollbarPosition();
    public abstract void scroll(int amount);
}
