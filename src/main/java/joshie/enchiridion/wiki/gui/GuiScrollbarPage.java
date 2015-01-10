package joshie.enchiridion.wiki.gui;

import joshie.lib.helpers.OpenGLHelper;

public class GuiScrollbarPage extends GuiExtension {
    @Override
    public void draw() {
        //Black Box
        drawRect(1002, 43, 1019, getHeight(), 0xFF000000);
        
        //Draw Scrollbar if the menu is long as fuck.
        drawRect(1002, 48, 1004, getHeight() - 155, 0xFFC2C29C);
        drawRect(1014, 48, 1016, getHeight() - 155, 0xFFC2C29C);
        drawRect(1002, getHeight() - 157, 1016, getHeight() - 155, 0xFFC2C29C);
        drawRect(1002, 46, 1016, 48, 0xFFC2C29C);
        OpenGLHelper.fixColors();
    }
}
