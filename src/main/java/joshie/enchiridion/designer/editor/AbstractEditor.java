package joshie.enchiridion.designer.editor;

import java.util.HashSet;
import java.util.Set;

import joshie.enchiridion.designer.DrawHelper;
import joshie.enchiridion.designer.DrawHelper.DrawType;
import joshie.enchiridion.gui.GuiEnchiridion;

public abstract class AbstractEditor {
    public AbstractEditor() {
        GuiEnchiridion.editors.add(this);
    }
    
    protected void clear() {
        for (AbstractEditor e: GuiEnchiridion.editors) {
            e.deselect();
        }
    }
    
    public abstract boolean isActive();
    public abstract void deselect();
    
    /** Draw **/
    public void draw() {
        if (DrawHelper.getDrawType() == DrawType.BOOK) drawBook();
        else if (DrawHelper.getDrawType() == DrawType.WIKI) drawWiki();
    }
    
    protected abstract void drawBook();
    protected abstract void drawWiki();
    
    /** Click **/
    public void click(int mouseX, int mouseY, int button) {
        if (DrawHelper.getDrawType() == DrawType.BOOK) clickBook(mouseX, mouseY, button);
        else if (DrawHelper.getDrawType() == DrawType.WIKI) clickWiki(mouseX, mouseY, button);
    }
    
    protected abstract void clickBook(int mouseX, int mouseY, int button);
    protected abstract void clickWiki(int mouseX, int mouseY, int button);
}
