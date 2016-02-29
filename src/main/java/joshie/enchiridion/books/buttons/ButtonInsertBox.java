package joshie.enchiridion.books.buttons;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.IPage;
import joshie.enchiridion.books.features.FeatureBox;

public class ButtonInsertBox extends AbstractButton {
	public ButtonInsertBox() {
		super("box");
	}

	@Override
	public void performAction() {
		IPage current = EnchiridionAPI.book.getPage();
		FeatureBox feature = new FeatureBox("FF000000");
		current.addFeature(feature, 0, 0, 50D, 5D, false, false);
	}
}
