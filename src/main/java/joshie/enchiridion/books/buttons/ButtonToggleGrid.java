package joshie.enchiridion.books.buttons;

import joshie.enchiridion.books.gui.GuiGrid;

public class ButtonToggleGrid extends AbstractButton {
	public ButtonToggleGrid() {
		super("grid");
	}
	
	@Override
	public boolean isLeftAligned() {
		return false;
	}

	@Override
	public void performAction() {
		GuiGrid.INSTANCE.toggle();
	}
}
