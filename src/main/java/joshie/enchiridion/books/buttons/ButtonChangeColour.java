package joshie.enchiridion.books.buttons;

import joshie.lib.editables.IColorable;

public class ButtonChangeColour extends AbstractButton implements IColorable {
	public ButtonChangeColour() {
		super("colour");
	}

	@Override
	public void performAction() {
		/** TODO: COLOR! **/
	}
	
	@Override
	public boolean isLeftAligned() {
		return false;
	}

	@Override
	public String getColorAsHex() {
		return null;
	}

	@Override
	public void setColorAsHex(String color) {
		//EnchiridionAPI.draw.setBookColor(color)
	}
}
