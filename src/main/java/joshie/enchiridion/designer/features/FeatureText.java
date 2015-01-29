package joshie.enchiridion.designer.features;

import joshie.enchiridion.designer.DesignerHelper;

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
        DesignerHelper.drawSplitString(text, left, top, wrap, color);
    }

    @Override
    public void loadEditor() {
        // DRAW TEXT+++, TEXT---, BBCODE MODE
        
    }
}
