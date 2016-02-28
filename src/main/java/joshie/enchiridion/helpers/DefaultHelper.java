package joshie.enchiridion.helpers;

import joshie.enchiridion.api.IFeatureProvider;
import joshie.enchiridion.api.IPage;
import joshie.enchiridion.books.features.Feature;
import joshie.enchiridion.books.features.FeatureButton;
import joshie.enchiridion.books.features.actions.ActionNextPage;
import joshie.enchiridion.books.features.actions.ActionPreviousPage;
import joshie.enchiridion.lib.EInfo;

public class DefaultHelper {
	public static void addArrows(IPage page) {
		FeatureButton left = new FeatureButton(EInfo.TEXPATH + "arrow_left_off.png", EInfo.TEXPATH + "arrow_left_on.png", new ActionPreviousPage());
		page.addFeature(left, 21, 200, 18, 10, true, false);
		FeatureButton right = new FeatureButton(EInfo.TEXPATH + "arrow_right_off.png", EInfo.TEXPATH + "arrow_right_on.png", new ActionNextPage());
		page.addFeature(right, 387, 200, 18, 10, true, false);
	}
	
	public static IPage addDefaults(IPage page) {
		addArrows(page);
		
		//Initialise everything
		for (IFeatureProvider feature: page.getFeatures()) {
			feature.getFeature().update(feature);
		}
		
		return page;
	}
}
