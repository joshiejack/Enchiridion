package joshie.enchiridion.designer.features;

import com.google.gson.annotations.Expose;

public class FeatureBox extends Feature {
    @Expose
    private int color = 0xFFFFFFFF;

    @Override
    public void drawFeature() {
        gui.drawRect(left, top, right, bottom, 0xFFFFFFFF);
    }
}
