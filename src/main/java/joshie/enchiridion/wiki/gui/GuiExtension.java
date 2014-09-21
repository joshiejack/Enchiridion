package joshie.enchiridion.wiki.gui;

import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.WikiMod;
import joshie.enchiridion.wiki.WikiPage;
import joshie.enchiridion.wiki.WikiTab;

public abstract class GuiExtension {
    public abstract void draw();
    public void clicked(int button) {
        return;
    }
    
    public void release(int button) {
        return;
    }

    public void follow() {
        return;
    }
    
    public void scroll(boolean scrollingDown) {
        return;
    }
    
    public WikiTab getTab() {
        return WikiHelper.gui.tab;
    }
    
    public int getHeight() {
        return WikiHelper.height;
    }
    
    public void drawRect(int x, int y, int x2, int y2, int color) {
        WikiHelper.drawRect(x, y, x2, y2, color);
    }
    
    public void horizontalGradient(int x, int y, int x2, int y2, int from, int to) {
        WikiHelper.horizontalGradient(x, y, x2, y2, from, to);
    }
    
    public void verticalGradient(int x, int y, int x2, int y2, int from, int to) {
        WikiHelper.verticalGradient(x, y, x2, y2, from, to);
    }
    
    public void drawScaledCentredText(float scale, String text, int x, int y, int color) {
        WikiHelper.drawScaledCentredText(scale, text, x, y, color);
    }
    
    public void drawScaledText(float scale, String text, int x, int y, int color) {
        WikiHelper.drawScaledText(scale, text, x, y, color);
    }
    
    public WikiPage getPage() {
        return WikiHelper.getPage();
    }
    
    public void fixLighting() {
        WikiHelper.fixLighting();
    }
    
    public boolean isEditMode() {
        return WikiHelper.isEditMode();
    }
    
    public void setPage(String mod, String tab, String cat, String page) {
        WikiHelper.setPage(mod, tab, cat, page);
    }
    
    public WikiMod getMod() {
        return WikiHelper.getMod();
    }
}
