package joshie.enchiridion.books.buttons;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.IPage;
import joshie.enchiridion.books.features.FeatureButton;
import joshie.enchiridion.books.features.actions.ActionJumpPage;
import joshie.enchiridion.lib.EInfo;

public class ButtonInsertButton extends AbstractButton {
	public ButtonInsertButton() {
		super("arrow");
	}

	@Override
	public void performAction() {
		IPage current = EnchiridionAPI.book.getPage();
		FeatureButton feature = new FeatureButton(EInfo.TEXPATH + "arrow_right_off.png", EInfo.TEXPATH + "arrow_right_on.png", new ActionJumpPage().create());
		current.addFeature(feature, 0, 0, 18D, 10D, false, false);
	}
}
