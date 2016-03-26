package joshie.enchiridion.helpers;

import joshie.enchiridion.api.book.IBook;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.api.book.IPage;
import joshie.enchiridion.gui.book.GuiSimpleEditorTemplate;
import joshie.enchiridion.gui.book.buttons.actions.ActionNextPage;
import joshie.enchiridion.gui.book.buttons.actions.ActionPreviousPage;
import joshie.enchiridion.gui.book.features.FeatureButton;
import joshie.enchiridion.lib.EInfo;

import java.util.List;

public class DefaultHelper {
	public static IPage addArrows(IPage page) {
		FeatureButton left = new FeatureButton(EInfo.TEXPATH + "arrow_left_off.png", EInfo.TEXPATH + "arrow_left_on.png", new ActionPreviousPage());
		page.addFeature(left, 21, 200, 18, 10, true, false);
		FeatureButton right = new FeatureButton(EInfo.TEXPATH + "arrow_right_off.png", EInfo.TEXPATH + "arrow_right_on.png", new ActionNextPage());
		page.addFeature(right, 387, 200, 18, 10, true, false);
		return page;
	}
	
	public static IPage addDefaults(IBook book, IPage page) {
		if (book.getDefaultFeatures() != null) {
			for (String unique: book.getDefaultFeatures()) {
				List<IFeatureProvider> providers = GuiSimpleEditorTemplate.INSTANCE.getFeaturesFromString(unique);
				for (IFeatureProvider provider: providers) {
					page.addFeature(provider.getFeature(), provider.getLeft(), provider.getTop(), provider.getWidth(), provider.getHeight(), provider.isLocked(), !provider.isVisible());
				}
			}
		} else addArrows(page);
		
		//Initialise everything
		for (IFeatureProvider feature: page.getFeatures()) {
			feature.update(page);
		}
		
		return page;
	}
}
