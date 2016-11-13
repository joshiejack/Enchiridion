package joshie.enchiridion.gui.book.buttons;

import joshie.enchiridion.gui.book.GuiGrid;

public class ButtonToggleGrid extends ButtonAbstract {
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