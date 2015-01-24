package joshie.enchiridion.wiki.gui;

import static joshie.enchiridion.wiki.WikiHelper.mouseX;
import static joshie.enchiridion.wiki.WikiHelper.mouseY;
import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.WikiMod;
import joshie.enchiridion.wiki.WikiPage;
import joshie.enchiridion.wiki.WikiTab;

public abstract class GuiExtension {
    private boolean isVisible = true;
    
    public boolean isVisible() {
    	return isVisible;
    }
    
    public void setVisibility(boolean isVisible) {
    	this.isVisible = isVisible;
    }

	public void draw() {
        return;
    }
    
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
    
    public void drawTexture(int x, int y, int xStart, int yStart, int xEnd, int yEnd) {
        WikiHelper.drawTexture(x, y, xStart, yStart, xEnd, yEnd);
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
    
    public void drawScaledSplitText(float scale, String text, int x, int y, int color, int length) {
    	WikiHelper.drawScaledSplitText(scale, text, x, y, color, length);
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
    
    public void setMod(WikiMod mod) {
        WikiHelper.setMod(mod);
    }
    
    public WikiMod getMod() {
        return WikiHelper.getMod();
    }
    
    public void setTab(WikiTab tab) {
        WikiHelper.setTab(tab);
    }
    
    public void setPage(WikiPage page) {
        WikiHelper.setPage(page);
    }
    
    public boolean isTabSelected(WikiTab tab) {
        return WikiHelper.isTabSelected(tab);
    }
    
    protected int[] getCategoryBGColors(int pageY) {
        int bg_color_1 = 0xFF1A2738;
        int bg_color_2 = 0xFF255174;
        if (mouseX >= 5 && mouseX <= 245) {
            if (mouseY >= pageY + 79 && mouseY <= pageY + 119) {
                bg_color_1 = 0xCC67C3D2;
                bg_color_2 = 0xCC5498CA;
            }
        }

        return new int[] { bg_color_1, bg_color_2 };
    }

    protected void drawCategoryBox(boolean isVisible, String title, int pageY, int bg_color_1, int bg_color_2) {
        drawRect(5, pageY + 114, 270, pageY + 117, 0xFFC2C29C);
        verticalGradient(5, pageY + 79, 270, pageY + 82, 0xFF3B3C2C, 0xFFC2C29C);
        drawRect(5, pageY + 82, 270, pageY + 83, 0xFF31443E);
        verticalGradient(5, pageY + 83, 270, pageY + 114, bg_color_1, bg_color_2);
        verticalGradient(5, pageY + 115, 270, pageY + 118, 0xFFC2C29C, 0xFF3B3C2C);
        drawRect(5, pageY + 118, 270, pageY + 119, 0xFF071015);
        drawScaledText(2F, (isVisible ? " - " : " + ") + title, 15, 92 + pageY, 0xFFC2C29C);
    }

    protected int[] getContentBGColors(int pageY) {
        int page_bg_1 = 0x4410202F;
        int page_bg_2 = 0x4410202F;
        if (mouseX >= 5 && mouseX <= 270) {
            if (mouseY > pageY + 80 + 39 && mouseY < 80 + pageY + 79) {
                page_bg_1 = 0xCC366374;
                page_bg_2 = 0xCC47849A;
            }
        }

        return new int[] { page_bg_1, page_bg_2 };
    }

    protected void drawContentBox(String title, int pageY, int page_bg_1, int page_bg_2) {
        drawContentBox(title, pageY, page_bg_1, page_bg_2, 5, 80);
    }
    
    protected void drawSearchBox(String title, int pageY, int page_bg_1, int page_bg_2, int x, int y) {
        horizontalGradient(x, pageY + y + 38, x + 265, y + pageY + 40, 0xFF354755, 0XFF192B39);
        verticalGradient(x, pageY + y + 40, x + 265, y + pageY + 42, 0xFF172A39, 0xFF091D28);
        horizontalGradient(x, pageY + y + 42, x + 265, y + pageY + 77, page_bg_1, page_bg_2);
        horizontalGradient(x, pageY + y + 77, x + 265, y + pageY + 78, 0xFF354755, 0XFF192B39);
        verticalGradient(x, pageY + y + 78, x + 265, y + pageY + 80, 0xFF172A39, 0xFF091D28);
        drawScaledText(2F, title, x + 8, 53 + y + pageY, 0xFFC2C29C);
    }
    
    protected void drawContentBox(String title, int pageY, int page_bg_1, int page_bg_2, int x, int y) {
        horizontalGradient(x, pageY + y + 38, x + 265, y + pageY + 40, 0x44354755, 0X44192B39);
        verticalGradient(x, pageY + y + 40, x + 265, y + pageY + 42, 0x44172A39, 0x44091D28);
        horizontalGradient(x, pageY + y + 42, x + 265, y + pageY + 77, page_bg_1, page_bg_2);
        horizontalGradient(x, pageY + y + 77, x + 265, y + pageY + 78, 0x44354755, 0X44192B39);
        verticalGradient(x, pageY + y + 78, x + 265, y + pageY + 80, 0x44172A39, 0x44091D28);
        drawScaledText(2F, title, x + 8, 53 + y + pageY, 0xFFC2C29C);
    }
    
    public void keyTyped(char character, int key) {
        return;
    }
}
