package joshie.enchiridion.gui.book.features;

import joshie.enchiridion.api.book.IFeature;

public class FeatureError extends FeatureAbstract {
    public FeatureError(){}

    @Override
    public IFeature copy() {
        return new FeatureError();
    }
}
