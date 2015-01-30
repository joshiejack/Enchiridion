package joshie.enchiridion.designer.features;

import joshie.enchiridion.designer.DesignerHelper;
import joshie.enchiridion.helpers.OpenGLHelper;

import com.google.gson.annotations.Expose;

public class FeatureText extends FeatureWithText {
    @Expose
    private String text = "Lorem ipsum";
    @Expose
    private int color = 4210752;
    @Expose
    private int wrap = 500;

    @Override
    public void drawFeature() {
        DesignerHelper.drawSplitString(getText(), left, top, wrap, color);
    }

    @Override
    public String getTextField() {
        return this.text;
    }

    @Override
    public void setTextField(String str) {
        this.text = str;
    }

    @Override
    public void loadEditor() {
        // TODO: DRAW TEXT+++, TEXT---, BBCODE MODE
    }
}
