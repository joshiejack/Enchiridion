package joshie.enchiridion.gui.book.buttons;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IPage;
import joshie.enchiridion.gui.book.features.FeatureText;

public class ButtonInsertText extends ButtonAbstract {
    public ButtonInsertText() {
        super("text");
    }

    @Override
    public void performAction() {
        IPage current = EnchiridionAPI.book.getPage();
        FeatureText feature = new FeatureText(EConfig.SETTINGS.defaultText.get());
        current.addFeature(feature, 0, current.getScroll(), 200D, 80D, false, false, false);
    }
}