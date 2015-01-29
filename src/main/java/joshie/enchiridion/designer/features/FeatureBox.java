package joshie.enchiridion.designer.features;

import joshie.enchiridion.designer.DesignerHelper;

import com.google.gson.annotations.Expose;

public class FeatureBox extends Feature {
    @Expose
    private int color = 0xFFFFFFFF;

    @Override
    public void drawFeature() {
        DesignerHelper.drawRect(left, top, right, bottom, 0xFFFFFFFF);
    }
}
