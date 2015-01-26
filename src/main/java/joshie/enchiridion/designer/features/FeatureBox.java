package joshie.enchiridion.designer.features;

import com.google.gson.annotations.Expose;

public class FeatureBox extends Feature {
    @Expose
    private int color = 4210752;

    @Override
    public void drawFeature() {
        gui.drawRect(x, y, x + width, x + height, color);
    }
}
