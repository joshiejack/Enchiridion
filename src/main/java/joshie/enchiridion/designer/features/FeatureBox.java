package joshie.enchiridion.designer.features;

import com.google.gson.annotations.Expose;

public class FeatureBox extends Feature {
    @Expose
    private int color = 0xFFFFFFFF;

    @Override
    public void drawFeature() {
        gui.drawRect(x, y, x + width, y + height, 0xFFFFFFFF);
    }
}
