package joshie.enchiridion.api.gui;

import java.util.List;

public interface IBookEditorOverlay {
	public void draw(int mouseX, int mouseY);
	public void addToolTip(List<String> tooltip, int mouseX, int mouseY);
	public void keyTyped(char character, int key);
	public boolean mouseClicked(int mouseX, int mouseY);
	public void mouseReleased(int mouseX, int mouseY);
	public void scroll(boolean down, int mouseX, int mouseY);
	public void updateSearch(String string);
}
