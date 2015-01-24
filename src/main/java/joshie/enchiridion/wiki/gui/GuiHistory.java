package joshie.enchiridion.wiki.gui;

import static joshie.enchiridion.wiki.WikiHelper.drawScaledTexture;
import static joshie.enchiridion.wiki.WikiHelper.getIntFromMouse;
import static joshie.enchiridion.wiki.gui.GuiMain.texture;

import java.util.ArrayList;

import joshie.enchiridion.wiki.WikiPage;
import joshie.lib.helpers.OpenGLHelper;

public class GuiHistory extends GuiExtension {
    /** A store of all the pages you've visited in this session **/
    public static ArrayList<WikiPage> history = new ArrayList();
    public static int page_number = -1; //The current index of the arraylist

    public static void backPage() {
        if (page_number > 0) {
            page_number -= 1;
            GuiMain.setPage(history.get(page_number));
        }
    }
    
    public static void delete() {
        history.remove(page_number);
        backPage();
    }

    public static void forwardPage() {
        if (page_number < (history.size() - 1)) {
            page_number += 1;
            GuiMain.setPage(history.get(page_number));
        }
    }

    public static void newPage(WikiPage page) {
        page_number += 1;
        history.add(page);
        GuiMain.setPage(page);
    }

    @Override
    public void draw() {
        OpenGLHelper.fixColors();
        
        if (getIntFromMouse(-18, 5, 5, 33, 0, 1) == 1) {
            drawScaledTexture(texture, -18, 5, 189, 2, 24, 28, 1F);
        } else {
            drawScaledTexture(texture, -18, 5, 134, 2, 24, 28, 1F);
        }
        
        if (getIntFromMouse(273, 297, 5, 33, 0, 1) == 1) {
            drawScaledTexture(texture, 273, 5, 216, 2, 24, 28, 1F);
        } else {
            drawScaledTexture(texture, 273, 5, 161, 2, 24, 28, 1F);
        }
    }

    @Override
    public void clicked(int button) {
        if (button == 0) {
            if (getIntFromMouse(-18, 5, 5, 33, 0, 1) == 1) {
                backPage();
            }
            
            if (getIntFromMouse(273, 297, 5, 33, 0, 1) == 1) {
                forwardPage();
            }
        }
    }
}
