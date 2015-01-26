package joshie.enchiridion.designer.features;

import com.google.gson.annotations.Expose;

public class FeatureText extends Feature {
    @Expose
    private String text = "Lorem ipsum";
    @Expose
    private int color = 4210752;
    @Expose
    private int wrap = 500;
    
    @Override
    public void drawFeature() {
        gui.mc.fontRenderer.drawSplitString(text, x, y, wrap, color);
    }
}
