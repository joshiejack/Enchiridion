package joshie.enchiridion.books.buttons;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.IPage;
import joshie.enchiridion.books.features.FeatureItem;

public class ButtonDeletePage extends AbstractButton {
	public ButtonDeletePage() {
		super("delete");
	}

	@Override
	public void performAction() {
		/** TODO: COLOR! **/
	}
	
	@Override
	public boolean isLeftAligned() {
		return false;
	}
}
