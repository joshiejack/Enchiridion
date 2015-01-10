package joshie.enchiridion.wiki.gui;

import joshie.lib.helpers.OpenGLHelper;

public class GuiScrollbarMenu extends GuiExtension {
    @Override
    public void draw() {
        //Black Box
        drawRect(278, 43, 295, getHeight(), 0xFF000000);
        
        //Draw Scrollbar if the menu is long as fuck.
        drawRect(281, 48, 283, getHeight() - 155, 0xFFC2C29C);
        drawRect(293, 48, 295, getHeight() - 155, 0xFFC2C29C);
        drawRect(281, getHeight() - 157, 295, getHeight() - 155, 0xFFC2C29C);
        drawRect(281, 46, 295, 48, 0xFFC2C29C);
        OpenGLHelper.fixColors();
    }
}
