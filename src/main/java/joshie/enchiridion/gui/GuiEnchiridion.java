package joshie.enchiridion.gui;

import java.util.HashSet;
import java.util.Set;

import joshie.enchiridion.designer.editor.AbstractEditor;
import net.minecraft.client.gui.GuiScreen;

public class GuiEnchiridion extends GuiScreen {
    public static final Set<AbstractEditor> editors = new HashSet();
    public int mouseX = 0;
    public int mouseY = 0;
    
    @Override
    public void drawScreen(int x, int y, float tick) {
        for (AbstractEditor e: editors) {
            if (e.isActive()) {
                e.draw();
            }
        }
    }
    
    @Override
    protected void mouseClicked(int x, int y, int button) {
        for (AbstractEditor e: editors) {
            if(e.isActive()) {
                e.click(x, y, button);
            }
        }
    }
}
