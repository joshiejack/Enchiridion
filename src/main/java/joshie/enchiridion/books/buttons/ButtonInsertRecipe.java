package joshie.enchiridion.books.buttons;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.IPage;
import joshie.enchiridion.books.features.FeatureRecipe;

public class ButtonInsertRecipe extends AbstractButton {
	public ButtonInsertRecipe() {
		super("crafting");
	}

	@Override
	public void performAction() {
		IPage current = EnchiridionAPI.book.getPage();
		FeatureRecipe feature = new FeatureRecipe(EConfig.getDefaultItem());
		current.addFeature(feature, 0, 0, 160D, 80D, false, false);
	}
}
