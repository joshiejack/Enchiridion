package joshie.enchiridion.wiki.gui;

import static joshie.enchiridion.wiki.WikiHelper.drawScaledTexture;
import static joshie.enchiridion.wiki.WikiHelper.getIntFromMouse;
import static joshie.enchiridion.wiki.gui.GuiMain.texture;
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
        
      //Draw the Bottom Arrow
        if (getIntFromMouse(284, 297, getHeight() - 170, getHeight() - 157, 0, 1) == 1) {
            drawScaledTexture(texture, 284, getHeight() - 170, 229, 201, 8, 13, 1F);
        } else {
            drawScaledTexture(texture, 284, getHeight() - 170, 216, 201, 8, 13, 1F);
        }
        
        //Draw the Top Arrow
        if (getIntFromMouse(284, 297, 48, 61, 0, 1) == 1) {
            drawScaledTexture(texture, 284, 48, 229, 191, 8, 13, 1F);
        } else {
            drawScaledTexture(texture, 284, 48, 216, 191, 8, 13, 1F);
        }
    }
}
