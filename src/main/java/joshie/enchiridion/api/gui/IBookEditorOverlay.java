package joshie.enchiridion.api.gui;

import net.minecraft.util.text.ITextComponent;

import java.util.List;

public interface IBookEditorOverlay {
    void draw(int mouseX, int mouseY);
    void addToolTip(List<ITextComponent> tooltip, int mouseX, int mouseY);
    void keyTyped(char character, int key);
    boolean mouseClicked(int mouseX, int mouseY);
    void mouseReleased(int mouseX, int mouseY);
    void scroll(boolean down, int mouseX, int mouseY);
    void updateSearch(String string);
}