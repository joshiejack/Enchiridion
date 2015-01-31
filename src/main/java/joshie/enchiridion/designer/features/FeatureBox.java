package joshie.enchiridion.designer.features;

import joshie.enchiridion.designer.DesignerHelper;

public class FeatureBox extends FeatureColorable {
    @Override
    public void drawFeature() {
        super.drawFeature();

        DesignerHelper.drawRect(left, top, right, bottom, colorI);
    }
}
