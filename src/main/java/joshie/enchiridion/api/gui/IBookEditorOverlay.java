package joshie.enchiridion.api.gui;

import net.minecraft.client.gui.IGuiEventListener;

import java.util.List;

public interface IBookEditorOverlay {
    void draw(int mouseX, int mouseY);
    void addToolTip(List<String> tooltip, int mouseX, int mouseY);
    void charTyped(char character, int key);
    boolean mouseClicked(int mouseX, int mouseY);
    void mouseReleased(int mouseX, int mouseY);
    void scroll(boolean down, int mouseX, int mouseY);
    void updateSearch(String string);
    void init();
    void tick();
    IGuiEventListener getFocused();
}