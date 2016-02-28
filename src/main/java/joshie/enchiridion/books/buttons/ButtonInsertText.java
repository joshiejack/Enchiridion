package joshie.enchiridion.books.buttons;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.IPage;
import joshie.enchiridion.books.features.FeatureText;

public class ButtonInsertText extends AbstractButton {
	public ButtonInsertText() {
		super("text");
	}

	@Override
	public void performAction() {
		IPage current = EnchiridionAPI.draw.getPage();
		FeatureText feature = new FeatureText(EConfig.defaultText);
		EnchiridionAPI.draw.getPage().addFeature(feature, 0, 0, 200D, 80D, false, false);
	}
}
