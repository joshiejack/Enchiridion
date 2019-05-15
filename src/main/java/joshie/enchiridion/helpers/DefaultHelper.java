package joshie.enchiridion.helpers;

import joshie.enchiridion.api.book.IBook;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.api.book.IPage;
import joshie.enchiridion.gui.book.GuiSimpleEditorTemplate;
import joshie.enchiridion.gui.book.buttons.actions.ActionNextPage;
import joshie.enchiridion.gui.book.buttons.actions.ActionPreviousPage;
import joshie.enchiridion.gui.book.features.FeatureButton;
import joshie.enchiridion.util.ELocation;

import java.util.List;

public class DefaultHelper {
    public static IPage addArrows(IPage page) {
        FeatureButton left = new FeatureButton(new ActionPreviousPage());
        left.setResourceLocation(true, new ELocation("arrow_left_on")).setResourceLocation(false, new ELocation("arrow_left_off"));
        page.addFeature(left, 21, 200, 18, 10, true, false, true);
        FeatureButton right = new FeatureButton(new ActionNextPage());
        right.setResourceLocation(true, new ELocation("arrow_right_on")).setResourceLocation(false, new ELocation("arrow_right_off"));
        page.addFeature(right, 387, 200, 18, 10, true, false, true);
        return page;
    }

    public static IPage addDefaults(IBook book, IPage page) {
        if (book.getDefaultFeatures() != null) {
            for (String unique : book.getDefaultFeatures()) {
                List<IFeatureProvider> providers = GuiSimpleEditorTemplate.INSTANCE.getFeaturesFromString(unique);
                for (IFeatureProvider provider : providers) {
                    page.addFeature(provider.getFeature(), provider.getLeft(), provider.getTop(), provider.getWidth(), provider.getHeight(), provider.isLocked(), !provider.isVisible(), provider.isFromTemplate());
                }
            }
        } else addArrows(page);

        //Initialise everything
        for (IFeatureProvider feature : page.getFeatures()) {
            feature.update(page);
        }

        return page;
    }
}